package com.pahappa.app.service;

import com.pahappa.app.entity.Attendance;
import com.pahappa.app.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Attendance operations.
 * 
 * This interface defines the business logic methods for managing
 * student attendance records.
 * 
 * @author Pahappa
 * @version 1.0
 */
public interface AttendanceService {

    /**
     * Sign in a user (create new attendance record)
     * @param user The user signing in
     * @return The created attendance record
     * @throws IllegalStateException if user is already signed in
     */
    Attendance signIn(User user);

    /**
     * Sign out a user (update existing attendance record)
     * @param user The user signing out
     * @return The updated attendance record
     * @throws IllegalStateException if user is not signed in
     */
    Attendance signOut(User user);

    /**
     * Get current active attendance record for a user
     * @param user The user
     * @return Optional containing active attendance record
     */
    Optional<Attendance> getCurrentAttendance(User user);

    /**
     * Check if user is currently signed in
     * @param user The user
     * @return true if user is signed in
     */
    boolean isUserSignedIn(User user);

    /**
     * Get all attendance records for a user
     * @param user The user
     * @return List of attendance records
     */
    List<Attendance> getUserAttendanceHistory(User user);

    /**
     * Get attendance records for a user on a specific date
     * @param user The user
     * @param date The date
     * @return List of attendance records
     */
    List<Attendance> getUserAttendanceByDate(User user, LocalDate date);

    /**
     * Get all attendance records for a specific date
     * @param date The date
     * @return List of attendance records
     */
    List<Attendance> getAttendanceByDate(LocalDate date);

    /**
     * Get all attendance records between two dates
     * @param startDate Start date
     * @param endDate End date
     * @return List of attendance records
     */
    List<Attendance> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Get attendance records for a user between two dates
     * @param user The user
     * @param startDate Start date
     * @param endDate End date
     * @return List of attendance records
     */
    List<Attendance> getUserAttendanceByDateRange(User user, LocalDate startDate, LocalDate endDate);

    /**
     * Get all currently signed in users
     * @return List of active attendance records
     */
    List<Attendance> getCurrentlySignedInUsers();

    /**
     * Get attendance record by ID
     * @param id The attendance ID
     * @return Optional containing the attendance record
     */
    Optional<Attendance> getAttendanceById(Long id);

    /**
     * Get total attendance count for a user
     * @param user The user
     * @return Count of attendance records
     */
    long getUserAttendanceCount(User user);

    /**
     * Check if user has signed in today
     * @param user The user
     * @return true if user has signed in today
     */
    boolean hasSignedInToday(User user);
}

// Made with Bob