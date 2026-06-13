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
     * Whether the check-in was late
     */
    @Column(name = "is_late", nullable = false)
    private boolean isLate = false;

    /**
     * Minutes late (if applicable)
     */
    @Column(name = "late_minutes")
    private Integer lateMinutes;

    /**
     * Whether the check-out was early
     */
    @Column(name = "is_early_departure", nullable = false)
    private boolean isEarlyDeparture = false;

    /**
     * Minutes early departure (if applicable)
     */
    @Column(name = "early_departure_minutes")
    private Integer earlyDepartureMinutes;

    /**
     * Overtime hours worked
     */
    @Column(name = "overtime_minutes")
    private Integer overtimeMinutes;

    /**
     * Location of check-in (optional)
     */
    @Column(name = "check_in_location", length = 200)
    private String checkInLocation;

    /**
     * Location of check-out (optional)
     */
    @Column(name = "check_out_location", length = 200)
    private String checkOutLocation;

    /**
     * QR code used for check-in
     */
    @Column(name = "qr_code_used")
    private boolean qrCodeUsed = false;

    /**
     * Attendance status enum
     */
    public enum AttendanceStatus {
        SIGNED_IN,
        SIGNED_OUT,
        ABSENT,
        LATE,
        ON_LEAVE
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
    
    public boolean isLate() {
        return isLate;
    }

    public void setLate(boolean late) {
        isLate = late;
    }

    public Integer getLateMinutes() {
        return lateMinutes;
    }

    public void setLateMinutes(Integer lateMinutes) {
        this.lateMinutes = lateMinutes;
    }

    public boolean isEarlyDeparture() {
        return isEarlyDeparture;
    }

    public void setEarlyDeparture(boolean earlyDeparture) {
        isEarlyDeparture = earlyDeparture;
    }

    public Integer getEarlyDepartureMinutes() {
        return earlyDepartureMinutes;
    }

    public void setEarlyDepartureMinutes(Integer earlyDepartureMinutes) {
        this.earlyDepartureMinutes = earlyDepartureMinutes;
    }

    public Integer getOvertimeMinutes() {
        return overtimeMinutes;
    }

    public void setOvertimeMinutes(Integer overtimeMinutes) {
        this.overtimeMinutes = overtimeMinutes;
    }

    public String getCheckInLocation() {
        return checkInLocation;
    }

    public void setCheckInLocation(String checkInLocation) {
        this.checkInLocation = checkInLocation;
    }

    public String getCheckOutLocation() {
        return checkOutLocation;
    }

    public void setCheckOutLocation(String checkOutLocation) {
        this.checkOutLocation = checkOutLocation;
    }

    public boolean isQrCodeUsed() {
        return qrCodeUsed;
    }

    public void setQrCodeUsed(boolean qrCodeUsed) {
        this.qrCodeUsed = qrCodeUsed;
    }

    /**
     * Get formatted late time
     * @return Formatted late time or empty string
     */
    public String getFormattedLateTime() {
        if (lateMinutes == null || lateMinutes == 0) {
            return "";
        }
        long hours = lateMinutes / 60;
        long minutes = lateMinutes % 60;
        if (hours > 0) {
            return String.format("%dh %dm late", hours, minutes);
        }
        return String.format("%dm late", minutes);
    }

    /**
     * Get formatted overtime
     * @return Formatted overtime or empty string
     */
    public String getFormattedOvertime() {
        if (overtimeMinutes == null || overtimeMinutes == 0) {
            return "";
        }
        long hours = overtimeMinutes / 60;
        long minutes = overtimeMinutes % 60;
        if (hours > 0) {
            return String.format("%dh %dm overtime", hours, minutes);
        }
        return String.format("%dm overtime", minutes);
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