package com.pahappa.app.repository;

import com.pahappa.app.entity.Attendance;
import com.pahappa.app.entity.Attendance.AttendanceStatus;
import com.pahappa.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Attendance entity.
 * 
 * This interface extends JpaRepository to provide CRUD operations
 * and custom query methods for attendance records.
 * 
 * @author Pahappa
 * @version 1.0
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    /**
     * Find all attendance records for a specific user
     * @param user The user to find attendance for
     * @return List of attendance records
     */
    List<Attendance> findByUserOrderBySignInTimeDesc(User user);

    /**
     * Find attendance records for a user on a specific date
     * @param user The user
     * @param date The date
     * @return List of attendance records
     */
    List<Attendance> findByUserAndAttendanceDate(User user, LocalDate date);

    /**
     * Find the most recent attendance record for a user
     * @param user The user
     * @return Optional containing the most recent attendance record
     */
    Optional<Attendance> findFirstByUserOrderBySignInTimeDesc(User user);

    /**
     * Find active (signed in) attendance record for a user
     * @param user The user
     * @param status The status (SIGNED_IN)
     * @return Optional containing the active attendance record
     */
    Optional<Attendance> findByUserAndStatus(User user, AttendanceStatus status);

    /**
     * Find all attendance records for a specific date
     * @param date The date
     * @return List of attendance records
     */
    List<Attendance> findByAttendanceDateOrderBySignInTimeDesc(LocalDate date);

    /**
     * Find all attendance records between two dates
     * @param startDate Start date
     * @param endDate End date
     * @return List of attendance records
     */
    @Query("SELECT a FROM Attendance a WHERE a.attendanceDate BETWEEN :startDate AND :endDate ORDER BY a.signInTime DESC")
    List<Attendance> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Find all attendance records for a user between two dates
     * @param user The user
     * @param startDate Start date
     * @param endDate End date
     * @return List of attendance records
     */
    @Query("SELECT a FROM Attendance a WHERE a.user = :user AND a.attendanceDate BETWEEN :startDate AND :endDate ORDER BY a.signInTime DESC")
    List<Attendance> findByUserAndDateRange(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Count attendance records for a user
     * @param user The user
     * @return Count of attendance records
     */
    long countByUser(User user);

    /**
     * Count attendance records for a user on a specific date
     * @param user The user
     * @param date The date
     * @return Count of attendance records
     */
    long countByUserAndAttendanceDate(User user, LocalDate date);

    /**
     * Find all attendance records with status SIGNED_IN (currently active)
     * @return List of active attendance records
     */
    List<Attendance> findByStatusOrderBySignInTimeDesc(AttendanceStatus status);

    /**
     * Check if user has signed in today
     * @param user The user
     * @param date Today's date
     * @return true if user has signed in today
     */
    /**
     * Count late arrivals for a user in a date range
     * @param user The user
     * @param startDate Start date
     * @param endDate End date
     * @return Count of late arrivals
     */
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.user = :user AND a.attendanceDate BETWEEN :startDate AND :endDate AND a.isLate = true")
    long countLateArrivalsByUserAndDateRange(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Count early departures for a user in a date range
     * @param user The user
     * @param startDate Start date
     * @param endDate End date
     * @return Count of early departures
     */
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.user = :user AND a.attendanceDate BETWEEN :startDate AND :endDate AND a.isEarlyDeparture = true")
    long countEarlyDeparturesByUserAndDateRange(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Find all late arrivals in a date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of late attendance records
     */
    @Query("SELECT a FROM Attendance a WHERE a.attendanceDate BETWEEN :startDate AND :endDate AND a.isLate = true ORDER BY a.signInTime DESC")
    List<Attendance> findLateArrivalsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    boolean existsByUserAndAttendanceDate(User user, LocalDate date);
}

// Made with Bob