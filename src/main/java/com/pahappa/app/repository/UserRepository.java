package com.pahappa.app.repository;

import com.pahappa.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository interface for database operations on User entities.
 * 
 * This interface extends JpaRepository which provides:
 * - CRUD operations (save, findById, findAll, delete, etc.)
 * - Pagination and sorting capabilities
 * - Query derivation from method names
 * 
 * Spring Data JPA automatically implements this interface at runtime.
 * No need to write implementation code for standard CRUD operations.
 * 
 * Custom query methods are defined using method naming conventions:
 * - findBy[PropertyName] - finds entities by a specific property
 * - existsBy[PropertyName] - checks if entity exists by property
 * 
 * @author Pahappa
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by username.
     * 
     * This method is used by Spring Security's UserDetailsService
     * to load user details during authentication.
     * 
     * @param username The username to search for
     * @return Optional containing the User if found, empty Optional otherwise
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by email address.
     * 
     * Useful for email-based authentication or user lookup.
     * 
     * @param email The email address to search for
     * @return Optional containing the User if found, empty Optional otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists with the given username.
     * 
     * Used during registration to prevent duplicate usernames.
     * More efficient than findByUsername when you only need to check existence.
     * 
     * @param username The username to check
     * @return true if a user with this username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Check if a user exists with the given email.
     * 
     * Used during registration to prevent duplicate email addresses.
     * More efficient than findByEmail when you only need to check existence.
     * 
     * @param email The email address to check
     * @return true if a user with this email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Find all users with a specific role.
     *
     * @param role the role to filter by
     * @return list of matching users
     */
    List<User> findByRole(User.UserRole role);

    /**
     * Find a user by first name and username.
     * Used for password recovery verification.
     *
     * @param firstName The user's first name
     * @param username The user's username
     * @return Optional containing the User if found, empty Optional otherwise
     */
    Optional<User> findByFirstNameAndUsername(String firstName, String username);
}

// Made with Bob
