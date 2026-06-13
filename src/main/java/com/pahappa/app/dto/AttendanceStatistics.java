package com.pahappa.app.dto;

import java.time.LocalDate;
import java.util.Map;

/**
 * Data Transfer Object for Attendance Statistics.
 * 
 * Used to display dashboard statistics and analytics.
 * 
 * @author Pahappa
 * @version 1.0
 */
public class AttendanceStatistics {

    private Long userId;
    private String userName;
    
    // Weekly statistics
    private double totalHoursThisWeek;
    private int daysAttendedThisWeek;
    private int lateArrivalsThisWeek;
    private int earlyDeparturesThisWeek;
    
    // Monthly statistics
    private double totalHoursThisMonth;
    private int daysAttendedThisMonth;
    private int lateArrivalsThisMonth;
    private int earlyDeparturesThisMonth;
    private double overtimeHoursThisMonth;
    
    // Attendance rate
    private double attendanceRateWeek;
    private double attendanceRateMonth;
    
    // Current status
    private boolean currentlySignedIn;
    private String currentSignInTime;
    
    // Trends (day of week -> attendance count)
    private Map<String, Integer> weeklyTrend;
    
    // Average times
    private String averageCheckInTime;
    private String averageCheckOutTime;
    private double averageDailyHours;
    
    // Date range
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private LocalDate monthStartDate;
    private LocalDate monthEndDate;

    // Constructors
    public AttendanceStatistics() {
    }

    public AttendanceStatistics(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getTotalHoursThisWeek() {
        return totalHoursThisWeek;
    }

    public void setTotalHoursThisWeek(double totalHoursThisWeek) {
        this.totalHoursThisWeek = totalHoursThisWeek;
    }

    public int getDaysAttendedThisWeek() {
        return daysAttendedThisWeek;
    }

    public void setDaysAttendedThisWeek(int daysAttendedThisWeek) {
        this.daysAttendedThisWeek = daysAttendedThisWeek;
    }

    public int getLateArrivalsThisWeek() {
        return lateArrivalsThisWeek;
    }

    public void setLateArrivalsThisWeek(int lateArrivalsThisWeek) {
        this.lateArrivalsThisWeek = lateArrivalsThisWeek;
    }

    public int getEarlyDeparturesThisWeek() {
        return earlyDeparturesThisWeek;
    }

    public void setEarlyDeparturesThisWeek(int earlyDeparturesThisWeek) {
        this.earlyDeparturesThisWeek = earlyDeparturesThisWeek;
    }

    public double getTotalHoursThisMonth() {
        return totalHoursThisMonth;
    }

    public void setTotalHoursThisMonth(double totalHoursThisMonth) {
        this.totalHoursThisMonth = totalHoursThisMonth;
    }

    public int getDaysAttendedThisMonth() {
        return daysAttendedThisMonth;
    }

    public void setDaysAttendedThisMonth(int daysAttendedThisMonth) {
        this.daysAttendedThisMonth = daysAttendedThisMonth;
    }

    public int getLateArrivalsThisMonth() {
        return lateArrivalsThisMonth;
    }

    public void setLateArrivalsThisMonth(int lateArrivalsThisMonth) {
        this.lateArrivalsThisMonth = lateArrivalsThisMonth;
    }

    public int getEarlyDeparturesThisMonth() {
        return earlyDeparturesThisMonth;
    }

    public void setEarlyDeparturesThisMonth(int earlyDeparturesThisMonth) {
        this.earlyDeparturesThisMonth = earlyDeparturesThisMonth;
    }

    public double getOvertimeHoursThisMonth() {
        return overtimeHoursThisMonth;
    }

    public void setOvertimeHoursThisMonth(double overtimeHoursThisMonth) {
        this.overtimeHoursThisMonth = overtimeHoursThisMonth;
    }

    public double getAttendanceRateWeek() {
        return attendanceRateWeek;
    }

    public void setAttendanceRateWeek(double attendanceRateWeek) {
        this.attendanceRateWeek = attendanceRateWeek;
    }

    public double getAttendanceRateMonth() {
        return attendanceRateMonth;
    }

    public void setAttendanceRateMonth(double attendanceRateMonth) {
        this.attendanceRateMonth = attendanceRateMonth;
    }

    public boolean isCurrentlySignedIn() {
        return currentlySignedIn;
    }

    public void setCurrentlySignedIn(boolean currentlySignedIn) {
        this.currentlySignedIn = currentlySignedIn;
    }

    public String getCurrentSignInTime() {
        return currentSignInTime;
    }

    public void setCurrentSignInTime(String currentSignInTime) {
        this.currentSignInTime = currentSignInTime;
    }

    public Map<String, Integer> getWeeklyTrend() {
        return weeklyTrend;
    }

    public void setWeeklyTrend(Map<String, Integer> weeklyTrend) {
        this.weeklyTrend = weeklyTrend;
    }

    public String getAverageCheckInTime() {
        return averageCheckInTime;
    }

    public void setAverageCheckInTime(String averageCheckInTime) {
        this.averageCheckInTime = averageCheckInTime;
    }

    public String getAverageCheckOutTime() {
        return averageCheckOutTime;
    }

    public void setAverageCheckOutTime(String averageCheckOutTime) {
        this.averageCheckOutTime = averageCheckOutTime;
    }

    public double getAverageDailyHours() {
        return averageDailyHours;
    }

    public void setAverageDailyHours(double averageDailyHours) {
        this.averageDailyHours = averageDailyHours;
    }

    public LocalDate getWeekStartDate() {
        return weekStartDate;
    }

    public void setWeekStartDate(LocalDate weekStartDate) {
        this.weekStartDate = weekStartDate;
    }

    public LocalDate getWeekEndDate() {
        return weekEndDate;
    }

    public void setWeekEndDate(LocalDate weekEndDate) {
        this.weekEndDate = weekEndDate;
    }

    public LocalDate getMonthStartDate() {
        return monthStartDate;
    }

    public void setMonthStartDate(LocalDate monthStartDate) {
        this.monthStartDate = monthStartDate;
    }

    public LocalDate getMonthEndDate() {
        return monthEndDate;
    }

    public void setMonthEndDate(LocalDate monthEndDate) {
        this.monthEndDate = monthEndDate;
    }

    @Override
    public String toString() {
        return "AttendanceStatistics{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", totalHoursThisWeek=" + totalHoursThisWeek +
                ", daysAttendedThisWeek=" + daysAttendedThisWeek +
                ", attendanceRateWeek=" + attendanceRateWeek +
                ", totalHoursThisMonth=" + totalHoursThisMonth +
                ", daysAttendedThisMonth=" + daysAttendedThisMonth +
                ", attendanceRateMonth=" + attendanceRateMonth +
                '}';
    }
}

// Made with Bob