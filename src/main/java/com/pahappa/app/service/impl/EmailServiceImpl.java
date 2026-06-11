package com.pahappa.app.service.impl;

import com.pahappa.app.service.EmailService;
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
    
    public EmailServiceImpl() {
        this.restTemplate = new RestTemplate();
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
}

// Made with Bob