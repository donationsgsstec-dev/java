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
}

// Made with Bob