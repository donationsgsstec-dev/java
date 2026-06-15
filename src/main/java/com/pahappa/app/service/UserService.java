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
     * Check if email exists.
     * 
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Change the password for a user.
     * Verifies the current password before applying the new one.
     *
     * @param username        the user whose password is being changed
     * @param currentPassword the user's current (plain-text) password
     * @param newPassword     the desired new plain-text password
     * @throws RuntimeException if the current password is wrong or user not found
     */
    void changePassword(String username, String currentPassword, String newPassword);

    /**
     * Promote an intern to ADMIN role.
     *
     * @param userId the ID of the user to promote
     * @throws RuntimeException if user not found
     */
    void promoteToAdmin(Long userId);

    /**
     * Demote an admin back to INTERN role.
     *
     * @param userId the ID of the user to demote
     * @throws RuntimeException if user not found
     */
    void demoteToIntern(Long userId);

    /**
     * Find all users with INTERN role.
     *
     * @return list of interns
     */
    List<User> findAllInterns();

    /**
     * Find all users with ADMIN role.
     *
     * @return list of admins
     */
    List<User> findAllAdmins();

    /**
     * Find a user by first name and username for password recovery.
     *
     * @param firstName The user's first name
     * @param username The user's username
     * @return Optional containing the User if found, empty Optional otherwise
     */
    Optional<User> findByFirstNameAndUsername(String firstName, String username);

    /**
     * Send password reset email to user.
     * Finds the user by first name and username, then sends their password to their registered email.
     *
     * @param firstName The user's first name
     * @param username The user's username
     * @return true if password was sent successfully, false otherwise
     * @throws RuntimeException if user not found
     */
    boolean sendPasswordResetEmail(String firstName, String username);

    /**
     * Reset admin password: generates a temporary password, saves it,
     * and emails it to the admin's registered email address.
     *
     * @param username The admin's username
     * @return true if successful
     * @throws RuntimeException if user not found or not an admin
     */
    boolean resetAdminPassword(String username);
}

// Made with Bob
