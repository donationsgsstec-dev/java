package com.pahappa.app.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home Controller for handling the home page.
 * 
 * This controller manages the home page which is displayed after successful login.
 * It retrieves the authenticated user's information and displays it.
 * 
 * Annotations:
 * - @Controller: Marks this as a Spring MVC controller
 * - @GetMapping: Handles HTTP GET requests
 * 
 * @author Pahappa
 * @version 1.0
 */
@Controller
public class HomeController {

    @Value("${gss.ai.api.key}")
    private String gssApiKey;

    @Value("${gss.ai.cf.worker.url}")
    private String gssCfWorkerUrl;

    @Value("${gss.ai.hf.engine.url}")
    private String gssHfEngineUrl;

    @Value("${gss.ai.model}")
    private String gssModel;

    @Value("${gss.ai.enabled}")
    private boolean gssEnabled;

    /**
     * Display the home page for authenticated users.
     * 
     * This method:
     * 1. Retrieves the current authenticated user from SecurityContext
     * 2. Extracts the username
     * 3. Passes it to the view for display
     * 
     * The SecurityContextHolder provides access to the security context,
     * which contains the authentication information of the current user.
     * 
     * @param model Spring MVC Model for passing data to the view
     * @return View name "home" (resolves to /WEB-INF/views/home.jsp)
     */
    @GetMapping("/home")
    public String home(Model model) {
        // Get the current authentication object from SecurityContext
        // This contains information about the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract the username from the authentication object
        // getName() returns the principal (username in our case)
        String username = authentication.getName();
        
        // Add username to model for display in JSP
        model.addAttribute("username", username);
        
        // Add GSS AI configuration to model
        addGssConfigToModel(model);
        
        return "home";
    }

    /**
     * Redirect root URL to home page.
     * 
     * This method handles requests to the root URL (/) and redirects
     * authenticated users to the home page.
     * 
     * @return Redirect to /home
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    /**
     * Helper method to add GSS AI configuration to model.
     * This makes the configuration available to JSP pages.
     *
     * @param model Spring MVC Model
     */
    private void addGssConfigToModel(Model model) {
        model.addAttribute("gssApiKey", gssApiKey);
        model.addAttribute("gssCfWorkerUrl", gssCfWorkerUrl);
        model.addAttribute("gssHfEngineUrl", gssHfEngineUrl);
        model.addAttribute("gssModel", gssModel);
        model.addAttribute("gssEnabled", gssEnabled);
    }
}

// Made with Bob
