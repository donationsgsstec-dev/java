package com.pahappa.app.config;

import com.pahappa.app.service.EmailService;
import com.pahappa.app.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom Authentication Success Handler.
 * 
 * This handler is invoked after a successful login and:
 * 1. Sends a login notification email to the user
 * 2. Redirects the user to the home page
 * 
 * The email is sent asynchronously to avoid blocking the login process.
 * 
 * @author Pahappa
 * @version 1.0
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
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
    public CustomAuthenticationSuccessHandler(EmailService emailService, @Lazy UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }
    
    /**
     * Handle successful authentication.
     * 
     * This method is called after a user successfully logs in.
     * It sends a login notification email and redirects to the home page.
     * 
     * @param request The HTTP request
     * @param response The HTTP response
     * @param authentication The authentication object containing user details
     * @throws IOException If an I/O error occurs
     * @throws ServletException If a servlet error occurs
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {
        
        // Get the username from the authentication object
        String username = authentication.getName();
        
        System.out.println("User logged in successfully: " + username);
        
        // Send login notification email asynchronously
        try {
            // Get user's email from database
            var userOptional = userService.findByUsername(username);
            if (userOptional.isPresent()) {
                var user = userOptional.get();
                emailService.sendLoginNotification(user.getEmail(), username);
            }
        } catch (Exception e) {
            // Log error but don't fail the login
            System.err.println("Failed to send login notification email: " + e.getMessage());
        }
        
        // Redirect to home page
        response.sendRedirect("/home");
    }
}

// Made with Bob