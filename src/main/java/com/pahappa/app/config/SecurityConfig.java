package com.pahappa.app.config;

import com.pahappa.app.service.impl.CustomUserDetailsService;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security Configuration class.
 *
 * This class configures the security settings for the application including:
 * - Authentication mechanism (form-based login)
 * - Password encoding (BCrypt)
 * - URL authorization rules
 * - Login/logout behavior with email notifications
 * - Custom UserDetailsService integration
 *
 * Annotations:
 * - @Configuration: Marks this as a Spring configuration class
 * - @EnableWebSecurity: Enables Spring Security's web security support
 *
 * Spring Security 6.x uses a component-based configuration approach
 * with SecurityFilterChain beans instead of extending WebSecurityConfigurerAdapter.
 *
 * @author Pahappa
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Custom UserDetailsService for loading user-specific data
     */
    private final CustomUserDetailsService userDetailsService;
    
    /**
     * Custom authentication success handler for login notifications
     */
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    
    /**
     * Custom logout success handler for logout notifications
     */
    private final CustomLogoutSuccessHandler logoutSuccessHandler;

    /**
     * Constructor-based dependency injection.
     *
     * @param userDetailsService Custom implementation of UserDetailsService
     * @param authenticationSuccessHandler Handler for successful authentication
     * @param logoutSuccessHandler Handler for successful logout
     */
    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService,
                         CustomAuthenticationSuccessHandler authenticationSuccessHandler,
                         CustomLogoutSuccessHandler logoutSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    /**
     * Configure the SecurityFilterChain.
     * 
     * This method defines:
     * 1. Which URLs require authentication
     * 2. Which URLs are publicly accessible
     * 3. Login page configuration
     * 4. Logout behavior
     * 5. CSRF protection settings
     * 
     * @param http HttpSecurity object to configure
     * @return Configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configure URL authorization
            .authorizeHttpRequests(authorize -> authorize
                // Public URLs - accessible without authentication
                .requestMatchers(
                    "/register",           // Registration page
                    "/register/save",      // Registration form submission
                    "/login",              // Login page
                    "/css/**",             // Static CSS files
                    "/js/**",              // Static JavaScript files
                    "/images/**",          // Static images
                    "/h2-console/**"       // H2 database console (for development)
                ).permitAll()
                // Attendance endpoints - require authentication
                .requestMatchers(
                    "/attendance",
                    "/attendance/sign-in",
                    "/attendance/sign-out",
                    "/attendance/history"
                ).authenticated()
                // Admin-only attendance endpoints
                .requestMatchers(
                    "/attendance/admin",
                    "/attendance/admin/**"
                ).hasAuthority("ADMIN")
                // Disable security for JSP forwards (internal forwards should not be secured)
                .dispatcherTypeMatchers(
                    DispatcherType.FORWARD,
                    DispatcherType.ERROR
                ).permitAll()
                // All other URLs require authentication
                .anyRequest().authenticated()
            )
            
            // Configure form-based login with custom success handler
            .formLogin(form -> form
                .loginPage("/login")                    // Custom login page URL
                .loginProcessingUrl("/login")           // URL to submit login form (POST)
                .successHandler(authenticationSuccessHandler)  // Custom handler sends email notification
                .failureUrl("/login?error=true")        // Redirect after failed login with error parameter
                .usernameParameter("username")          // Form field name for username
                .passwordParameter("password")          // Form field name for password
                .permitAll()                            // Allow everyone to access login page
            )
            
            // Configure logout with custom success handler
            .logout(logout -> logout
                .logoutUrl("/logout")                   // URL to trigger logout (POST request)
                .logoutSuccessHandler(logoutSuccessHandler)  // Custom handler sends email notification
                .invalidateHttpSession(true)            // Invalidate HTTP session on logout
                .deleteCookies("JSESSIONID")            // Delete session cookie
                .clearAuthentication(true)              // Clear authentication
                .permitAll()                            // Allow everyone to logout
            )
            
            // Configure CSRF protection
            .csrf(csrf -> csrf
                // Disable CSRF for H2 console (development only)
                // CSRF is enabled for all other endpoints including login, logout, and registration
                .ignoringRequestMatchers("/h2-console/**")
            )
            
            // Configure session management
            .sessionManagement(session -> session
                .maximumSessions(1)                     // Allow only one session per user
                .maxSessionsPreventsLogin(false)        // New login invalidates old session
            )
            
            // Allow frames for H2 console (development only)
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            );

        return http.build();
    }

    /**
     * Create a PasswordEncoder bean using BCrypt.
     * 
     * BCrypt is a strong password hashing function that:
     * - Automatically generates a salt
     * - Is computationally expensive (resistant to brute-force attacks)
     * - Produces different hashes for the same password (due to random salt)
     * 
     * The default strength is 10 rounds, which provides good security
     * while maintaining reasonable performance.
     * 
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Create an AuthenticationProvider bean.
     * 
     * DaoAuthenticationProvider is Spring Security's default authentication provider.
     * It uses:
     * - UserDetailsService to load user details
     * - PasswordEncoder to verify passwords
     * 
     * This provider handles the authentication logic:
     * 1. Load user by username using UserDetailsService
     * 2. Compare provided password with stored hash using PasswordEncoder
     * 3. Return authentication result
     * 
     * @return Configured DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        
        // Set our custom UserDetailsService
        authProvider.setUserDetailsService(userDetailsService);
        
        // Set the password encoder (BCrypt)
        authProvider.setPasswordEncoder(passwordEncoder());
        
        return authProvider;
    }

    /**
     * Expose AuthenticationManager as a bean.
     * 
     * AuthenticationManager is the main interface for authentication in Spring Security.
     * It's needed for programmatic authentication (e.g., in controllers).
     * 
     * @param authConfig AuthenticationConfiguration from Spring Security
     * @return AuthenticationManager instance
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) 
            throws Exception {
        return authConfig.getAuthenticationManager();
    }
}

// Made with Bob
