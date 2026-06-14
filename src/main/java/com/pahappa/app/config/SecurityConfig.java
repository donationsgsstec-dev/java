package com.pahappa.app.config;

import com.pahappa.app.service.impl.CustomUserDetailsService;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
     * Admin SecurityFilterChain — evaluated first (Order 1).
     *
     * Handles all requests under /admin/**:
     * - Uses /admin/login as its own login page.
     * - On success, verifies the ADMIN authority and redirects to the admin
     *   dashboard; non-admin credentials are rejected back to /admin/login.
     * - Shares the same logout, CSRF, and session settings as the main chain.
     *
     * @param http HttpSecurity object to configure
     * @return Configured SecurityFilterChain for admin paths
     * @throws Exception if configuration fails
     */
    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // This chain handles both /admin/** and /attendance/admin/** requests
            .securityMatcher("/admin/**", "/attendance/admin", "/attendance/admin/**")

            // URL authorization for admin paths
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/admin/login").permitAll()   // Admin login page is public
                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                .anyRequest().hasAuthority("ADMIN")            // Everything else requires ADMIN
            )

            // Redirect unauthenticated requests to the admin login page (not the default /login)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) ->
                    response.sendRedirect(request.getContextPath() + "/admin/login")
                )
            )

            // Admin login form
            .formLogin(form -> form
                .loginPage("/admin/login")              // Admin login page URL
                .loginProcessingUrl("/admin/login")     // POST target for the admin login form
                .successHandler((request, response, authentication) -> {
                    // Verify the authenticated user actually has the ADMIN role.
                    // This prevents a regular user who knows the admin URL from logging in.
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));

                    if (isAdmin) {
                        response.sendRedirect(request.getContextPath() + "/attendance/admin");
                    } else {
                        // Authenticated but not an admin — boot them back with an error
                        response.sendRedirect(request.getContextPath() + "/admin/login?error=true");
                    }
                })
                .failureUrl("/admin/login?error=true")  // Redirect on bad credentials
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )

            // Logout — mirrors the main chain
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .permitAll()
            )

            // CSRF — same exclusions as main chain
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**", "/attendance/qr/**")
            )
            // Session management
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            );

        return http.build();
    }

    /**
     * Default SecurityFilterChain — evaluated second (Order 2).
     *
     * Handles all non-admin requests:
     * - Uses /login as the login page.
     * - On success delegates to CustomAuthenticationSuccessHandler which sends
     *   a login-notification email and redirects to /home.
     *
     * @param http HttpSecurity object to configure
     * @return Configured SecurityFilterChain for general paths
     * @throws Exception if configuration fails
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configure URL authorization
            .authorizeHttpRequests(authorize -> authorize
                // Public URLs - accessible without authentication
                .requestMatchers(
                    "/register",           // Registration page
                    "/register/save",      // Registration form submission
                    "/login",              // Login page
                    "/forgot-password",    // Forgot password page
                    "/attendance/qr/scan", // Public QR code scan endpoint
                    "/attendance/qr/room-scan", // Public room QR code scan endpoint
                    "/css/**",             // Static CSS files
                    "/js/**",              // Static JavaScript files
                    "/images/**",          // Static images
                    "/h2-console/**",      // H2 database console (for development)
                    "/actuator/**"         // Actuator endpoints (e.g. /actuator/health for Railway healthcheck)
                ).permitAll()
                // Attendance endpoints - require authentication
                .requestMatchers(
                    "/attendance",
                    "/attendance/sign-in",
                    "/attendance/sign-out",
                    "/attendance/history",
                    "/attendance/qr/**",
                    "/attendance/statistics",
                    "/attendance/calendar"
                ).authenticated()
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
                .loginPage("/login")                               // Custom login page URL
                .loginProcessingUrl("/login")                      // URL to submit login form (POST)
                .successHandler(authenticationSuccessHandler)      // Sends email notification + redirects /home
                .failureUrl("/login?error=true")                   // Redirect after failed login
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )

            // Configure logout with custom success handler
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)        // Sends email notification on logout
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .permitAll()
            )

            // Configure CSRF protection
            .csrf(csrf -> csrf
                // Disable CSRF for H2 console and QR endpoints (development only)
                .ignoringRequestMatchers("/h2-console/**", "/attendance/qr/**")
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
