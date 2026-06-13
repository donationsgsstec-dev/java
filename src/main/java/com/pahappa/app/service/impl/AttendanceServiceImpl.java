package com.pahappa.app.service.impl;

import com.pahappa.app.dto.AttendanceStatistics;
import com.pahappa.app.entity.Attendance;
import com.pahappa.app.entity.Attendance.AttendanceStatus;
import com.pahappa.app.entity.User;
import com.pahappa.app.entity.WorkSchedule;
import com.pahappa.app.repository.AttendanceRepository;
import com.pahappa.app.repository.UserRepository;
import com.pahappa.app.repository.WorkScheduleRepository;
import com.pahappa.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of AttendanceService interface.
 * 
 * This service handles all business logic related to attendance management
 * including sign in, sign out, and attendance record retrieval.
 * 
 * @author Pahappa
 * @version 1.0
 */
@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final QRCodeService qrCodeService;
    private final EmailNotificationService emailNotificationService;
    private final ExcelExportService excelExportService;
    private final WorkScheduleRepository workScheduleRepository;
    private final UserRepository userRepository;

    private final EmailService emailService;

    /**
     * Constructor-based dependency injection
     */
    @Autowired
    public AttendanceServiceImpl(
            AttendanceRepository attendanceRepository,
            EmailService emailService,
            QRCodeService qrCodeService,
            EmailNotificationService emailNotificationService,
            ExcelExportService excelExportService,
            WorkScheduleRepository workScheduleRepository,
            UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.emailService = emailService;
        this.qrCodeService = qrCodeService;
        this.emailNotificationService = emailNotificationService;
        this.excelExportService = excelExportService;
        this.workScheduleRepository = workScheduleRepository;
        this.userRepository = userRepository;
    }

    /**
     * Sign in a user (create new attendance record)
     * @param user The user signing in
     * @return The created attendance record
     * @throws IllegalStateException if user is already signed in
     */
    @Override
    public Attendance signIn(User user) {
        // Check if user is already signed in
        Optional<Attendance> existingAttendance = attendanceRepository.findByUserAndStatus(
            user,
            AttendanceStatus.SIGNED_IN
        );

        if (existingAttendance.isPresent()) {
            throw new IllegalStateException("User is already signed in. Please sign out first.");
        }

        // Check if user has already checked in today (within 24 hours)
        LocalDate today = LocalDate.now();
        boolean hasCheckedInToday = attendanceRepository.existsByUserAndAttendanceDate(user, today);
        
        if (hasCheckedInToday) {
            throw new IllegalStateException("You have already checked in today. You can only check in once per day.");
        }

        // Get active work schedule
        Optional<WorkSchedule> scheduleOpt = workScheduleRepository.findByActiveTrue();
        
        // Create new attendance record
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        LocalDateTime now = LocalDateTime.now();
        attendance.setSignInTime(now);
        attendance.setAttendanceDate(LocalDate.now());
        attendance.setStatus(AttendanceStatus.SIGNED_IN);

        // Check if late
        if (scheduleOpt.isPresent()) {
            WorkSchedule schedule = scheduleOpt.get();
            LocalTime checkInTime = now.toLocalTime();
            LocalTime workStartTime = schedule.getWorkStartTime();
            LocalTime graceEndTime = workStartTime.plusMinutes(schedule.getGracePeriodMinutes());
            
            if (checkInTime.isAfter(graceEndTime)) {
                attendance.setLate(true);
                long minutesLate = Duration.between(workStartTime, checkInTime).toMinutes();
                attendance.setLateMinutes((int) minutesLate);
                attendance.setStatus(AttendanceStatus.LATE);
            }
        }

        Attendance savedAttendance = attendanceRepository.save(attendance);
        
        // Send email notifications
        try {
            String signInTime = savedAttendance.getSignInTime()
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
            emailService.sendAttendanceSignInNotification(
                user.getEmail(),
                user.getUsername(),
                signInTime
            );
            
            // Send late notification if applicable
            if (savedAttendance.isLate() && scheduleOpt.isPresent() &&
                scheduleOpt.get().isNotifyLateArrival()) {
                emailNotificationService.sendLateArrivalNotification(user, savedAttendance);
            }
        } catch (Exception e) {
            System.err.println("Failed to send sign-in email: " + e.getMessage());
        }
        
        return savedAttendance;
    }

    /**
     * Sign out a user (update existing attendance record)
     * @param user The user signing out
     * @return The updated attendance record
     * @throws IllegalStateException if user is not signed in
     */
    @Override
    public Attendance signOut(User user) {
        // Find active attendance record
        Optional<Attendance> attendanceOpt = attendanceRepository.findByUserAndStatus(
            user,
            AttendanceStatus.SIGNED_IN
        );
        
        if (attendanceOpt.isEmpty()) {
            attendanceOpt = attendanceRepository.findByUserAndStatus(user, AttendanceStatus.LATE);
        }

        if (attendanceOpt.isEmpty()) {
            throw new IllegalStateException("User is not signed in. Please sign in first.");
        }

        // Get active work schedule
        Optional<WorkSchedule> scheduleOpt = workScheduleRepository.findByActiveTrue();
        
        // Update attendance record with sign out time
        Attendance attendance = attendanceOpt.get();
        LocalDateTime now = LocalDateTime.now();
        attendance.setSignOutTime(now);
        attendance.setStatus(AttendanceStatus.SIGNED_OUT);

        // Check for early departure and overtime
        if (scheduleOpt.isPresent()) {
            WorkSchedule schedule = scheduleOpt.get();
            LocalTime checkOutTime = now.toLocalTime();
            LocalTime workEndTime = schedule.getWorkEndTime();
            
            if (checkOutTime.isBefore(workEndTime)) {
                attendance.setEarlyDeparture(true);
                long minutesEarly = Duration.between(checkOutTime, workEndTime).toMinutes();
                attendance.setEarlyDepartureMinutes((int) minutesEarly);
            } else if (checkOutTime.isAfter(workEndTime)) {
                long overtimeMinutes = Duration.between(workEndTime, checkOutTime).toMinutes();
                attendance.setOvertimeMinutes((int) overtimeMinutes);
            }
        }

        Attendance savedAttendance = attendanceRepository.save(attendance);
        
        // Send email notifications
        try {
            String signInTime = savedAttendance.getSignInTime()
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
            String signOutTime = savedAttendance.getSignOutTime()
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
            String duration = savedAttendance.getFormattedDuration();
            
            emailService.sendAttendanceSignOutNotification(
                user.getEmail(),
                user.getUsername(),
                signInTime,
                signOutTime,
                duration
            );
            
            // Send early departure notification if applicable
            if (savedAttendance.isEarlyDeparture() && scheduleOpt.isPresent() &&
                scheduleOpt.get().isNotifyEarlyDeparture()) {
                emailNotificationService.sendEarlyDepartureNotification(user, savedAttendance);
            }
        } catch (Exception e) {
            System.err.println("Failed to send sign-out email: " + e.getMessage());
        }
        
        return savedAttendance;
    }

    /**
     * Get current active attendance record for a user
     * @param user The user
     * @return Optional containing active attendance record
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Attendance> getCurrentAttendance(User user) {
        return attendanceRepository.findByUserAndStatus(user, AttendanceStatus.SIGNED_IN);
    }

    /**
     * Check if user is currently signed in
     * @param user The user
     * @return true if user is signed in
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isUserSignedIn(User user) {
        return attendanceRepository.findByUserAndStatus(user, AttendanceStatus.SIGNED_IN).isPresent();
    }

    /**
     * Get all attendance records for a user
     * @param user The user
     * @return List of attendance records
     */
    @Override
    @Transactional(readOnly = true)
    public List<Attendance> getUserAttendanceHistory(User user) {
        return attendanceRepository.findByUserOrderBySignInTimeDesc(user);
    }

    /**
     * Get attendance records for a user on a specific date
     * @param user The user
     * @param date The date
     * @return List of attendance records
     */
    @Override
    @Transactional(readOnly = true)
    public List<Attendance> getUserAttendanceByDate(User user, LocalDate date) {
        return attendanceRepository.findByUserAndAttendanceDate(user, date);
    }

    /**
     * Get all attendance records for a specific date
     * @param date The date
     * @return List of attendance records
     */
    @Override
    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByAttendanceDateOrderBySignInTimeDesc(date);
    }

    /**
     * Get all attendance records between two dates
     * @param startDate Start date
     * @param endDate End date
     * @return List of attendance records
     */
    @Override
    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByDateRange(startDate, endDate);
    }

    /**
     * Get attendance records for a user between two dates
     * @param user The user
     * @param startDate Start date
     * @param endDate End date
     * @return List of attendance records
     */
    @Override
    @Transactional(readOnly = true)
    public List<Attendance> getUserAttendanceByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByUserAndDateRange(user, startDate, endDate);
    }

    /**
     * Get all currently signed in users
     * @return List of active attendance records
     */
    @Override
    @Transactional(readOnly = true)
    public List<Attendance> getCurrentlySignedInUsers() {
        return attendanceRepository.findByStatusOrderBySignInTimeDesc(AttendanceStatus.SIGNED_IN);
    }

    /**
     * Get attendance record by ID
     * @param id The attendance ID
     * @return Optional containing the attendance record
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Attendance> getAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }

    /**
     * Get total attendance count for a user
     * @param user The user
     * @return Count of attendance records
     */
    @Override
    @Transactional(readOnly = true)
    public long getUserAttendanceCount(User user) {
        return attendanceRepository.countByUser(user);
    }

    /**
     * Check if user has signed in today
     * @param user The user
     * @return true if user has signed in today
     */
    @Override
    @Transactional(readOnly = true)
    public boolean hasSignedInToday(User user) {
        return attendanceRepository.existsByUserAndAttendanceDate(user, LocalDate.now());
    }
    
    // ========== NEW ENHANCED METHODS ==========

    @Override
    public Attendance signInWithQRCode(String qrCodeData) {
        Long userId = qrCodeService.validateQRCode(qrCodeData);
        if (userId == null) {
            throw new IllegalStateException("Invalid QR code");
        }
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalStateException("User not found"));
        
        Attendance attendance = signIn(user);
        attendance.setQrCodeUsed(true);
        return attendanceRepository.save(attendance);
    }

    @Override
    public Attendance signOutWithQRCode(String qrCodeData) {
        Long userId = qrCodeService.validateQRCode(qrCodeData);
        if (userId == null) {
            throw new IllegalStateException("Invalid QR code");
        }
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalStateException("User not found"));
        
        return signOut(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceStatistics getUserStatistics(User user) {
        AttendanceStatistics stats = new AttendanceStatistics(user.getId(), user.getFullName());
        
        // Calculate week boundaries (Monday to Sunday)
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        
        // Calculate month boundaries
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate monthEnd = today.with(TemporalAdjusters.lastDayOfMonth());
        
        stats.setWeekStartDate(weekStart);
        stats.setWeekEndDate(weekEnd);
        stats.setMonthStartDate(monthStart);
        stats.setMonthEndDate(monthEnd);
        
        // Weekly statistics
        List<Attendance> weekAttendance = getUserAttendanceByDateRange(user, weekStart, weekEnd);
        stats.setDaysAttendedThisWeek(weekAttendance.size());
        stats.setTotalHoursThisWeek(calculateTotalHours(weekAttendance));
        stats.setLateArrivalsThisWeek((int) weekAttendance.stream().filter(Attendance::isLate).count());
        stats.setEarlyDeparturesThisWeek((int) weekAttendance.stream().filter(Attendance::isEarlyDeparture).count());
        
        // Monthly statistics
        List<Attendance> monthAttendance = getUserAttendanceByDateRange(user, monthStart, monthEnd);
        stats.setDaysAttendedThisMonth(monthAttendance.size());
        stats.setTotalHoursThisMonth(calculateTotalHours(monthAttendance));
        stats.setLateArrivalsThisMonth((int) monthAttendance.stream().filter(Attendance::isLate).count());
        stats.setEarlyDeparturesThisMonth((int) monthAttendance.stream().filter(Attendance::isEarlyDeparture).count());
        stats.setOvertimeHoursThisMonth(calculateOvertimeHours(monthAttendance));
        
        // Attendance rates
        int workDaysInWeek = calculateWorkDays(weekStart, weekEnd);
        int workDaysInMonth = calculateWorkDays(monthStart, monthEnd);
        stats.setAttendanceRateWeek(workDaysInWeek > 0 ? (stats.getDaysAttendedThisWeek() * 100.0 / workDaysInWeek) : 0);
        stats.setAttendanceRateMonth(workDaysInMonth > 0 ? (stats.getDaysAttendedThisMonth() * 100.0 / workDaysInMonth) : 0);
        
        // Current status
        stats.setCurrentlySignedIn(isUserSignedIn(user));
        if (stats.isCurrentlySignedIn()) {
            getCurrentAttendance(user).ifPresent(att -> 
                stats.setCurrentSignInTime(att.getSignInTime().format(
                    java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")))
            );
        }
        
        // Average times
        if (!monthAttendance.isEmpty()) {
            stats.setAverageDailyHours(stats.getTotalHoursThisMonth() / stats.getDaysAttendedThisMonth());
        }
        
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<LocalDate, String> getMonthlyCalendar(User user, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        
        List<Attendance> attendanceList = getUserAttendanceByDateRange(user, startDate, endDate);
        
        Map<LocalDate, String> calendar = new HashMap<>();
        for (Attendance attendance : attendanceList) {
            String status;
            if (attendance.isLate()) {
                status = "LATE";
            } else if (attendance.getStatus() == AttendanceStatus.ABSENT) {
                status = "ABSENT";
            } else if (attendance.getStatus() == AttendanceStatus.ON_LEAVE) {
                status = "ON_LEAVE";
            } else {
                status = "ON_TIME";
            }
            calendar.put(attendance.getAttendanceDate(), status);
        }
        
        return calendar;
    }

    @Override
    @Transactional(readOnly = true)
    public long getLateArrivalsCount(User user, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.countLateArrivalsByUserAndDateRange(user, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public long getEarlyDeparturesCount(User user, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.countEarlyDeparturesByUserAndDateRange(user, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public double getTotalHoursWorked(User user, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendanceList = getUserAttendanceByDateRange(user, startDate, endDate);
        return calculateTotalHours(attendanceList);
    }

    @Override
    @Transactional(readOnly = true)
    public double getOvertimeHours(User user, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendanceList = getUserAttendanceByDateRange(user, startDate, endDate);
        return calculateOvertimeHours(attendanceList);
    }

    @Override
    @Transactional(readOnly = true)
    public double getAttendanceRate(User user, LocalDate startDate, LocalDate endDate) {
        long daysAttended = getUserAttendanceByDateRange(user, startDate, endDate).size();
        int workDays = calculateWorkDays(startDate, endDate);
        return workDays > 0 ? (daysAttended * 100.0 / workDays) : 0;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportToExcel(List<Attendance> attendanceList, String title) throws IOException {
        return excelExportService.exportAttendanceToExcel(attendanceList, title);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportUserAttendanceToExcel(User user) throws IOException {
        List<Attendance> attendanceList = getUserAttendanceHistory(user);
        return excelExportService.exportUserAttendanceSummary(user, attendanceList);
    }

    // ========== HELPER METHODS ==========

    private double calculateTotalHours(List<Attendance> attendanceList) {
        return attendanceList.stream()
            .filter(a -> a.getDuration() != null)
            .mapToDouble(a -> a.getDuration().toMinutes() / 60.0)
            .sum();
    }

    private double calculateOvertimeHours(List<Attendance> attendanceList) {
        return attendanceList.stream()
            .filter(a -> a.getOvertimeMinutes() != null && a.getOvertimeMinutes() > 0)
            .mapToDouble(a -> a.getOvertimeMinutes() / 60.0)
            .sum();
    }

    private int calculateWorkDays(LocalDate startDate, LocalDate endDate) {
        int workDays = 0;
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            DayOfWeek dayOfWeek = current.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                workDays++;
            }
            current = current.plusDays(1);
        }
        return workDays;
    }
}