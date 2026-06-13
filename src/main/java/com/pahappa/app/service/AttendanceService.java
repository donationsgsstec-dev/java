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
    /**
     * Sign in a user using QR code
     * @param qrCodeData QR code data
     * @return The created attendance record
     * @throws IllegalStateException if user is already signed in or QR code is invalid
     */
    Attendance signInWithQRCode(String qrCodeData);

    /**
     * Sign out a user using QR code
     * @param qrCodeData QR code data
     * @return The updated attendance record
     * @throws IllegalStateException if user is not signed in or QR code is invalid
     */
    Attendance signOutWithQRCode(String qrCodeData);

    /**
     * Get attendance statistics for a user
     * @param user The user
     * @return AttendanceStatistics object
     */
    com.pahappa.app.dto.AttendanceStatistics getUserStatistics(User user);

    /**
     * Get attendance calendar data for a month
     * @param user The user
     * @param year Year
     * @param month Month (1-12)
     * @return Map of date to attendance status
     */
    java.util.Map<LocalDate, String> getMonthlyCalendar(User user, int year, int month);

    /**
     * Get late arrivals count for a user
     * @param user The user
     * @param startDate Start date
     * @param endDate End date
     * @return Count of late arrivals
     */
    long getLateArrivalsCount(User user, LocalDate startDate, LocalDate endDate);

    /**
     * Get early departures count for a user
     * @param user The user
     * @param startDate Start date
     * @param endDate End date
     * @return Count of early departures
     */
    long getEarlyDeparturesCount(User user, LocalDate startDate, LocalDate endDate);

    /**
     * Calculate total hours worked
     * @param user The user
     * @param startDate Start date
     * @param endDate End date
     * @return Total hours worked
     */
    double getTotalHoursWorked(User user, LocalDate startDate, LocalDate endDate);

    /**
     * Calculate overtime hours
     * @param user The user
     * @param startDate Start date
     * @param endDate End date
     * @return Total overtime hours
     */
    double getOvertimeHours(User user, LocalDate startDate, LocalDate endDate);

    /**
     * Get attendance rate percentage
     * @param user The user
     * @param startDate Start date
     * @param endDate End date
     * @return Attendance rate (0-100)
     */
    double getAttendanceRate(User user, LocalDate startDate, LocalDate endDate);

    /**
     * Export attendance to Excel
     * @param attendanceList List of attendance records
     * @param title Report title
     * @return Excel file as byte array
     */
    byte[] exportToExcel(List<Attendance> attendanceList, String title) throws java.io.IOException;

    /**
     * Export user attendance to Excel
     * @param user The user
     * @return Excel file as byte array
     */
    byte[] exportUserAttendanceToExcel(User user) throws java.io.IOException;

    boolean hasSignedInToday(User user);
}

// Made with Bob