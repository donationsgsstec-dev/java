package com.pahappa.app.controller;

import com.pahappa.app.entity.User;
import com.pahappa.app.service.EmailService;
import com.pahappa.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Authentication Controller for handling user registration and login.
 *
 * This controller manages:
 * - User registration (display form and process submission)
 * - Login page display
 * - Form validation and error handling
 * - Email notifications for registration
 *
 * Annotations:
 * - @Controller: Marks this as a Spring MVC controller
 * - @GetMapping: Handles HTTP GET requests
 * - @PostMapping: Handles HTTP POST requests
 *
 * The controller returns view names (JSP file names without extension)
 * which are resolved by Spring MVC using the prefix and suffix from application.properties.
 *
 * @author Pahappa
 * @version 1.0
 */
@Controller
public class AuthController {

    /**
     * Service for user-related operations
     */
    private final UserService userService;
    
    /**
     * Service for sending email notifications
     */
    private final EmailService emailService;

    /**
     * Constructor-based dependency injection.
     *
     * @param userService Service for user operations
     * @param emailService Service for email notifications
     */
    @Autowired
    public AuthController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    /**
     * Display the registration form.
     * 
     * This method handles GET requests to /register.
     * It creates a new empty User object and adds it to the model
     * for form binding in the JSP view.
     * 
     * @param model Spring MVC Model for passing data to the view
     * @return View name "register" (resolves to /WEB-INF/views/register.jsp)
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Create a new User object for form binding
        User user = new User();
        model.addAttribute("user", user);
        
        return "register";
    }

    /**
     * Test page for Julia chat widget
     */
    @GetMapping("/test-widget")
    public String testWidget() {
        return "test-widget";
    }

    /**
     * Process the registration form submission.
     * 
     * This method handles POST requests to /register/save.
     * It validates the form data, checks for errors, and saves the user.
     * 
     * Process:
     * 1. Validate form data using @Valid annotation
     * 2. Check for validation errors
     * 3. Check if username or email already exists
     * 4. Save the user (password is automatically encoded in service layer)
     * 5. Redirect to login page on success
     * 
     * @param user User object populated from form data
     * @param bindingResult Contains validation errors if any
     * @param model Spring MVC Model for passing data to the view
     * @return Redirect to login on success, or back to registration form on error
     */
    @PostMapping("/register/save")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                              BindingResult bindingResult,
                              Model model) {
        
        // Check for validation errors (from @Valid annotation)
        if (bindingResult.hasErrors()) {
            // Add general error message
            model.addAttribute("errorMessage", "Please correct the errors below");
            // Return to registration form with error messages
            return "register";
        }

        // Check if username already exists
        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("usernameError", "Username '" + user.getUsername() + "' is already taken");
            model.addAttribute("errorMessage", "Username already exists. Please choose a different username.");
            return "register";
        }

        // Check if email already exists
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("emailError", "Email '" + user.getEmail() + "' is already registered");
            model.addAttribute("errorMessage", "Email already exists. Please use a different email address.");
            return "register";
        }

        try {
            // Register the user (password will be securely encoded in service layer using BCrypt)
            User savedUser = userService.registerUser(user);
            
            // Log successful registration (for debugging)
            System.out.println("User registered successfully: " + savedUser.getUsername());
            
            // Send welcome email asynchronously (don't block registration if email fails)
            try {
                emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getUsername());
            } catch (Exception emailException) {
                // Log email error but don't fail the registration
                System.err.println("Failed to send welcome email: " + emailException.getMessage());
            }
            
            // Redirect to login page with success message
            return "redirect:/login?registered=true";
            
        } catch (RuntimeException e) {
            // Handle runtime exceptions (e.g., database errors)
            model.addAttribute("errorMessage", "Registration failed: " + e.getMessage());
            System.err.println("Registration error: " + e.getMessage());
            return "register";
        } catch (Exception e) {
            // Handle any unexpected errors
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            System.err.println("Unexpected registration error: " + e.getMessage());
            e.printStackTrace();
            return "register";
        }
    }

    /**
     * Display the login form.
     * 
     * This method handles GET requests to /login.
     * It checks for query parameters to display appropriate messages:
     * - error: Login failed
     * - logout: User logged out successfully
     * - registered: User registered successfully
     * 
     * @param error Optional parameter indicating login error
     * @param logout Optional parameter indicating successful logout
     * @param registered Optional parameter indicating successful registration
     * @param model Spring MVC Model for passing data to the view
     * @return View name "login" (resolves to /WEB-INF/views/login.jsp)
     */
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               @RequestParam(value = "registered", required = false) String registered,
                               @RequestParam(value = "passwordReset", required = false) String passwordReset,
                               Model model) {
        
        // Add error message if login failed
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }
        
        // Add success message if user logged out
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully");
        }
        
        // Add success message if user just registered
        if (registered != null) {
            model.addAttribute("successMessage", "Registration successful! Please login.");
        }
        
        // Add success message if password reset email was sent
        if (passwordReset != null) {
            model.addAttribute("successMessage", "Password recovery email has been sent! Please check your email.");
        }
        
        return "login";
    }

    /**
     * Display the forgot password form.
     *
     * This method handles GET requests to /forgot-password.
     * It shows a form where users can enter their first name and username
     * to receive password recovery information via email.
     *
     * @param model Spring MVC Model for passing data to the view
     * @return View name "forgot-password" (resolves to /WEB-INF/views/forgot-password.jsp)
     */
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        return "forgot-password";
    }

    /**
     * Process the forgot password form submission.
     *
     * This method handles POST requests to /forgot-password.
     * It validates the user's identity using first name and username,
     * then sends password recovery information to their registered email.
     *
     * Process:
     * 1. Validate that first name and username are provided
     * 2. Find user by first name and username
     * 3. Send password recovery email
     * 4. Redirect to login page with success message
     *
     * @param firstName User's first name
     * @param username User's username
     * @param model Spring MVC Model for passing data to the view
     * @return Redirect to login on success, or back to forgot password form on error
     */
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("firstName") String firstName,
                                       @RequestParam("username") String username,
                                       Model model) {
        
        // Validate input
        if (firstName == null || firstName.trim().isEmpty()) {
            model.addAttribute("errorMessage", "First name is required");
            return "forgot-password";
        }
        
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("errorMessage", "Username is required");
            return "forgot-password";
        }
        
        try {
            // Send password reset email
            boolean emailSent = userService.sendPasswordResetEmail(firstName.trim(), username.trim());
            
            if (emailSent) {
                // Log successful password reset request
                System.out.println("Password reset email sent for user: " + username);
                
                // Redirect to login page with success message
                return "redirect:/login?passwordReset=true";
            } else {
                model.addAttribute("errorMessage", "Failed to send password reset email. Please try again.");
                return "forgot-password";
            }
            
        } catch (RuntimeException e) {
            // Handle user not found or other errors
            model.addAttribute("errorMessage", "No account found with the provided first name and username. Please check your details and try again.");
            System.err.println("Password reset error: " + e.getMessage());
            return "forgot-password";
        } catch (Exception e) {
            // Handle any unexpected errors
            model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            System.err.println("Unexpected password reset error: " + e.getMessage());
            e.printStackTrace();
            return "forgot-password";
        }
    }
}

// Made with Bob
