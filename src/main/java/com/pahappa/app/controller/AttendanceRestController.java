package com.pahappa.app.controller;

import com.pahappa.app.dto.AttendanceStatistics;
import com.pahappa.app.entity.Attendance;
import com.pahappa.app.entity.User;
import com.pahappa.app.repository.AttendanceRepository;
import com.pahappa.app.repository.UserRepository;
import com.pahappa.app.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST Controller for real-time attendance data.
 * 
 * Provides JSON endpoints for:
 * - Weekly attendance overview (real-time)
 * - Current attendance status
 * - Attendance statistics
 * 
 * @author Pahappa
 * @version 1.0
 */
@RestController
@RequestMapping("/api/attendance")
public class AttendanceRestController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get real-time weekly attendance overview
     * @return Weekly attendance data
     */
    @GetMapping("/weekly-overview")
    public ResponseEntity<Map<String, Object>> getWeeklyOverview() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            User user = userOpt.get();
            
            // Get current week's date range (Monday to Sunday)
            LocalDate today = LocalDate.now();
            LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            
            // Get attendance records for the week
            List<Attendance> weekAttendance = attendanceRepository.findByUserAndDateRange(user, monday, sunday);
            
            // Create day-by-day map
            Map<String, Map<String, Object>> weeklyData = new LinkedHashMap<>();
            
            for (LocalDate date = monday; !date.isAfter(sunday); date = date.plusDays(1)) {
                String dayKey = date.getDayOfWeek().toString();
                Map<String, Object> dayData = new HashMap<>();
                
                final LocalDate currentDate = date;
                List<Attendance> dayAttendances = weekAttendance.stream()
                    .filter(a -> a.getAttendanceDate().equals(currentDate))
                    .collect(Collectors.toList());
                
                if (!dayAttendances.isEmpty()) {
                    Attendance att = dayAttendances.get(0);
                    dayData.put("present", true);
                    dayData.put("signInTime", att.getSignInTime().toString());
                    dayData.put("signOutTime", att.getSignOutTime() != null ? att.getSignOutTime().toString() : null);
                    dayData.put("isLate", att.isLate());
                    dayData.put("lateMinutes", att.getLateMinutes());
                    dayData.put("status", att.getStatus().toString());
                    dayData.put("duration", att.getFormattedDuration());
                } else {
                    dayData.put("present", false);
                    dayData.put("status", "ABSENT");
                }
                
                dayData.put("date", currentDate.toString());
                weeklyData.put(dayKey, dayData);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("weeklyData", weeklyData);
            response.put("weekStart", monday.toString());
            response.put("weekEnd", sunday.toString());
            response.put("lastUpdated", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get current attendance status
     * @return Current status (signed in/out)
     */
    @GetMapping("/current-status")
    public ResponseEntity<Map<String, Object>> getCurrentStatus() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            User user = userOpt.get();
            boolean isSignedIn = attendanceService.isUserSignedIn(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("isSignedIn", isSignedIn);
            response.put("username", username);
            response.put("timestamp", System.currentTimeMillis());
            
            if (isSignedIn) {
                Optional<Attendance> currentAtt = attendanceService.getCurrentAttendance(user);
                currentAtt.ifPresent(attendance -> {
                    response.put("signInTime", attendance.getSignInTime().toString());
                    response.put("isLate", attendance.isLate());
                });
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get attendance statistics
     * @return Attendance statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<AttendanceStatistics> getStatistics() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            User user = userOpt.get();
            AttendanceStatistics stats = attendanceService.getUserStatistics(user);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get today's attendance
     * @return Today's attendance record
     */
    @GetMapping("/today")
    public ResponseEntity<Map<String, Object>> getTodayAttendance() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            User user = userOpt.get();
            LocalDate today = LocalDate.now();
            List<Attendance> todayAttendance = attendanceService.getUserAttendanceByDate(user, today);
            
            Map<String, Object> response = new HashMap<>();
            if (!todayAttendance.isEmpty()) {
                Attendance att = todayAttendance.get(0);
                response.put("present", true);
                response.put("signInTime", att.getSignInTime().toString());
                response.put("signOutTime", att.getSignOutTime() != null ? att.getSignOutTime().toString() : null);
                response.put("isLate", att.isLate());
                response.put("lateMinutes", att.getLateMinutes());
                response.put("status", att.getStatus().toString());
                response.put("duration", att.getFormattedDuration());
            } else {
                response.put("present", false);
                response.put("status", "NOT_SIGNED_IN");
            }
            
            response.put("date", today.toString());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

// Made with Bob