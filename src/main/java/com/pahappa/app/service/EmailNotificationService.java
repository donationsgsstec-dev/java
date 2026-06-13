package com.pahappa.app.service;

import com.pahappa.app.entity.Attendance;
import com.pahappa.app.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.format.DateTimeFormatter;

/**
 * Service for sending email notifications.
 * 
 * Sends notifications for late arrivals, early departures, and other attendance events.
 * 
 * @author Pahappa
 * @version 1.0
 */
@Service
public class EmailNotificationService {

    @Autowired(required = false)
    private JavaMailSender mailSender;
    
    @Autowired
    private com.pahappa.app.repository.UserRepository userRepository;

    @Value("${spring.mail.username:noreply@pahappa.com}")
    private String fromEmail;

    @Value("${app.admin.email:admin@pahappa.com}")
    private String adminEmail;

    @Value("${app.name:Pahappa Attendance System}")
    private String appName;

    /**
     * Send late arrival notification
     * @param user User who arrived late
     * @param attendance Attendance record
     */
    @Async
    public void sendLateArrivalNotification(User user, Attendance attendance) {
        if (mailSender == null) {
            System.out.println("Mail sender not configured. Skipping email notification.");
            return;
        }

        try {
            // Send notification to the user
            MimeMessage userMessage = mailSender.createMimeMessage();
            MimeMessageHelper userHelper = new MimeMessageHelper(userMessage, true, "UTF-8");

            userHelper.setFrom(fromEmail);
            userHelper.setTo(user.getEmail());
            userHelper.setSubject("⚠️ Late Arrival Notification - " + appName);

            String htmlContent = buildLateArrivalEmail(user, attendance);
            userHelper.setText(htmlContent, true);

            mailSender.send(userMessage);
            System.out.println("Late arrival notification sent to: " + user.getEmail());
            
            // Send notification to all admins
            java.util.List<User> admins = userRepository.findByRole(User.UserRole.ADMIN);
            for (User admin : admins) {
                try {
                    MimeMessage adminMessage = mailSender.createMimeMessage();
                    MimeMessageHelper adminHelper = new MimeMessageHelper(adminMessage, true, "UTF-8");
                    
                    adminHelper.setFrom(fromEmail);
                    adminHelper.setTo(admin.getEmail());
                    adminHelper.setSubject("⚠️ Late Arrival Alert: " + user.getUsername() + " - " + appName);
                    
                    String adminHtmlContent = buildLateArrivalAdminEmail(user, attendance, admin);
                    adminHelper.setText(adminHtmlContent, true);
                    
                    mailSender.send(adminMessage);
                    System.out.println("Late arrival admin notification sent to: " + admin.getEmail());
                } catch (MessagingException e) {
                    System.err.println("Failed to send late arrival notification to admin " + admin.getEmail() + ": " + e.getMessage());
                }
            }
        } catch (MessagingException e) {
            System.err.println("Failed to send late arrival notification: " + e.getMessage());
        }
    }

    /**
     * Send early departure notification
     * @param user User who departed early
     * @param attendance Attendance record
     */
    @Async
    public void sendEarlyDepartureNotification(User user, Attendance attendance) {
        if (mailSender == null) {
            System.out.println("Mail sender not configured. Skipping email notification.");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(user.getEmail());
            helper.setCc(adminEmail);
            helper.setSubject("⚠️ Early Departure Notification - " + appName);

            String htmlContent = buildEarlyDepartureEmail(user, attendance);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("Early departure notification sent to: " + user.getEmail());
        } catch (MessagingException e) {
            System.err.println("Failed to send early departure notification: " + e.getMessage());
        }
    }

    /**
     * Send attendance summary email
     * @param user User
     * @param subject Email subject
     * @param content Email content
     */
    @Async
    public void sendAttendanceSummary(User user, String subject, String content) {
        if (mailSender == null) {
            System.out.println("Mail sender not configured. Skipping email notification.");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);
            System.out.println("Attendance summary sent to: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send attendance summary: " + e.getMessage());
        }
    }

    /**
     * Build HTML email for late arrival
     */
    private String buildLateArrivalEmail(User user, Attendance attendance) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #f44336; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }" +
                ".content { background-color: #f9f9f9; padding: 20px; border: 1px solid #ddd; }" +
                ".info-box { background-color: white; padding: 15px; margin: 10px 0; border-left: 4px solid #f44336; }" +
                ".footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h2>⚠️ Late Arrival Notification</h2>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Dear " + user.getFirstName() + ",</p>" +
                "<p>This is to notify you that you arrived late today.</p>" +
                "<div class='info-box'>" +
                "<strong>Date:</strong> " + attendance.getAttendanceDate().format(dateFormatter) + "<br>" +
                "<strong>Check-in Time:</strong> " + attendance.getSignInTime().format(timeFormatter) + "<br>" +
                "<strong>Late by:</strong> " + attendance.getFormattedLateTime() + "<br>" +
                "</div>" +
                "<p>Please ensure to arrive on time in the future.</p>" +
                "<p>If you have any concerns, please contact your supervisor.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>This is an automated message from " + appName + "</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Build HTML email for early departure
     */
    private String buildEarlyDepartureEmail(User user, Attendance attendance) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #ff9800; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }" +
                ".content { background-color: #f9f9f9; padding: 20px; border: 1px solid #ddd; }" +
                ".info-box { background-color: white; padding: 15px; margin: 10px 0; border-left: 4px solid #ff9800; }" +
                ".footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h2>⚠️ Early Departure Notification</h2>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Dear " + user.getFirstName() + ",</p>" +
                "<p>This is to notify you that you departed early today.</p>" +
                "<div class='info-box'>" +
                "<strong>Date:</strong> " + attendance.getAttendanceDate().format(dateFormatter) + "<br>" +
                "<strong>Check-out Time:</strong> " + attendance.getSignOutTime().format(timeFormatter) + "<br>" +
                "<strong>Early by:</strong> " + (attendance.getEarlyDepartureMinutes() != null ? 
                    attendance.getEarlyDepartureMinutes() + " minutes" : "N/A") + "<br>" +
                "</div>" +
                "<p>Please ensure to complete your work hours.</p>" +
                "<p>If you have any concerns, please contact your supervisor.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>This is an automated message from " + appName + "</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
    
    /**
     * Build HTML email for late arrival notification to admins
     */
    private String buildLateArrivalAdminEmail(User user, Attendance attendance, User admin) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #f44336; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }" +
                ".content { background-color: #f9f9f9; padding: 20px; border: 1px solid #ddd; }" +
                ".info-box { background-color: white; padding: 15px; margin: 10px 0; border-left: 4px solid #f44336; }" +
                ".footer { text-align: center; padding: 20px; color: #666; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h2>⚠️ Late Arrival Alert</h2>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Dear " + admin.getFirstName() + ",</p>" +
                "<p>This is an automated notification to inform you that an intern has arrived late.</p>" +
                "<div class='info-box'>" +
                "<strong>Intern Name:</strong> " + user.getFirstName() + " " + user.getLastName() + "<br>" +
                "<strong>Username:</strong> " + user.getUsername() + "<br>" +
                "<strong>Email:</strong> " + user.getEmail() + "<br>" +
                "<strong>Date:</strong> " + attendance.getAttendanceDate().format(dateFormatter) + "<br>" +
                "<strong>Check-in Time:</strong> " + attendance.getSignInTime().format(timeFormatter) + "<br>" +
                "<strong>Late by:</strong> " + attendance.getFormattedLateTime() + "<br>" +
                "</div>" +
                "<p>Please take appropriate action if necessary.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>This is an automated message from " + appName + "</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}

// Made with Bob