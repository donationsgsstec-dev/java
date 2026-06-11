package com.pahappa.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.Duration;

/**
 * Attendance Entity class representing student attendance records.
 * 
 * This JPA entity maps to the 'attendance' table in the database.
 * It tracks when students sign in and sign out.
 * 
 * @author Pahappa
 * @version 1.0
 */
@Entity
@Table(name = "attendance")
public class Attendance {

    /**
     * Primary key - auto-generated attendance ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the User (student) who signed in
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Timestamp when the student signed in
     */
    @Column(name = "sign_in_time", nullable = false)
    private LocalDateTime signInTime;

    /**
     * Timestamp when the student signed out
     */
    @Column(name = "sign_out_time")
    private LocalDateTime signOutTime;

    /**
     * Date of the attendance record (for easy querying)
     */
    @Column(name = "attendance_date", nullable = false)
    private java.time.LocalDate attendanceDate;

    /**
     * Status of the attendance record
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status = AttendanceStatus.SIGNED_IN;

    /**
     * Notes or remarks about the attendance
     */
    @Column(length = 500)
    private String notes;

    /**
     * Timestamp when the record was created
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the record was last updated
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Attendance status enum
     */
    public enum AttendanceStatus {
        SIGNED_IN,
        SIGNED_OUT
    }

    /**
     * Default constructor required by JPA
     */
    public Attendance() {
    }

    /**
     * Constructor with essential fields
     */
    public Attendance(User user, LocalDateTime signInTime) {
        this.user = user;
        this.signInTime = signInTime;
        this.attendanceDate = signInTime.toLocalDate();
        this.status = AttendanceStatus.SIGNED_IN;
    }

    /**
     * JPA callback method - executed before persisting a new entity
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * JPA callback method - executed before updating an entity
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Calculate duration between sign in and sign out
     * @return Duration or null if not signed out yet
     */
    public Duration getDuration() {
        if (signOutTime != null) {
            return Duration.between(signInTime, signOutTime);
        }
        return null;
    }

    /**
     * Get formatted duration string
     * @return Formatted duration (e.g., "8h 30m") or "Still signed in"
     */
    public String getFormattedDuration() {
        if (signOutTime == null) {
            return "Still signed in";
        }
        Duration duration = getDuration();
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return String.format("%dh %dm", hours, minutes);
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getSignInTime() {
        return signInTime;
    }

    public void setSignInTime(LocalDateTime signInTime) {
        this.signInTime = signInTime;
    }

    public LocalDateTime getSignOutTime() {
        return signOutTime;
    }

    public void setSignOutTime(LocalDateTime signOutTime) {
        this.signOutTime = signOutTime;
    }

    public java.time.LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(java.time.LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", signInTime=" + signInTime +
                ", signOutTime=" + signOutTime +
                ", status=" + status +
                ", attendanceDate=" + attendanceDate +
                '}';
    }
}

// Made with Bob