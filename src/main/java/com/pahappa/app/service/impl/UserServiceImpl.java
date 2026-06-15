package com.pahappa.app.service.impl;

import com.pahappa.app.entity.User;
import com.pahappa.app.repository.UserRepository;
import com.pahappa.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of UserService interface.
 * 
 * This service class contains the business logic for user management operations.
 * It interacts with UserRepository for data persistence and uses PasswordEncoder
 * for secure password hashing.
 * 
 * Annotations:
 * - @Service: Marks this as a Spring service component
 * - @Transactional: Ensures database operations are executed within transactions
 * 
 * @author Pahappa
 * @version 1.0
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    /**
     * Repository for User entity database operations
     */
    private final UserRepository userRepository;

    /**
     * Password encoder for hashing passwords (BCrypt)
     */
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Email service for sending notifications
     */
    private final com.pahappa.app.service.EmailService emailService;

    /**
     * Constructor-based dependency injection.
     *
     * Spring automatically injects UserRepository and PasswordEncoder beans.
     * Constructor injection is preferred over field injection for better testability.
     *
     * @param userRepository Repository for user data access
     * @param passwordEncoder Encoder for password hashing
     * @param emailService Service for sending email notifications
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          com.pahappa.app.service.EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /**
     * Register a new user in the system.
     * 
     * Process:
     * 1. Check if username already exists
     * 2. Check if email already exists
     * 3. Encode the password using BCrypt
     * 4. Save the user to database
     * 
     * @param user User object with registration details
     * @return Saved user with generated ID
     * @throws RuntimeException if username or email already exists
     */
    @Override
    public User registerUser(User user) {
        // Validate username uniqueness
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }

        // Validate email uniqueness
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }

        // Encode password using BCrypt before saving
        // BCrypt automatically generates a salt and produces a secure hash
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Save user to database
        // JPA will automatically set the ID and timestamps
        return userRepository.save(user);
    }

    /**
     * Find a user by username.
     * 
     * @param username Username to search for
     * @return Optional containing User if found
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Find a user by email.
     * 
     * @param email Email address to search for
     * @return Optional containing User if found
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Find a user by ID.
     * 
     * @param id User ID to search for
     * @return Optional containing User if found
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Get all users in the system.
     * 
     * @return List of all users
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Check if username exists.
     * 
     * @param username Username to check
     * @return true if exists, false otherwise
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Check if email exists.
     * 
     * @param email Email to check
     * @return true if exists, false otherwise
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Change password — verifies current password then saves new BCrypt hash.
     */
    @Override
    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Promote a user to ADMIN role.
     */
    @Override
    public void promoteToAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setRole(User.UserRole.ADMIN);
        userRepository.save(user);
        
        // Send promotion notification email
        try {
            emailService.sendRolePromotionNotification(user.getEmail(), user.getUsername(), "ADMIN");
            System.out.println("Promotion notification sent to: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send promotion notification: " + e.getMessage());
        }
    }

    /**
     * Demote a user back to INTERN role.
     */
    @Override
    public void demoteToIntern(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setRole(User.UserRole.INTERN);
        userRepository.save(user);
        
        // Send demotion notification email
        try {
            emailService.sendRoleDemotionNotification(user.getEmail(), user.getUsername(), "INTERN");
            System.out.println("Demotion notification sent to: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send demotion notification: " + e.getMessage());
        }
    }

    /**
     * Return all interns.
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> findAllInterns() {
        return userRepository.findByRole(User.UserRole.INTERN);
    }

    /**
     * Return all admins.
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> findAllAdmins() {
        return userRepository.findByRole(User.UserRole.ADMIN);
    }

    /**
     * Find a user by first name and username.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByFirstNameAndUsername(String firstName, String username) {
        return userRepository.findByFirstNameAndUsername(firstName, username);
    }

    /**
     * Send password reset email to user.
     * Note: This sends account recovery info, not the actual hashed password.
     */
    @Override
    public boolean sendPasswordResetEmail(String firstName, String username) {
        User user = userRepository.findByFirstNameAndUsername(firstName, username)
                .orElseThrow(() -> new RuntimeException("No user found with the provided first name and username"));

        // Generate a temp password and reset for regular users too
        String tempPassword = generateTempPassword();
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        try {
            boolean emailSent = emailService.sendAdminPasswordResetEmail(
                user.getEmail(),
                user.getUsername(),
                tempPassword
            );
            if (emailSent) {
                System.out.println("Password reset email sent to: " + user.getEmail());
                return true;
            } else {
                System.err.println("Failed to send password reset email to: " + user.getEmail());
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error sending password reset email: " + e.getMessage());
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage());
        }
    }

    /**
     * Reset admin password: generates a temp password, saves BCrypt hash, emails plain text to admin.
     */
    @Override
    public boolean resetAdminPassword(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("No account found with username: " + username));

        if (user.getRole() == null || !user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Account is not an admin account.");
        }

        String tempPassword = generateTempPassword();
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        return emailService.sendAdminPasswordResetEmail(user.getEmail(), user.getUsername(), tempPassword);
    }

    /**
     * Generates a secure 10-character temporary password.
     */
    private String generateTempPassword() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789@#$!";
        java.util.Random random = new java.security.SecureRandom();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}

// Made with Bob
