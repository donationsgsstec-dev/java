package com.pahappa.app.service;

/**
 * Email Service interface for sending email notifications.
 * 
 * This service handles sending emails through the QSSN Mail Services API
 * for various user actions like registration, login, and logout.
 * 
 * @author Pahappa
 * @version 1.0
 */
public interface EmailService {
    
    /**
     * Send a welcome email to a newly registered user.
     * 
     * @param toEmail The recipient's email address
     * @param username The user's username
     * @return true if email was sent successfully, false otherwise
     */
    boolean sendWelcomeEmail(String toEmail, String username);
    
    /**
     * Send a login notification email to the user.
     * 
     * @param toEmail The recipient's email address
     * @param username The user's username
     * @return true if email was sent successfully, false otherwise
     */
    boolean sendLoginNotification(String toEmail, String username);
    
    /**
     * Send a logout notification email to the user.
     * 
     * @param toEmail The recipient's email address
     * @param username The user's username
     * @return true if email was sent successfully, false otherwise
     */
    boolean sendLogoutNotification(String toEmail, String username);
    
    /**
     * Send an attendance sign-in notification email to the user.
     *
     * @param toEmail The recipient's email address
     * @param username The user's username
     * @param signInTime The sign-in time
     * @return true if email was sent successfully, false otherwise
     */
    boolean sendAttendanceSignInNotification(String toEmail, String username, String signInTime);
    
    /**
     * Send an attendance sign-out notification email to the user.
     *
     * @param toEmail The recipient's email address
     * @param username The user's username
     * @param signInTime The sign-in time
     * @param signOutTime The sign-out time
     * @param duration The duration of attendance
     * @return true if email was sent successfully, false otherwise
     */
    boolean sendAttendanceSignOutNotification(String toEmail, String username, String signInTime, String signOutTime, String duration);
    
    /**
     * Send role promotion notification email to the user.
     *
     * @param toEmail The recipient's email address
     * @param username The user's username
     * @param newRole The new role assigned
     * @return true if email was sent successfully, false otherwise
     */
    boolean sendRolePromotionNotification(String toEmail, String username, String newRole);
    
    /**
     * Send role demotion notification email to the user.
     *
     * @param toEmail The recipient's email address
     * @param username The user's username
     * @param newRole The new role assigned
     * @return true if email was sent successfully, false otherwise
     */
    boolean sendRoleDemotionNotification(String toEmail, String username, String newRole);
    
    /**
     * Send admin login notification to all admins.
     *
     * @param adminEmail The admin's email who logged in
     * @param adminUsername The admin's username who logged in
     * @return true if email was sent successfully, false otherwise
     */
    boolean sendAdminLoginNotification(String adminEmail, String adminUsername);
    
    /**
     * Send admin logout notification to all admins.
     *
     * @param adminEmail The admin's email who logged out
     * @param adminUsername The admin's username who logged out
     * @return true if email was sent successfully, false otherwise
     */
    boolean sendAdminLogoutNotification(String adminEmail, String adminUsername);

    /**
     * Notify all interns that the room QR code is now active and ready to scan.
     *
     * @param expiresAt  human-readable expiry time shown in the email (e.g. "09:00")
     * @param label      optional session label set by the admin
     * @return true if all emails were dispatched successfully
     */
    boolean sendRoomQRNotification(String expiresAt, String label);
}
