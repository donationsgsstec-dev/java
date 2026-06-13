package com.pahappa.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling admin-specific login.
 * 
 * This controller provides a separate login page for administrators
 * to access admin-only features.
 * 
 * @author Pahappa
 * @version 1.0
 */
@Controller
@RequestMapping("/admin")
public class AdminLoginController {

    /**
     * Display the admin login page.
     * 
     * @param error Optional error parameter for failed login attempts
     * @param logout Optional logout parameter for successful logout
     * @param model Spring MVC Model for passing data to the view
     * @return View name "admin-login" (resolves to /WEB-INF/views/admin-login.jsp)
     */
    @GetMapping("/login")
    public String adminLogin(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Invalid username or password. Admin access only.");
        }
        
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        
        return "admin-login";
    }
}

// Made with Bob