-- =====================================================
-- SUPER ATTENDANCE SYSTEM - DATABASE SCHEMA UPDATE
-- =====================================================
-- This script updates the existing database schema with
-- new features for the enhanced attendance system
-- =====================================================

-- Add new columns to attendance table
ALTER TABLE attendance 
ADD COLUMN is_late BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN late_minutes INT,
ADD COLUMN is_early_departure BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN early_departure_minutes INT,
ADD COLUMN overtime_minutes INT,
ADD COLUMN check_in_location VARCHAR(200),
ADD COLUMN check_out_location VARCHAR(200),
ADD COLUMN qr_code_used BOOLEAN NOT NULL DEFAULT FALSE;

-- Update attendance status enum to include new statuses
-- Note: This depends on your database. For MySQL 8.0+:
ALTER TABLE attendance MODIFY COLUMN status 
ENUM('SIGNED_IN', 'SIGNED_OUT', 'ABSENT', 'LATE', 'ON_LEAVE') NOT NULL;

-- Add QR code column to users table
ALTER TABLE users 
ADD COLUMN qr_code VARCHAR(500) UNIQUE;

-- Create work_schedule table
CREATE TABLE IF NOT EXISTS work_schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    work_start_time TIME NOT NULL,
    work_end_time TIME NOT NULL,
    grace_period_minutes INT NOT NULL DEFAULT 15,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    applicable_days VARCHAR(100) DEFAULT 'MON,TUE,WED,THU,FRI',
    minimum_work_hours DOUBLE DEFAULT 8.0,
    notify_late_arrival BOOLEAN NOT NULL DEFAULT TRUE,
    notify_early_departure BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert default work schedule
INSERT INTO work_schedule (name, work_start_time, work_end_time, grace_period_minutes, active)
VALUES ('Default Schedule', '09:00:00', '17:00:00', 15, TRUE);

-- Create indexes for better performance
CREATE INDEX idx_attendance_late ON attendance(is_late);
CREATE INDEX idx_attendance_early_departure ON attendance(is_early_departure);
CREATE INDEX idx_attendance_date_user ON attendance(attendance_date, user_id);
CREATE INDEX idx_work_schedule_active ON work_schedule(active);

-- =====================================================
-- SAMPLE DATA FOR TESTING (Optional)
-- =====================================================

-- Update existing attendance records to set default values
UPDATE attendance 
SET is_late = FALSE, 
    is_early_departure = FALSE, 
    qr_code_used = FALSE 
WHERE is_late IS NULL;

-- =====================================================
-- VERIFICATION QUERIES
-- =====================================================

-- Verify attendance table structure
-- SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT
-- FROM INFORMATION_SCHEMA.COLUMNS
-- WHERE TABLE_NAME = 'attendance' AND TABLE_SCHEMA = 'userdb';

-- Verify work_schedule table
-- SELECT * FROM work_schedule;

-- Check attendance with late arrivals
-- SELECT u.username, a.attendance_date, a.sign_in_time, a.is_late, a.late_minutes
-- FROM attendance a
-- JOIN users u ON a.user_id = u.id
-- WHERE a.is_late = TRUE
-- ORDER BY a.attendance_date DESC;

-- =====================================================
-- ROLLBACK SCRIPT (Use with caution!)
-- =====================================================
-- Uncomment the following lines to rollback changes

-- DROP TABLE IF EXISTS work_schedule;
-- ALTER TABLE users DROP COLUMN qr_code;
-- ALTER TABLE attendance 
--   DROP COLUMN is_late,
--   DROP COLUMN late_minutes,
--   DROP COLUMN is_early_departure,
--   DROP COLUMN early_departure_minutes,
--   DROP COLUMN overtime_minutes,
--   DROP COLUMN check_in_location,
--   DROP COLUMN check_out_location,
--   DROP COLUMN qr_code_used;

-- =====================================================
-- END OF SCHEMA UPDATE
-- =====================================================

-- Made with Bob
