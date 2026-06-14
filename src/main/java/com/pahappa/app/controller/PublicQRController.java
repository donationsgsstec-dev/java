package com.pahappa.app.controller;

import com.pahappa.app.entity.RoomQRSession.SessionType;
import com.pahappa.app.entity.User;
import com.pahappa.app.repository.UserRepository;
import com.pahappa.app.service.AttendanceService;
import com.pahappa.app.service.EmailService;
import com.pahappa.app.service.QRCodeService;
import com.pahappa.app.service.RoomQRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Public controller for handling QR code scans from external QR scanner apps.
 * 
 * This controller provides public endpoints that don't require authentication,
 * allowing users to scan QR codes with any QR scanner app and automatically
 * check in/out with email notifications.
 * 
 * @author Pahappa
 * @version 1.0
 */
@Controller
@RequestMapping("/attendance/qr")
public class PublicQRController {

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private RoomQRService roomQRService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Handle personal QR code scan from external scanner.
     * 
     * When a user scans their personal QR code with any QR scanner app,
     * this endpoint validates the token and performs check-in/out automatically.
     * 
     * @param token Encrypted QR code token
     * @param model Spring MVC Model
     * @return View name with success/error message
     */
    @GetMapping("/scan")
    public String handlePersonalQRScan(@RequestParam("token") String token, Model model) {
        try {
            // Validate QR code and get user ID
            Long userId = qrCodeService.validateQRCode(token);
            
            if (userId == null) {
                model.addAttribute("error", "Invalid or expired QR code");
                model.addAttribute("message", "This QR code is invalid, expired, or has already been used. Please generate a new one.");
                return "qr-scan-result";
            }
            
            // Get user
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                model.addAttribute("error", "User not found");
                model.addAttribute("message", "The user associated with this QR code could not be found.");
                return "qr-scan-result";
            }
            
            User user = userOpt.get();
            
            // Check if user is already signed in
            boolean isSignedIn = attendanceService.isUserSignedIn(user);
            
            if (isSignedIn) {
                // Sign out
                attendanceService.signOut(user);
                
                // Send sign-out notification email
                try {
                    String signOutTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    emailService.sendAttendanceSignOutNotification(
                        user.getEmail(),
                        user.getUsername(),
                        "N/A", // Sign-in time will be fetched from attendance record
                        signOutTime,
                        "N/A" // Duration will be calculated
                    );
                } catch (Exception e) {
                    System.err.println("Failed to send sign-out notification: " + e.getMessage());
                }
                
                model.addAttribute("success", true);
                model.addAttribute("action", "CHECK OUT");
                model.addAttribute("username", user.getUsername());
                model.addAttribute("message", "You have successfully checked out! A confirmation email has been sent.");
            } else {
                // Sign in
                attendanceService.signIn(user);
                
                // Send sign-in notification email
                try {
                    String signInTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    emailService.sendAttendanceSignInNotification(
                        user.getEmail(),
                        user.getUsername(),
                        signInTime
                    );
                } catch (Exception e) {
                    System.err.println("Failed to send sign-in notification: " + e.getMessage());
                }
                
                model.addAttribute("success", true);
                model.addAttribute("action", "CHECK IN");
                model.addAttribute("username", user.getUsername());
                model.addAttribute("message", "You have successfully checked in! A confirmation email has been sent.");
            }
            
            return "qr-scan-result";
            
        } catch (Exception e) {
            model.addAttribute("error", "Scan failed");
            model.addAttribute("message", "An error occurred while processing your QR code: " + e.getMessage());
            return "qr-scan-result";
        }
    }

    /**
     * Handle room QR code scan from external scanner.
     * 
     * When a user scans the admin-generated room QR code with any QR scanner app,
     * this endpoint validates the token and performs check-in or check-out based
     * on the session type.
     * 
     * @param token Room QR session token
     * @param type Session type (CHECK_IN or CHECK_OUT)
     * @param username User's username (required for room QR codes)
     * @param model Spring MVC Model
     * @return View name with success/error message
     */
    @GetMapping("/room-scan")
    public String handleRoomQRScan(@RequestParam("token") String token,
                                   @RequestParam("type") String type,
                                   @RequestParam(value = "username", required = false) String username,
                                   Model model) {
        try {
            // If username is not provided, show a form to enter it
            if (username == null || username.trim().isEmpty()) {
                model.addAttribute("token", token);
                model.addAttribute("type", type);
                model.addAttribute("sessionType", type.equals("CHECK_IN") ? "Check In" : "Check Out");
                return "qr-username-input";
            }
            
            // Validate token
            Optional<SessionType> sessionTypeOpt = roomQRService.validateToken(token);
            if (sessionTypeOpt.isEmpty()) {
                model.addAttribute("error", "Invalid or expired QR code");
                model.addAttribute("message", "This room QR code is invalid, expired, or has already been used.");
                return "qr-scan-result";
            }
            
            // Get user
            Optional<User> userOpt = userRepository.findByUsername(username.trim());
            if (userOpt.isEmpty()) {
                model.addAttribute("error", "User not found");
                model.addAttribute("message", "No user found with username: " + username);
                model.addAttribute("token", token);
                model.addAttribute("type", type);
                return "qr-username-input";
            }
            
            User user = userOpt.get();
            SessionType sessionType = sessionTypeOpt.get();
            
            // Perform check-in or check-out based on session type
            if (sessionType == SessionType.CHECK_IN) {
                attendanceService.signIn(user);
                
                // Send sign-in notification
                try {
                    String signInTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    emailService.sendAttendanceSignInNotification(
                        user.getEmail(),
                        user.getUsername(),
                        signInTime
                    );
                } catch (Exception e) {
                    System.err.println("Failed to send sign-in notification: " + e.getMessage());
                }
                
                model.addAttribute("success", true);
                model.addAttribute("action", "CHECK IN");
                model.addAttribute("username", user.getUsername());
                model.addAttribute("message", "You have successfully checked in using the room QR code! A confirmation email has been sent.");
            } else {
                attendanceService.signOut(user);
                
                // Send sign-out notification
                try {
                    String signOutTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    emailService.sendAttendanceSignOutNotification(
                        user.getEmail(),
                        user.getUsername(),
                        "N/A",
                        signOutTime,
                        "N/A"
                    );
                } catch (Exception e) {
                    System.err.println("Failed to send sign-out notification: " + e.getMessage());
                }
                
                model.addAttribute("success", true);
                model.addAttribute("action", "CHECK OUT");
                model.addAttribute("username", user.getUsername());
                model.addAttribute("message", "You have successfully checked out using the room QR code! A confirmation email has been sent.");
            }
            
            return "qr-scan-result";
            
        } catch (Exception e) {
            model.addAttribute("error", "Scan failed");
            model.addAttribute("message", "An error occurred while processing the room QR code: " + e.getMessage());
            return "qr-scan-result";
        }
    }
}

// Made with Bob