package com.pahappa.app.service.impl;

import com.pahappa.app.entity.User;
import com.pahappa.app.repository.UserRepository;
import com.pahappa.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of EmailService using QSSN Mail Services API.
 * 
 * This service sends email notifications through the QSSN API for:
 * - User registration (welcome email)
 * - User login (login notification)
 * - User logout (logout notification)
 * - Attendance sign-in/sign-out
 * 
 * Configuration is loaded from application.properties:
 * - qssn.email.api.key: API key for authentication
 * - qssn.email.api.url: QSSN API endpoint URL
 * - qssn.email.from.name: Sender name for emails
 * - qssn.email.enabled: Enable/disable email sending
 * 
 * @author Pahappa
 * @version 1.0
 */
@Service
public class EmailServiceImpl implements EmailService {
    
    @Value("${qssn.email.api.key}")
    private String apiKey;
    
    @Value("${qssn.email.api.url}")
    private String apiUrl;
    
    @Value("${qssn.email.from.name}")
    private String fromName;
    
    @Value("${qssn.email.enabled:true}")
    private boolean emailEnabled;
    
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    
    @Autowired
    public EmailServiceImpl(UserRepository userRepository) {
        this.restTemplate = new RestTemplate();
        this.userRepository = userRepository;
    }
    
    @Override
    public boolean sendWelcomeEmail(String toEmail, String username) {
        if (!emailEnabled) {
            System.out.println("Email sending is disabled. Skipping welcome email.");
            return false;
        }
        
        String subject = "Welcome to User Auth App!";
        String htmlContent = buildWelcomeEmailHtml(username);
        
        return sendEmail(toEmail, subject, htmlContent);
    }
    
    @Override
    public boolean sendLoginNotification(String toEmail, String username) {
        if (!emailEnabled) {
            System.out.println("Email sending is disabled. Skipping login notification.");
            return false;
        }
        
        String subject = "Login Notification - User Auth App";
        String htmlContent = buildLoginNotificationHtml(username);
        
        return sendEmail(toEmail, subject, htmlContent);
    }
    
    @Override
    public boolean sendLogoutNotification(String toEmail, String username) {
        if (!emailEnabled) {
            System.out.println("Email sending is disabled. Skipping logout notification.");
            return false;
        }
        
        String subject = "Logout Notification - User Auth App";
        String htmlContent = buildLogoutNotificationHtml(username);
        
        return sendEmail(toEmail, subject, htmlContent);
    }
    
    @Override
    public boolean sendAttendanceSignInNotification(String toEmail, String username, String signInTime) {
        if (!emailEnabled) {
            System.out.println("Email sending is disabled. Skipping attendance sign-in notification.");
            return false;
        }
        
        String subject = "Attendance Sign-In Confirmation";
        String htmlContent = buildAttendanceSignInHtml(username, signInTime);
        
        return sendEmail(toEmail, subject, htmlContent);
    }
    
    @Override
    public boolean sendAttendanceSignOutNotification(String toEmail, String username, String signInTime, String signOutTime, String duration) {
        if (!emailEnabled) {
            System.out.println("Email sending is disabled. Skipping attendance sign-out notification.");
            return false;
        }
        
        String subject = "Attendance Sign-Out Confirmation";
        String htmlContent = buildAttendanceSignOutHtml(username, signInTime, signOutTime, duration);
        
        return sendEmail(toEmail, subject, htmlContent);
    }
    
    private boolean sendEmail(String toEmail, String subject, String htmlContent) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("to", toEmail);
            requestBody.put("subject", subject);
            requestBody.put("html", htmlContent);
            requestBody.put("from_name", fromName);
            
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                request,
                String.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Email sent successfully to: " + toEmail);
                System.out.println("Response: " + response.getBody());
                return true;
            } else {
                System.err.println("Failed to send email. Status: " + response.getStatusCode());
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("Error sending email to " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private String buildWelcomeEmailHtml(String username) {
        return String.format(
            "<!DOCTYPE html>" +
            "<html><head><style>" +
            "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
            ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
            ".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }" +
            ".content { padding: 20px; background-color: #f9f9f9; }" +
            ".footer { text-align: center; padding: 10px; font-size: 12px; color: #666; }" +
            "</style></head><body>" +
            "<div class='container'>" +
            "<div class='header'><h1>Welcome to User Auth App!</h1></div>" +
            "<div class='content'>" +
            "<h2>Hello %s!</h2>" +
            "<p>Thank you for registering with User Auth App. Your account has been successfully created.</p>" +
            "<p>You can now log in and start using our application.</p>" +
            "<p><strong>Username:</strong> %s</p>" +
            "<p>If you didn't create this account, please contact our support team immediately.</p>" +
            "</div>" +
            "<div class='footer'><p>&copy; 2026 User Auth App. All rights reserved.</p></div>" +
            "</div></body></html>",
            username, username
        );
    }
    
    private String buildLoginNotificationHtml(String username) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format(
            "<!DOCTYPE html>" +
            "<html><head><style>" +
            "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
            ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
            ".header { background-color: #2196F3; color: white; padding: 20px; text-align: center; }" +
            ".content { padding: 20px; background-color: #f9f9f9; }" +
            ".info-box { background-color: #e3f2fd; padding: 15px; border-left: 4px solid #2196F3; margin: 10px 0; }" +
            ".footer { text-align: center; padding: 10px; font-size: 12px; color: #666; }" +
            "</style></head><body>" +
            "<div class='container'>" +
            "<div class='header'><h1>Login Notification</h1></div>" +
            "<div class='content'>" +
            "<h2>Hello %s!</h2>" +
            "<p>We detected a login to your account.</p>" +
            "<div class='info-box'>" +
            "<p><strong>Username:</strong> %s</p>" +
            "<p><strong>Time:</strong> %s</p>" +
            "</div>" +
            "<p>If this wasn't you, please secure your account immediately by changing your password.</p>" +
            "</div>" +
            "<div class='footer'><p>&copy; 2026 User Auth App. All rights reserved.</p></div>" +
            "</div></body></html>",
            username, username, timestamp
        );
    }
    
    private String buildLogoutNotificationHtml(String username) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format(
            "<!DOCTYPE html>" +
            "<html><head><style>" +
            "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
            ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
            ".header { background-color: #FF9800; color: white; padding: 20px; text-align: center; }" +
            ".content { padding: 20px; background-color: #f9f9f9; }" +
            ".info-box { background-color: #fff3e0; padding: 15px; border-left: 4px solid #FF9800; margin: 10px 0; }" +
            ".footer { text-align: center; padding: 10px; font-size: 12px; color: #666; }" +
            "</style></head><body>" +
            "<div class='container'>" +
            "<div class='header'><h1>Logout Notification</h1></div>" +
            "<div class='content'>" +
            "<h2>Goodbye %s!</h2>" +
            "<p>You have successfully logged out from your account.</p>" +
            "<div class='info-box'>" +
            "<p><strong>Username:</strong> %s</p>" +
            "<p><strong>Time:</strong> %s</p>" +
            "</div>" +
            "<p>Thank you for using User Auth App. We hope to see you again soon!</p>" +
            "<p>If you didn't perform this logout, please contact our support team immediately.</p>" +
            "</div>" +
            "<div class='footer'><p>&copy; 2026 User Auth App. All rights reserved.</p></div>" +
            "</div></body></html>",
            username, username, timestamp
        );
    }
    
    private String buildAttendanceSignInHtml(String username, String signInTime) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return String.format(
            "<!DOCTYPE html>" +
            "<html><head><style>" +
            "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
            ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
            ".header { background-color: #28a745; color: white; padding: 20px; text-align: center; }" +
            ".content { padding: 20px; background-color: #f9f9f9; }" +
            ".info-box { background-color: #d4edda; padding: 15px; border-left: 4px solid #28a745; margin: 10px 0; }" +
            ".footer { text-align: center; padding: 10px; font-size: 12px; color: #666; }" +
            "</style></head><body>" +
            "<div class='container'>" +
            "<div class='header'><h1>Attendance Sign-In Confirmed</h1></div>" +
            "<div class='content'>" +
            "<h2>Hello %s!</h2>" +
            "<p>You have successfully signed in for attendance.</p>" +
            "<div class='info-box'>" +
            "<p><strong>Date:</strong> %s</p>" +
            "<p><strong>Sign-In Time:</strong> %s</p>" +
            "</div>" +
            "<p>Please remember to sign out when you leave.</p>" +
            "<p>Have a productive day!</p>" +
            "</div>" +
            "<div class='footer'><p>&copy; 2026 Internship Attendance System. All rights reserved.</p></div>" +
            "</div></body></html>",
            username, date, signInTime
        );
    }
    
    private String buildAttendanceSignOutHtml(String username, String signInTime, String signOutTime, String duration) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return String.format(
            "<!DOCTYPE html>" +
            "<html><head><style>" +
            "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
            ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
            ".header { background-color: #dc3545; color: white; padding: 20px; text-align: center; }" +
            ".content { padding: 20px; background-color: #f9f9f9; }" +
            ".info-box { background-color: #f8d7da; padding: 15px; border-left: 4px solid #dc3545; margin: 10px 0; }" +
            ".summary-box { background-color: #d1ecf1; padding: 15px; border-left: 4px solid #17a2b8; margin: 10px 0; }" +
            ".footer { text-align: center; padding: 10px; font-size: 12px; color: #666; }" +
            "</style></head><body>" +
            "<div class='container'>" +
            "<div class='header'><h1>Attendance Sign-Out Confirmed</h1></div>" +
            "<div class='content'>" +
            "<h2>Goodbye %s!</h2>" +
            "<p>You have successfully signed out from attendance.</p>" +
            "<div class='info-box'>" +
            "<p><strong>Date:</strong> %s</p>" +
            "<p><strong>Sign-In Time:</strong> %s</p>" +
            "<p><strong>Sign-Out Time:</strong> %s</p>" +
            "</div>" +
            "<div class='summary-box'>" +
            "<h3>Attendance Summary</h3>" +
            "<p><strong>Total Duration:</strong> %s</p>" +
            "</div>" +
            "<p>Thank you for your time today!</p>" +
            "<p>See you tomorrow!</p>" +
            "</div>" +
            "<div class='footer'><p>&copy; 2026 Internship Attendance System. All rights reserved.</p></div>" +
            "</div></body></html>",
            username, date, signInTime, signOutTime, duration
        );
    }
    
    @Override
    public boolean sendRolePromotionNotification(String toEmail, String username, String newRole) {
        if (!emailEnabled) {
            System.out.println("Email sending is disabled. Skipping role promotion notification.");
            return false;
        }
        
        String subject = "🎉 Role Promotion Notification - User Auth App";
        String htmlContent = buildRolePromotionHtml(username, newRole);
        
        return sendEmail(toEmail, subject, htmlContent);
    }
    
    @Override
    public boolean sendRoleDemotionNotification(String toEmail, String username, String newRole) {
        if (!emailEnabled) {
            System.out.println("Email sending is disabled. Skipping role demotion notification.");
            return false;
        }
        
        String subject = "Role Change Notification - User Auth App";
        String htmlContent = buildRoleDemotionHtml(username, newRole);
        
        return sendEmail(toEmail, subject, htmlContent);
    }
    
    @Override
    public boolean sendAdminLoginNotification(String adminEmail, String adminUsername) {
        if (!emailEnabled) {
            System.out.println("Email sending is disabled. Skipping admin login notification.");
            return false;
        }
        
        // Get all admins except the one who logged in
        List<User> allAdmins = userRepository.findByRole(User.UserRole.ADMIN);
        boolean allSent = true;
        
        for (User admin : allAdmins) {
            if (!admin.getEmail().equals(adminEmail)) {
                String subject = "🔐 Admin Login Alert - User Auth App";
                String htmlContent = buildAdminLoginNotificationHtml(adminUsername, admin.getUsername());
                boolean sent = sendEmail(admin.getEmail(), subject, htmlContent);
                if (!sent) {
                    allSent = false;
                }
            }
        }
        
        return allSent;
    }
    
    @Override
    public boolean sendAdminLogoutNotification(String adminEmail, String adminUsername) {
        if (!emailEnabled) {
            System.out.println("Email sending is disabled. Skipping admin logout notification.");
            return false;
        }
        
        // Get all admins except the one who logged out
        List<User> allAdmins = userRepository.findByRole(User.UserRole.ADMIN);
        boolean allSent = true;
        
        for (User admin : allAdmins) {
            if (!admin.getEmail().equals(adminEmail)) {
                String subject = "🔓 Admin Logout Alert - User Auth App";
                String htmlContent = buildAdminLogoutNotificationHtml(adminUsername, admin.getUsername());
                boolean sent = sendEmail(admin.getEmail(), subject, htmlContent);
                if (!sent) {
                    allSent = false;
                }
            }
        }
        
        return allSent;
    }

    @Override
    public boolean sendRoomQRNotification(String expiresAt, String label) {
        if (!emailEnabled) {
            System.out.println("Email sending is disabled. Skipping room QR notification.");
            return false;
        }

        List<User> interns = userRepository.findByRole(User.UserRole.INTERN);
        if (interns.isEmpty()) {
            System.out.println("No interns found to notify.");
            return true;
        }

        boolean allSent = true;
        for (User intern : interns) {
            String subject = "📱 Attendance QR Code is Ready — Sign In Now";
            String htmlContent = buildRoomQRNotificationHtml(intern.getFirstName(), expiresAt, label);
            boolean sent = sendEmail(intern.getEmail(), subject, htmlContent);
            if (!sent) {
                allSent = false;
                System.err.println("Failed to send room QR notification to: " + intern.getEmail());
            }
        }
        return allSent;
    }

    private String buildRoomQRNotificationHtml(String firstName, String expiresAt, String label) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String sessionLabel = (label != null && !label.isBlank()) ? label : "Morning Sign-In";
        return String.format(
            "<!DOCTYPE html>" +
            "<html><head><style>" +
            "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; }" +
            ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
            ".header { background: linear-gradient(135deg,#667eea 0%%,#764ba2 100%%); color: white; padding: 30px 20px; text-align: center; border-radius: 10px 10px 0 0; }" +
            ".header h1 { margin: 0; font-size: 24px; }" +
            ".content { padding: 25px; background-color: #f9f9f9; }" +
            ".info-box { background-color: #e8eaf6; padding: 15px 20px; border-left: 4px solid #667eea; border-radius: 4px; margin: 15px 0; }" +
            ".info-box p { margin: 6px 0; }" +
            ".cta { text-align: center; margin: 25px 0; }" +
            ".cta a { background: linear-gradient(135deg,#667eea 0%%,#764ba2 100%%); color: white; padding: 14px 32px; border-radius: 8px; text-decoration: none; font-weight: bold; font-size: 16px; }" +
            ".warning { background-color: #fff3e0; padding: 12px 16px; border-left: 4px solid #FF9800; border-radius: 4px; font-size: 14px; margin-top: 15px; }" +
            ".footer { text-align: center; padding: 15px; font-size: 12px; color: #666; border-top: 1px solid #eee; }" +
            "</style></head><body>" +
            "<div class='container'>" +
            "<div class='header'>" +
            "<h1>📱 QR Code is Live — Sign In Now</h1>" +
            "</div>" +
            "<div class='content'>" +
            "<h2>Hello %s!</h2>" +
            "<p>The admin has generated today's attendance QR code. Head to the office and scan it to record your sign-in.</p>" +
            "<div class='info-box'>" +
            "<p><strong>📋 Session:</strong> %s</p>" +
            "<p><strong>🕐 Generated at:</strong> %s</p>" +
            "<p><strong>⏰ Expires at:</strong> %s</p>" +
            "</div>" +
            "<div class='cta'>" +
            "<a href='#'>Open QR Scanner</a>" +
            "</div>" +
            "<div class='warning'>" +
            "⚠️ The QR code is displayed on the screen inside the office. You must be physically present to scan it." +
            "</div>" +
            "</div>" +
            "<div class='footer'><p>&copy; 2026 Internship Attendance System. All rights reserved.</p></div>" +
            "</div></body></html>",
            firstName, sessionLabel, timestamp, expiresAt
        );
    }
    
    private String buildRolePromotionHtml(String username, String newRole) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format(
            "<!DOCTYPE html>" +
            "<html><head><style>" +
            "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
            ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
            ".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }" +
            ".content { padding: 20px; background-color: #f9f9f9; }" +
            ".info-box { background-color: #d4edda; padding: 15px; border-left: 4px solid #4CAF50; margin: 10px 0; }" +
            ".footer { text-align: center; padding: 10px; font-size: 12px; color: #666; }" +
            "</style></head><body>" +
            "<div class='container'>" +
            "<div class='header'><h1>🎉 Congratulations!</h1></div>" +
            "<div class='content'>" +
            "<h2>Hello %s!</h2>" +
            "<p>We are pleased to inform you that your role has been updated.</p>" +
            "<div class='info-box'>" +
            "<p><strong>New Role:</strong> %s</p>" +
            "<p><strong>Effective Date:</strong> %s</p>" +
            "</div>" +
            "<p>With your new role, you now have access to additional features and responsibilities.</p>" +
            "<p>Please log in to explore your new capabilities.</p>" +
            "</div>" +
            "<div class='footer'><p>&copy; 2026 User Auth App. All rights reserved.</p></div>" +
            "</div></body></html>",
            username, newRole, timestamp
        );
    }
    
    private String buildRoleDemotionHtml(String username, String newRole) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format(
            "<!DOCTYPE html>" +
            "<html><head><style>" +
            "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
            ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
            ".header { background-color: #FF9800; color: white; padding: 20px; text-align: center; }" +
            ".content { padding: 20px; background-color: #f9f9f9; }" +
            ".info-box { background-color: #fff3e0; padding: 15px; border-left: 4px solid #FF9800; margin: 10px 0; }" +
            ".footer { text-align: center; padding: 10px; font-size: 12px; color: #666; }" +
            "</style></head><body>" +
            "<div class='container'>" +
            "<div class='header'><h1>Role Change Notification</h1></div>" +
            "<div class='content'>" +
            "<h2>Hello %s!</h2>" +
            "<p>We would like to inform you that your role has been updated.</p>" +
            "<div class='info-box'>" +
            "<p><strong>New Role:</strong> %s</p>" +
            "<p><strong>Effective Date:</strong> %s</p>" +
            "</div>" +
            "<p>Your access permissions have been adjusted accordingly.</p>" +
            "<p>If you have any questions, please contact your administrator.</p>" +
            "</div>" +
            "<div class='footer'><p>&copy; 2026 User Auth App. All rights reserved.</p></div>" +
            "</div></body></html>",
            username, newRole, timestamp
        );
    }
    
    private String buildAdminLoginNotificationHtml(String loggedInAdmin, String recipientAdmin) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format(
            "<!DOCTYPE html>" +
            "<html><head><style>" +
            "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
            ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
            ".header { background-color: #2196F3; color: white; padding: 20px; text-align: center; }" +
            ".content { padding: 20px; background-color: #f9f9f9; }" +
            ".alert-box { background-color: #e3f2fd; padding: 15px; border-left: 4px solid #2196F3; margin: 10px 0; }" +
            ".footer { text-align: center; padding: 10px; font-size: 12px; color: #666; }" +
            "</style></head><body>" +
            "<div class='container'>" +
            "<div class='header'><h1>🔐 Admin Login Alert</h1></div>" +
            "<div class='content'>" +
            "<h2>Hello %s!</h2>" +
            "<p>This is an automated notification to inform you that an administrator has logged into the system.</p>" +
            "<div class='alert-box'>" +
            "<p><strong>Admin Username:</strong> %s</p>" +
            "<p><strong>Login Time:</strong> %s</p>" +
            "</div>" +
            "<p>This notification is sent to all administrators for security monitoring purposes.</p>" +
            "<p>If this activity seems suspicious, please take appropriate action immediately.</p>" +
            "</div>" +
            "<div class='footer'><p>&copy; 2026 User Auth App. All rights reserved.</p></div>" +
            "</div></body></html>",
            recipientAdmin, loggedInAdmin, timestamp
        );
    }
    
    private String buildAdminLogoutNotificationHtml(String loggedOutAdmin, String recipientAdmin) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format(
            "<!DOCTYPE html>" +
            "<html><head><style>" +
            "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
            ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
            ".header { background-color: #FF9800; color: white; padding: 20px; text-align: center; }" +
            ".content { padding: 20px; background-color: #f9f9f9; }" +
            ".alert-box { background-color: #fff3e0; padding: 15px; border-left: 4px solid #FF9800; margin: 10px 0; }" +
            ".footer { text-align: center; padding: 10px; font-size: 12px; color: #666; }" +
            "</style></head><body>" +
            "<div class='container'>" +
            "<div class='header'><h1>🔓 Admin Logout Alert</h1></div>" +
            "<div class='content'>" +
            "<h2>Hello %s!</h2>" +
            "<p>This is an automated notification to inform you that an administrator has logged out of the system.</p>" +
            "<div class='alert-box'>" +
            "<p><strong>Admin Username:</strong> %s</p>" +
            "<p><strong>Logout Time:</strong> %s</p>" +
            "</div>" +
            "<p>This notification is sent to all administrators for security monitoring purposes.</p>" +
            "</div>" +
            "<div class='footer'><p>&copy; 2026 User Auth App. All rights reserved.</p></div>" +
            "</div></body></html>",
            recipientAdmin, loggedOutAdmin, timestamp
        );
    }

    @Override
    public boolean sendPasswordResetEmail(String toEmail, String username, String firstName) {
        if (!emailEnabled) {
            System.out.println("Email sending is disabled. Skipping password reset email.");
            return false;
        }
        
        String subject = "Password Recovery - Pahappa Attendance System";
        String htmlContent = buildPasswordResetEmailHtml(username, firstName);
        
        return sendEmail(toEmail, subject, htmlContent);
    }

    private String buildPasswordResetEmailHtml(String username, String firstName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format(
            "<!DOCTYPE html>" +
            "<html><head><style>" +
            "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; }" +
            ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
            ".header { background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: white; padding: 30px 20px; text-align: center; border-radius: 10px 10px 0 0; }" +
            ".header h1 { margin: 0; font-size: 24px; }" +
            ".content { padding: 25px; background-color: #f9f9f9; }" +
            ".info-box { background-color: #e8eaf6; padding: 15px 20px; border-left: 4px solid #667eea; border-radius: 4px; margin: 15px 0; }" +
            ".info-box p { margin: 6px 0; }" +
            ".warning { background-color: #fff3e0; padding: 12px 16px; border-left: 4px solid #FF9800; border-radius: 4px; font-size: 14px; margin-top: 15px; }" +
            ".cta { text-align: center; margin: 25px 0; }" +
            ".cta a { background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: white; padding: 14px 32px; border-radius: 8px; text-decoration: none; font-weight: bold; font-size: 16px; display: inline-block; }" +
            ".footer { text-align: center; padding: 15px; font-size: 12px; color: #666; border-top: 1px solid #eee; }" +
            ".security-note { background-color: #ffebee; padding: 12px 16px; border-left: 4px solid #f44336; border-radius: 4px; font-size: 13px; margin-top: 15px; }" +
            "</style></head><body>" +
            "<div class='container'>" +
            "<div class='header'>" +
            "<h1>🔐 Password Recovery Request</h1>" +
            "</div>" +
            "<div class='content'>" +
            "<h2>Hello %s!</h2>" +
            "<p>We received a request to recover your password for the Pahappa Attendance System.</p>" +
            "<div class='info-box'>" +
            "<p><strong>👤 Username:</strong> %s</p>" +
            "<p><strong>🕐 Request Time:</strong> %s</p>" +
            "</div>" +
            "<p><strong>Important:</strong> Your account credentials have been verified. Please use your existing password to log in.</p>" +
            "<div class='warning'>" +
            "⚠️ <strong>Security Notice:</strong> If you did not request this password recovery, please contact your administrator immediately. Someone may be trying to access your account." +
            "</div>" +
            "<div class='cta'>" +
            "<a href='#'>Login to Your Account</a>" +
            "</div>" +
            "<div class='security-note'>" +
            "🔒 <strong>Password Security Tips:</strong><br>" +
            "• Never share your password with anyone<br>" +
            "• Use a strong, unique password<br>" +
            "• Change your password regularly<br>" +
            "• Enable two-factor authentication if available" +
            "</div>" +
            "<p style='margin-top: 20px; font-size: 13px; color: #666;'>" +
            "If you're having trouble logging in, please contact your system administrator for assistance." +
            "</p>" +
            "</div>" +
            "<div class='footer'><p>&copy; 2026 Pahappa Attendance System. All rights reserved.</p></div>" +
            "</div></body></html>",
            firstName, username, timestamp
        );
    }
}

// Made with Bob