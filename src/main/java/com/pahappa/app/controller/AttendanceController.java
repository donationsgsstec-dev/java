package com.pahappa.app.controller;

import com.pahappa.app.entity.Attendance;
import com.pahappa.app.entity.User;
import com.pahappa.app.repository.UserRepository;
import com.pahappa.app.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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

    /**
     * Constructor-based dependency injection
     */
    @Autowired
    public AttendanceController(AttendanceService attendanceService, UserRepository userRepository) {
        this.attendanceService = attendanceService;
        this.userRepository = userRepository;
    }

    /**
     * Display attendance page for students
     */
    @GetMapping
    public String attendancePage(Model model) {
        User currentUser = getCurrentUser();
        
        // Check if user is currently signed in
        boolean isSignedIn = attendanceService.isUserSignedIn(currentUser);
        Optional<Attendance> currentAttendance = attendanceService.getCurrentAttendance(currentUser);
        
        // Get user's attendance history (last 10 records)
        List<Attendance> attendanceHistory = attendanceService.getUserAttendanceHistory(currentUser);
        
        // Get attendance count
        long totalAttendance = attendanceService.getUserAttendanceCount(currentUser);
        
        model.addAttribute("isSignedIn", isSignedIn);
        model.addAttribute("currentAttendance", currentAttendance.orElse(null));
        model.addAttribute("attendanceHistory", attendanceHistory);
        model.addAttribute("totalAttendance", totalAttendance);
        model.addAttribute("user", currentUser);
        
        return "attendance";
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

// Made with Bob