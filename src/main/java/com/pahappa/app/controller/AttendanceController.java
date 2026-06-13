package com.pahappa.app.controller;

import com.pahappa.app.entity.Attendance;
import com.pahappa.app.entity.RoomQRSession;
import com.pahappa.app.entity.User;
import com.pahappa.app.entity.WorkSchedule;
import com.pahappa.app.repository.UserRepository;
import com.pahappa.app.service.AttendanceService;
import com.pahappa.app.service.RoomQRService;
import com.pahappa.app.service.WorkScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Controller for handling attendance operations.
 * 
 * This controller manages student attendance including sign in, sign out,
 * viewing attendance history, and admin views.
 * 
 * @author Pahappa
 * @version 1.0
 */
@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final UserRepository userRepository;
    private final com.pahappa.app.service.QRCodeService qrCodeService;
    private final WorkScheduleService workScheduleService;
    private final com.pahappa.app.service.UserService userService;
    private final RoomQRService roomQRService;

    @Autowired
    public AttendanceController(
            AttendanceService attendanceService,
            UserRepository userRepository,
            com.pahappa.app.service.QRCodeService qrCodeService,
            WorkScheduleService workScheduleService,
            com.pahappa.app.service.UserService userService,
            RoomQRService roomQRService) {
        this.attendanceService = attendanceService;
        this.userRepository = userRepository;
        this.qrCodeService = qrCodeService;
        this.workScheduleService = workScheduleService;
        this.userService = userService;
        this.roomQRService = roomQRService;
    }

    /**
     * Redirect /attendance to dashboard
     */
    @GetMapping
    public String attendancePage() {
        return "redirect:/attendance/dashboard";
    }

    /**
     * Handle sign in request
     */
    @PostMapping("/sign-in")
    public String signIn(RedirectAttributes redirectAttributes) {
        try {
            User currentUser = getCurrentUser();
            Attendance attendance = attendanceService.signIn(currentUser);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Successfully signed in at " + attendance.getSignInTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while signing in. Please try again.");
        }
        
        return "redirect:/attendance";
    }

    /**
     * Handle sign out request
     */
    @PostMapping("/sign-out")
    public String signOut(RedirectAttributes redirectAttributes) {
        try {
            User currentUser = getCurrentUser();
            Attendance attendance = attendanceService.signOut(currentUser);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Successfully signed out at " + attendance.getSignOutTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) +
                ". Duration: " + attendance.getFormattedDuration());
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while signing out. Please try again.");
        }
        
        return "redirect:/attendance";
    }

    /**
     * Display attendance history for current user
     */
    @GetMapping("/history")
    public String attendanceHistory(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model) {
        
        User currentUser = getCurrentUser();
        List<Attendance> attendanceList;
        
        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            attendanceList = attendanceService.getUserAttendanceByDateRange(currentUser, start, end);
        } else {
            attendanceList = attendanceService.getUserAttendanceHistory(currentUser);
        }
        
        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("user", currentUser);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        return "attendance-history";
    }

    /**
     * Admin view - Display all attendance records
     */
    @GetMapping("/admin")
    public String adminAttendanceView(
            @RequestParam(required = false) String date,
            Model model) {
        
        List<Attendance> attendanceList;
        
        if (date != null && !date.isEmpty()) {
            LocalDate selectedDate = LocalDate.parse(date);
            attendanceList = attendanceService.getAttendanceByDate(selectedDate);
            model.addAttribute("selectedDate", date);
        } else {
            // Show today's attendance by default
            attendanceList = attendanceService.getAttendanceByDate(LocalDate.now());
            model.addAttribute("selectedDate", LocalDate.now().toString());
        }
        
        // Get currently signed in users
        List<Attendance> currentlySignedIn = attendanceService.getCurrentlySignedInUsers();
        
        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("currentlySignedIn", currentlySignedIn);
        model.addAttribute("totalToday", attendanceList.size());
        model.addAttribute("currentlySignedInCount", currentlySignedIn.size());
        
        return "attendance-admin";
    }

    /**
     * Admin view - Display all attendance records with date range
     */
    @GetMapping("/admin/report")
    public String adminAttendanceReport(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model) {
        
        List<Attendance> attendanceList;
        
        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            attendanceList = attendanceService.getAttendanceByDateRange(start, end);
        } else {
            // Show last 30 days by default
            LocalDate end = LocalDate.now();
            LocalDate start = end.minusDays(30);
            attendanceList = attendanceService.getAttendanceByDateRange(start, end);
            startDate = start.toString();
            endDate = end.toString();
        }
        
        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("totalRecords", attendanceList.size());
        
        return "attendance-report";
    }
    
    // ========== NEW ENHANCED ENDPOINTS ==========

    /**
     * Display dashboard with statistics
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User currentUser = getCurrentUser();
        
        // Get user statistics
        com.pahappa.app.dto.AttendanceStatistics stats = attendanceService.getUserStatistics(currentUser);
        
        model.addAttribute("stats", stats);
        model.addAttribute("user", currentUser);
        
        return "attendance-dashboard";
    }

    /**
     * Display calendar view
     */
    @GetMapping("/calendar")
    public String calendar(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            Model model) {
        
        User currentUser = getCurrentUser();
        
        // Default to current month if not specified
        LocalDate now = LocalDate.now();
        int selectedYear = (year != null) ? year : now.getYear();
        int selectedMonth = (month != null) ? month : now.getMonthValue();
        
        // Get calendar data
        java.util.Map<LocalDate, String> calendarData = 
            attendanceService.getMonthlyCalendar(currentUser, selectedYear, selectedMonth);
        
        model.addAttribute("calendarData", calendarData);
        model.addAttribute("year", selectedYear);
        model.addAttribute("month", selectedMonth);
        model.addAttribute("user", currentUser);
        
        return "attendance-calendar";
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ROOM QR CODE — Admin generates, interns scan to prove physical presence
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Admin page: show the active CHECK_IN and CHECK_OUT QR codes side by side.
     */
    @GetMapping("/admin/room-qr")
    public String adminRoomQR(Model model) {
        roomQRService.getActiveCheckInSession().ifPresent(s -> {
            model.addAttribute("checkInSession", s);
            try { model.addAttribute("checkInQRBase64", roomQRService.renderQRCodeBase64(s)); }
            catch (Exception e) { model.addAttribute("qrError", e.getMessage()); }
        });
        roomQRService.getActiveCheckOutSession().ifPresent(s -> {
            model.addAttribute("checkOutSession", s);
            try { model.addAttribute("checkOutQRBase64", roomQRService.renderQRCodeBase64(s)); }
            catch (Exception e) { model.addAttribute("qrError", e.getMessage()); }
        });
        return "attendance-room-qr";
    }

    /**
     * Admin action: generate a fresh CHECK_IN + CHECK_OUT pair.
     */
    @PostMapping("/admin/room-qr/generate")
    public String generateRoomQR(
            @RequestParam(required = false, defaultValue = "Office Session") String label,
            RedirectAttributes redirectAttributes) {
        try {
            roomQRService.generateNewSession(label);
            redirectAttributes.addFlashAttribute("roomQRSuccess",
                    "Check-In and Check-Out QR codes generated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("roomQRError", "Failed to generate: " + e.getMessage());
        }
        return "redirect:/attendance/admin/room-qr";
    }

    /**
     * Admin action: revoke all active room QR codes immediately.
     */
    @PostMapping("/admin/room-qr/revoke")
    public String revokeRoomQR(RedirectAttributes redirectAttributes) {
        roomQRService.invalidateCurrentSession();
        redirectAttributes.addFlashAttribute("roomQRSuccess", "All room QR codes revoked.");
        return "redirect:/attendance/admin/room-qr";
    }

    /**
     * Intern: unified scan endpoint.
     * The server determines whether the scanned token is CHECK_IN or CHECK_OUT
     * and performs the correct action.  Check-out is rejected if the intern
     * has not checked in today.
     */
    @PostMapping("/qr/room-scan")
    @ResponseBody
    public java.util.Map<String, Object> roomQRScan(
            @RequestBody java.util.Map<String, String> payload) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            String token = payload.get("token");
            if (token == null || token.isBlank()) {
                response.put("success", false);
                response.put("message", "No token provided.");
                return response;
            }

            // Determine type — empty means invalid/expired
            java.util.Optional<com.pahappa.app.entity.RoomQRSession.SessionType> typeOpt =
                    roomQRService.validateToken(token);

            if (typeOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "QR code is invalid or has expired. Ask the admin to refresh it.");
                return response;
            }

            User currentUser = getCurrentUser();
            com.pahappa.app.entity.RoomQRSession.SessionType type = typeOpt.get();

            if (type == com.pahappa.app.entity.RoomQRSession.SessionType.CHECK_IN) {
                Attendance attendance = attendanceService.signIn(currentUser);
                response.put("success", true);
                response.put("action", "CHECK_IN");
                response.put("message", "Checked in at "
                        + attendance.getSignInTime().toLocalTime().toString().substring(0, 5));

            } else {
                // CHECK_OUT — reject if intern hasn't checked in today
                if (!attendanceService.isUserSignedIn(currentUser)) {
                    response.put("success", false);
                    response.put("message", "You have not checked in yet today. Please scan the Check-In QR first.");
                    return response;
                }
                Attendance attendance = attendanceService.signOut(currentUser);
                response.put("success", true);
                response.put("action", "CHECK_OUT");
                response.put("message", "Checked out. Duration: " + attendance.getFormattedDuration());
            }

        } catch (IllegalStateException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Unexpected error: " + e.getMessage());
        }
        return response;
    }

    // Keep old endpoints as aliases for backward compatibility
    @PostMapping("/qr/room-sign-in")
    @ResponseBody
    public java.util.Map<String, Object> roomQRSignIn(@RequestBody java.util.Map<String, String> payload) {
        return roomQRScan(payload);
    }

    @PostMapping("/qr/room-sign-out")
    @ResponseBody
    public java.util.Map<String, Object> roomQRSignOut(@RequestBody java.util.Map<String, String> payload) {
        return roomQRScan(payload);
    }

    /**
     * QR Code scanner page
     */
    @GetMapping("/qr-scanner")
    public String qrScanner(Model model) {
        User currentUser = getCurrentUser();
        model.addAttribute("user", currentUser);
        return "attendance-qr-scanner";
    }

    /**
     * Sign in with QR code
     */
    @PostMapping("/qr/sign-in")
    @ResponseBody
    public java.util.Map<String, Object> signInWithQR(@RequestBody java.util.Map<String, String> payload) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            String qrCodeData = payload.get("qrCode");
            Attendance attendance = attendanceService.signInWithQRCode(qrCodeData);
            
            response.put("success", true);
            response.put("message", "Successfully signed in at " + 
                attendance.getSignInTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            response.put("isLate", attendance.isLate());
            if (attendance.isLate()) {
                response.put("lateMessage", "You are " + attendance.getFormattedLateTime());
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    /**
     * Sign out with QR code
     */
    @PostMapping("/qr/sign-out")
    @ResponseBody
    public java.util.Map<String, Object> signOutWithQR(@RequestBody java.util.Map<String, String> payload) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            String qrCodeData = payload.get("qrCode");
            Attendance attendance = attendanceService.signOutWithQRCode(qrCodeData);
            
            response.put("success", true);
            response.put("message", "Successfully signed out. Duration: " + attendance.getFormattedDuration());
            response.put("duration", attendance.getFormattedDuration());
            if (attendance.getOvertimeMinutes() != null && attendance.getOvertimeMinutes() > 0) {
                response.put("overtime", attendance.getFormattedOvertime());
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    /**
     * Export user attendance to Excel
     */
    @GetMapping("/export")
    public void exportAttendance(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            jakarta.servlet.http.HttpServletResponse response) {
        
        try {
            User currentUser = getCurrentUser();
            byte[] excelData;
            
            if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
                LocalDate start = LocalDate.parse(startDate);
                LocalDate end = LocalDate.parse(endDate);
                List<Attendance> attendanceList = attendanceService.getUserAttendanceByDateRange(currentUser, start, end);
                excelData = attendanceService.exportToExcel(attendanceList, 
                    "Attendance Report - " + currentUser.getFullName());
            } else {
                excelData = attendanceService.exportUserAttendanceToExcel(currentUser);
            }
            
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", 
                "attachment; filename=attendance_" + currentUser.getUsername() + "_" + 
                LocalDate.now() + ".xlsx");
            response.getOutputStream().write(excelData);
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException("Error exporting attendance data", e);
        }
    }

    /**
     * Admin - Export all attendance to Excel
     */
    @GetMapping("/admin/export")
    public void adminExportAttendance(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            jakarta.servlet.http.HttpServletResponse response) {
        
        try {
            List<Attendance> attendanceList;
            String title;
            
            if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
                LocalDate start = LocalDate.parse(startDate);
                LocalDate end = LocalDate.parse(endDate);
                attendanceList = attendanceService.getAttendanceByDateRange(start, end);
                title = "Attendance Report (" + startDate + " to " + endDate + ")";
            } else {
                LocalDate end = LocalDate.now();
                LocalDate start = end.minusDays(30);
                attendanceList = attendanceService.getAttendanceByDateRange(start, end);
                title = "Attendance Report - Last 30 Days";
            }
            
            byte[] excelData = attendanceService.exportToExcel(attendanceList, title);
            
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", 
                "attachment; filename=attendance_report_" + LocalDate.now() + ".xlsx");
            response.getOutputStream().write(excelData);
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException("Error exporting attendance data", e);
        }
    }

    /**
     * Admin - Work schedule settings page (GET)
     * Loads the active schedule and pre-populates the form.
     */
    @GetMapping("/admin/settings")
    public String adminSettings(Model model) {
        WorkSchedule schedule = workScheduleService.getActiveSchedule();
        model.addAttribute("schedule", schedule);
        model.addAttribute("interns", userService.findAllInterns());
        model.addAttribute("admins", userService.findAllAdmins());
        // Current admin username for the change-password form
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentAdminUsername", auth.getName());
        return "attendance-admin-settings";
    }

    /**
     * Admin - Change own password (POST)
     */
    @PostMapping("/admin/change-password")
    public String changeAdminPassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("pwError", "New passwords do not match.");
            return "redirect:/attendance/admin/settings";
        }
        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("pwError", "New password must be at least 6 characters.");
            return "redirect:/attendance/admin/settings";
        }

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            userService.changePassword(auth.getName(), currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("pwSuccess", "Password changed successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("pwError", e.getMessage());
        }

        return "redirect:/attendance/admin/settings";
    }

    /**
     * Admin - Promote an intern to ADMIN (POST)
     */
    @PostMapping("/admin/promote/{userId}")
    public String promoteUser(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        try {
            userService.promoteToAdmin(userId);
            redirectAttributes.addFlashAttribute("roleSuccess", "User promoted to Admin successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("roleError", e.getMessage());
        }
        return "redirect:/attendance/admin/settings";
    }

    /**
     * Admin - Demote an admin back to INTERN (POST)
     */
    @PostMapping("/admin/demote/{userId}")
    public String demoteUser(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        try {
            // Prevent an admin from demoting themselves
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            com.pahappa.app.entity.User target = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (target.getUsername().equals(auth.getName())) {
                redirectAttributes.addFlashAttribute("roleError", "You cannot demote yourself.");
                return "redirect:/attendance/admin/settings";
            }
            userService.demoteToIntern(userId);
            redirectAttributes.addFlashAttribute("roleSuccess", "Admin demoted to Intern.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("roleError", e.getMessage());
        }
        return "redirect:/attendance/admin/settings";
    }

    /**
     * Admin - Save work schedule settings (POST)
     * Persists updated schedule values submitted from the settings form.
     */
    @PostMapping("/admin/settings")
    public String saveAdminSettings(
            @RequestParam(required = false) Long scheduleId,
            @RequestParam String workStartTime,
            @RequestParam String workEndTime,
            @RequestParam Integer gracePeriod,
            @RequestParam Double minimumHours,
            @RequestParam(required = false) List<String> workDays,
            @RequestParam(defaultValue = "false") boolean notifyLate,
            @RequestParam(defaultValue = "false") boolean notifyEarly,
            RedirectAttributes redirectAttributes) {

        try {
            // Load existing or start fresh
            WorkSchedule schedule = (scheduleId != null)
                    ? workScheduleService.getActiveSchedule()
                    : new WorkSchedule();

            schedule.setName("Default Schedule");
            schedule.setWorkStartTime(LocalTime.parse(workStartTime));
            schedule.setWorkEndTime(LocalTime.parse(workEndTime));
            schedule.setGracePeriodMinutes(gracePeriod);
            schedule.setMinimumWorkHours(minimumHours);
            schedule.setNotifyLateArrival(notifyLate);
            schedule.setNotifyEarlyDeparture(notifyEarly);

            // Build comma-separated days string (default to Mon-Fri if none ticked)
            String days = (workDays != null && !workDays.isEmpty())
                    ? String.join(",", workDays)
                    : "MON,TUE,WED,THU,FRI";
            schedule.setApplicableDays(days);

            workScheduleService.saveSchedule(schedule);
            redirectAttributes.addFlashAttribute("successMessage", "Settings saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save settings: " + e.getMessage());
        }

        return "redirect:/attendance/admin/settings";
    }

    /**
     * My QR Code page — redirects to scanner since personal QR generation
     * has been removed in favour of the admin room QR system.
     */
    @GetMapping("/my-qr-code")
    public String myQRCode() {
        return "redirect:/attendance/qr-scanner";
    }

    /**
     * Personal QR code image generation endpoint — disabled.
     * Returns 410 Gone so any cached links fail gracefully.
     */
    @GetMapping("/qr-code/generate")
    public void generateQRCode(jakarta.servlet.http.HttpServletResponse response) {
        response.setStatus(410); // Gone — this feature has been removed
    }

    /**
     * Get current authenticated user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}