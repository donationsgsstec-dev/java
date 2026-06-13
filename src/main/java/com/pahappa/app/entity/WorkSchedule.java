package com.pahappa.app.entity;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * WorkSchedule Entity class for configurable work hours.
 * 
 * This entity allows admins to set work start/end times,
 * grace periods, and other attendance policies.
 * 
 * @author Pahappa
 * @version 1.0
 */
@Entity
@Table(name = "work_schedule")
public class WorkSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the schedule (e.g., "Default Schedule", "Flexible Hours")
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Work start time (e.g., 09:00)
     */
    @Column(name = "work_start_time", nullable = false)
    private LocalTime workStartTime;

    /**
     * Work end time (e.g., 17:00)
     */
    @Column(name = "work_end_time", nullable = false)
    private LocalTime workEndTime;

    /**
     * Grace period in minutes before marking as late
     */
    @Column(name = "grace_period_minutes", nullable = false)
    private Integer gracePeriodMinutes = 15;

    /**
     * Whether this is the active schedule
     */
    @Column(nullable = false)
    private boolean active = true;

    /**
     * Days of week this schedule applies to (comma-separated: MON,TUE,WED,THU,FRI)
     */
    @Column(name = "applicable_days", length = 100)
    private String applicableDays = "MON,TUE,WED,THU,FRI";

    /**
     * Minimum work hours per day
     */
    @Column(name = "minimum_work_hours")
    private Double minimumWorkHours = 8.0;

    /**
     * Whether to send email notifications for late arrivals
     */
    @Column(name = "notify_late_arrival", nullable = false)
    private boolean notifyLateArrival = true;

    /**
     * Whether to send email notifications for early departures
     */
    @Column(name = "notify_early_departure", nullable = false)
    private boolean notifyEarlyDeparture = true;

    /**
     * Timestamp when created
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when last updated
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Default constructor
     */
    public WorkSchedule() {
    }

    /**
     * Constructor with essential fields
     */
    public WorkSchedule(String name, LocalTime workStartTime, LocalTime workEndTime) {
        this.name = name;
        this.workStartTime = workStartTime;
        this.workEndTime = workEndTime;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getWorkStartTime() {
        return workStartTime;
    }

    public void setWorkStartTime(LocalTime workStartTime) {
        this.workStartTime = workStartTime;
    }

    public LocalTime getWorkEndTime() {
        return workEndTime;
    }

    public void setWorkEndTime(LocalTime workEndTime) {
        this.workEndTime = workEndTime;
    }

    public Integer getGracePeriodMinutes() {
        return gracePeriodMinutes;
    }

    public void setGracePeriodMinutes(Integer gracePeriodMinutes) {
        this.gracePeriodMinutes = gracePeriodMinutes;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getApplicableDays() {
        return applicableDays;
    }

    public void setApplicableDays(String applicableDays) {
        this.applicableDays = applicableDays;
    }

    public Double getMinimumWorkHours() {
        return minimumWorkHours;
    }

    public void setMinimumWorkHours(Double minimumWorkHours) {
        this.minimumWorkHours = minimumWorkHours;
    }

    public boolean isNotifyLateArrival() {
        return notifyLateArrival;
    }

    public void setNotifyLateArrival(boolean notifyLateArrival) {
        this.notifyLateArrival = notifyLateArrival;
    }

    public boolean isNotifyEarlyDeparture() {
        return notifyEarlyDeparture;
    }

    public void setNotifyEarlyDeparture(boolean notifyEarlyDeparture) {
        this.notifyEarlyDeparture = notifyEarlyDeparture;
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
        return "WorkSchedule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", workStartTime=" + workStartTime +
                ", workEndTime=" + workEndTime +
                ", gracePeriodMinutes=" + gracePeriodMinutes +
                ", active=" + active +
                '}';
    }
}

// Made with Bob