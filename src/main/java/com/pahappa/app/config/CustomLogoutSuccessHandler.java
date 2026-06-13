package com.pahappa.app.config;

import com.pahappa.app.service.EmailService;
import com.pahappa.app.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom Logout Success Handler.
 * 
 * This handler is invoked after a successful logout and:
 * 1. Sends a logout notification email to the user
 * 2. Redirects the user to the login page with a logout message
 * 
 * The email is sent asynchronously to avoid blocking the logout process.
 * 
 * @author Pahappa
 * @version 1.0
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    
    /**
     * Service for sending email notifications
     */
    private final EmailService emailService;
    
    /**
     * Service for user-related operations
     */
    private final UserService userService;
    
    /**
     * Constructor-based dependency injection.
     *
     * Uses @Lazy on UserService to break circular dependency with SecurityConfig.
     *
     * @param emailService Service for email notifications
     * @param userService Service for user operations
     */
    @Autowired
    public CustomLogoutSuccessHandler(EmailService emailService, @Lazy UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }
    
    /**
     * Handle successful logout.
     * 
     * This method is called after a user successfully logs out.
     * It sends a logout notification email and redirects to the login page.
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @param authentication The authentication object containing user details (may be null)
     * @throws IOException If an I/O error occurs
     * @throws ServletException If a servlet error occurs
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                               HttpServletResponse response,
                               Authentication authentication) throws IOException, ServletException {
        
        // Check if authentication is not null (user was logged in)
        if (authentication != null) {
            // Get the username from the authentication object
            String username = authentication.getName();
            
            System.out.println("User logged out successfully: " + username);
            
            // Send logout notification email asynchronously
            try {
                // Get user's email from database
                var userOptional = userService.findByUsername(username);
                if (userOptional.isPresent()) {
                    var user = userOptional.get();
                    emailService.sendLogoutNotification(user.getEmail(), username);
                    
                    // If user is an admin, notify other admins
                    if (user.getRole() == com.pahappa.app.entity.User.UserRole.ADMIN) {
                        emailService.sendAdminLogoutNotification(user.getEmail(), username);
                    }
                }
            } catch (Exception e) {
                // Log error but don't fail the logout
                System.err.println("Failed to send logout notification email: " + e.getMessage());
            }
        }
        
        // Redirect to login page with logout success message
        response.sendRedirect("/login?logout=true");
    }
}

// Made with Bob