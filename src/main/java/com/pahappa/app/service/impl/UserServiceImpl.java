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
     * Constructor-based dependency injection.
     * 
     * Spring automatically injects UserRepository and PasswordEncoder beans.
     * Constructor injection is preferred over field injection for better testability.
     * 
     * @param userRepository Repository for user data access
     * @param passwordEncoder Encoder for password hashing
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}

// Made with Bob
