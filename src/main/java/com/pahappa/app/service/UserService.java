package com.pahappa.app.service;

import com.pahappa.app.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * UserService interface defining business logic operations for User management.
 * 
 * This interface defines the contract for user-related operations including:
 * - User registration
 * - User retrieval
 * - User validation
 * 
 * The implementation of this interface will contain the actual business logic
 * and interact with the UserRepository for data persistence.
 * 
 * @author Pahappa
 * @version 1.0
 */
public interface UserService {

    /**
     * Register a new user in the system.
     * 
     * This method:
     * 1. Validates that username and email are unique
     * 2. Encodes the password using BCrypt
     * 3. Saves the user to the database
     * 
     * @param user The user object containing registration details
     * @return The saved user with generated ID
     * @throws RuntimeException if username or email already exists
     */
    User registerUser(User user);

    /**
     * Find a user by their username.
     * 
     * @param username The username to search for
     * @return Optional containing the User if found, empty Optional otherwise
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by their email address.
     * 
     * @param email The email address to search for
     * @return Optional containing the User if found, empty Optional otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Find a user by their ID.
     * 
     * @param id The user ID to search for
     * @return Optional containing the User if found, empty Optional otherwise
     */
    Optional<User> findById(Long id);

    /**
     * Get all registered users.
     * 
     * @return List of all users in the system
     */
    List<User> findAllUsers();

    /**
     * Check if a username already exists in the system.
     * 
     * Used during registration validation to prevent duplicate usernames.
     * 
     * @param username The username to check
     * @return true if username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Check if an email already exists in the system.
     * 
     * Used during registration validation to prevent duplicate emails.
     * 
     * @param email The email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);
}

// Made with Bob
