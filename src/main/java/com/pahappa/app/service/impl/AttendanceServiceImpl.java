package com.pahappa.app.service.impl;

import com.pahappa.app.entity.Attendance;
import com.pahappa.app.entity.Attendance.AttendanceStatus;
import com.pahappa.app.entity.User;
import com.pahappa.app.repository.AttendanceRepository;
import com.pahappa.app.service.AttendanceService;
import com.pahappa.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private final EmailService emailService;

    /**
     * Constructor-based dependency injection
     * @param attendanceRepository The attendance repository
     * @param emailService The email service for notifications
     */
    @Autowired
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, EmailService emailService) {
        this.attendanceRepository = attendanceRepository;
        this.emailService = emailService;
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

        // Create new attendance record
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setSignInTime(LocalDateTime.now());
        attendance.setAttendanceDate(LocalDate.now());
        attendance.setStatus(AttendanceStatus.SIGNED_IN);

        Attendance savedAttendance = attendanceRepository.save(attendance);
        
        // Send email notification
        try {
            String signInTime = savedAttendance.getSignInTime()
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
            emailService.sendAttendanceSignInNotification(
                user.getEmail(),
                user.getUsername(),
                signInTime
            );
        } catch (Exception e) {
            // Log error but don't fail the sign-in process
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
            throw new IllegalStateException("User is not signed in. Please sign in first.");
        }

        // Update attendance record with sign out time
        Attendance attendance = attendanceOpt.get();
        attendance.setSignOutTime(LocalDateTime.now());
        attendance.setStatus(AttendanceStatus.SIGNED_OUT);

        Attendance savedAttendance = attendanceRepository.save(attendance);
        
        // Send email notification
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
        } catch (Exception e) {
            // Log error but don't fail the sign-out process
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
}

// Made with Bob