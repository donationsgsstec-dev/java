package com.pahappa.app.service;

import com.pahappa.app.entity.Attendance;
import com.pahappa.app.entity.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service for exporting attendance data to Excel.
 * 
 * Generates Excel reports with attendance statistics and details.
 * 
 * @author Pahappa
 * @version 1.0
 */
@Service
public class ExcelExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Export attendance records to Excel
     * @param attendanceList List of attendance records
     * @param title Report title
     * @return Excel file as byte array
     * @throws IOException if export fails
     */
    public byte[] exportAttendanceToExcel(List<Attendance> attendanceList, String title) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Attendance Report");

            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle timeStyle = createTimeStyle(workbook);

            // Create title row
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(title);
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);
            titleStyle.setFont(titleFont);
            titleCell.setCellStyle(titleStyle);

            // Create header row
            Row headerRow = sheet.createRow(2);
            String[] headers = {
                "ID", "Employee Name", "Username", "Date", "Check-In Time", 
                "Check-Out Time", "Duration", "Status", "Late", "Late Minutes",
                "Early Departure", "Overtime", "Notes"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Fill data rows
            int rowNum = 3;
            for (Attendance attendance : attendanceList) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(attendance.getId());
                row.createCell(1).setCellValue(attendance.getUser().getFullName());
                row.createCell(2).setCellValue(attendance.getUser().getUsername());
                
                Cell dateCell = row.createCell(3);
                dateCell.setCellValue(attendance.getAttendanceDate().format(DATE_FORMATTER));
                dateCell.setCellStyle(dateStyle);
                
                Cell checkInCell = row.createCell(4);
                checkInCell.setCellValue(attendance.getSignInTime().format(TIME_FORMATTER));
                checkInCell.setCellStyle(timeStyle);
                
                if (attendance.getSignOutTime() != null) {
                    Cell checkOutCell = row.createCell(5);
                    checkOutCell.setCellValue(attendance.getSignOutTime().format(TIME_FORMATTER));
                    checkOutCell.setCellStyle(timeStyle);
                    row.createCell(6).setCellValue(attendance.getFormattedDuration());
                } else {
                    row.createCell(5).setCellValue("Not signed out");
                    row.createCell(6).setCellValue("N/A");
                }
                
                row.createCell(7).setCellValue(attendance.getStatus().toString());
                row.createCell(8).setCellValue(attendance.isLate() ? "Yes" : "No");
                row.createCell(9).setCellValue(attendance.getLateMinutes() != null ? 
                    attendance.getLateMinutes() : 0);
                row.createCell(10).setCellValue(attendance.isEarlyDeparture() ? "Yes" : "No");
                row.createCell(11).setCellValue(attendance.getOvertimeMinutes() != null ? 
                    attendance.getOvertimeMinutes() : 0);
                row.createCell(12).setCellValue(attendance.getNotes() != null ? 
                    attendance.getNotes() : "");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * Export user attendance summary to Excel
     * @param user User
     * @param attendanceList User's attendance records
     * @return Excel file as byte array
     * @throws IOException if export fails
     */
    public byte[] exportUserAttendanceSummary(User user, List<Attendance> attendanceList) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("My Attendance");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle timeStyle = createTimeStyle(workbook);

            // User info
            int rowNum = 0;
            Row userRow = sheet.createRow(rowNum++);
            userRow.createCell(0).setCellValue("Employee:");
            userRow.createCell(1).setCellValue(user.getFullName());
            
            Row usernameRow = sheet.createRow(rowNum++);
            usernameRow.createCell(0).setCellValue("Username:");
            usernameRow.createCell(1).setCellValue(user.getUsername());
            
            Row emailRow = sheet.createRow(rowNum++);
            emailRow.createCell(0).setCellValue("Email:");
            emailRow.createCell(1).setCellValue(user.getEmail());

            // Statistics
            rowNum++;
            Row statsHeaderRow = sheet.createRow(rowNum++);
            statsHeaderRow.createCell(0).setCellValue("ATTENDANCE STATISTICS");
            
            Row totalDaysRow = sheet.createRow(rowNum++);
            totalDaysRow.createCell(0).setCellValue("Total Days Attended:");
            totalDaysRow.createCell(1).setCellValue(attendanceList.size());
            
            long lateCount = attendanceList.stream().filter(Attendance::isLate).count();
            Row lateRow = sheet.createRow(rowNum++);
            lateRow.createCell(0).setCellValue("Late Arrivals:");
            lateRow.createCell(1).setCellValue(lateCount);

            // Attendance details
            rowNum += 2;
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {"Date", "Check-In", "Check-Out", "Duration", "Status", "Late", "Notes"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            for (Attendance attendance : attendanceList) {
                Row row = sheet.createRow(rowNum++);
                
                Cell dateCell = row.createCell(0);
                dateCell.setCellValue(attendance.getAttendanceDate().format(DATE_FORMATTER));
                dateCell.setCellStyle(dateStyle);
                
                Cell checkInCell = row.createCell(1);
                checkInCell.setCellValue(attendance.getSignInTime().format(TIME_FORMATTER));
                checkInCell.setCellStyle(timeStyle);
                
                if (attendance.getSignOutTime() != null) {
                    Cell checkOutCell = row.createCell(2);
                    checkOutCell.setCellValue(attendance.getSignOutTime().format(TIME_FORMATTER));
                    checkOutCell.setCellStyle(timeStyle);
                    row.createCell(3).setCellValue(attendance.getFormattedDuration());
                } else {
                    row.createCell(2).setCellValue("Not signed out");
                    row.createCell(3).setCellValue("N/A");
                }
                
                row.createCell(4).setCellValue(attendance.getStatus().toString());
                row.createCell(5).setCellValue(attendance.isLate() ? 
                    attendance.getFormattedLateTime() : "On time");
                row.createCell(6).setCellValue(attendance.getNotes() != null ? 
                    attendance.getNotes() : "");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * Create header cell style
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    /**
     * Create date cell style
     */
    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("yyyy-mm-dd"));
        return style;
    }

    /**
     * Create time cell style
     */
    private CellStyle createTimeStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("hh:mm:ss"));
        return style;
    }
}

// Made with Bob