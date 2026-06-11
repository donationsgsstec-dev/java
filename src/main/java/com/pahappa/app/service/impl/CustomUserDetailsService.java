package com.pahappa.app.service.impl;

import com.pahappa.app.entity.User;
import com.pahappa.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * 
 * This service is used by Spring Security to load user-specific data during authentication.
 * It bridges our User entity with Spring Security's UserDetails interface.
 * 
 * Key responsibilities:
 * 1. Load user from database by username
 * 2. Convert our User entity to Spring Security's UserDetails
 * 3. Provide user authorities/roles for authorization
 * 
 * Spring Security calls loadUserByUsername() during the authentication process
 * to retrieve user credentials and authorities.
 * 
 * @author Pahappa
 * @version 1.0
 */
@Service  
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repository for accessing user data
     */
    private final UserRepository userRepository;

    /**
     * Constructor-based dependency injection.
     * 
     * @param userRepository Repository for user data access
     */
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load user by username for Spring Security authentication.
     * 
     * This method is called by Spring Security during the authentication process.
     * It retrieves the user from the database and converts it to UserDetails.
     * 
     * Process:
     * 1. Find user by username in database
     * 2. If not found, throw UsernameNotFoundException
     * 3. Convert User entity to Spring Security UserDetails
     * 4. Return UserDetails with username, password, and authorities
     * 
     * @param username The username to authenticate
     * @return UserDetails object containing user authentication information
     * @throws UsernameNotFoundException if user is not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user in database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + username));

        // Convert our User entity to Spring Security's UserDetails
        // This includes username, password, and authorities
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                getAuthorities(user)
        );
    }

    /**
     * Get authorities (roles) for the user.
     * 
     * In this simple implementation, all users have the "ROLE_USER" authority.
     * In a more complex system, you would retrieve roles from a database table.
     * 
     * Spring Security requires role names to be prefixed with "ROLE_".
     * 
     * @param user The user to get authorities for
     * @return Collection of granted authorities
     */
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        // For this simple application, all users have the USER role
        // In a production system, you would typically:
        // 1. Have a Role entity and User-Role relationship
        // 2. Fetch roles from database
        // 3. Convert roles to GrantedAuthority objects
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}

// Made with Bob
