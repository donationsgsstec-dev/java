-- Check and Fix Attendance Report Data Issues
-- Run this script to diagnose and fix missing column data

-- ============================================
-- STEP 1: Check if Work Schedule exists
-- ============================================
SELECT 
    'Work Schedule Status' as check_type,
    COUNT(*) as total_schedules,
    SUM(CASE WHEN is_active = 1 THEN 1 ELSE 0 END) as active_schedules
FROM work_schedule;

-- Show work schedule details
SELECT 
    id,
    work_start_time,
    work_end_time,
    grace_period_minutes,
    is_active,
    created_at
FROM work_schedule
ORDER BY created_at DESC;

-- ============================================
-- STEP 2: Check current attendance data
-- ============================================
SELECT 
    'Attendance Data Status' as check_type,
    COUNT(*) as total_records,
    SUM(CASE WHEN is_late = 1 THEN 1 ELSE 0 END) as records_with_late,
    SUM(CASE WHEN late_minutes IS NOT NULL AND late_minutes > 0 THEN 1 ELSE 0 END) as records_with_late_minutes,
    SUM(CASE WHEN is_early_departure = 1 THEN 1 ELSE 0 END) as records_with_early_departure,
    SUM(CASE WHEN overtime_minutes IS NOT NULL AND overtime_minutes > 0 THEN 1 ELSE 0 END) as records_with_overtime,
    SUM(CASE WHEN notes IS NOT NULL AND notes != '' THEN 1 ELSE 0 END) as records_with_notes
FROM attendance;

-- Show sample attendance records
SELECT 
    id,
    user_id,
    attendance_date,
    sign_in_time,
    sign_out_time,
    status,
    is_late,
    late_minutes,
    is_early_departure,
    early_departure_minutes,
    overtime_minutes,
    notes
FROM attendance
ORDER BY attendance_date DESC, sign_in_time DESC
LIMIT 10;

-- ============================================
-- STEP 3: Create default work schedule if missing
-- ============================================
-- Uncomment and run this if no active work schedule exists
/*
INSERT INTO work_schedule (
    work_start_time,
    work_end_time,
    grace_period_minutes,
    is_active,
    notify_late_arrival,
    notify_early_departure,
    created_at,
    updated_at
) VALUES (
    '09:00:00',  -- Work starts at 9 AM
    '17:00:00',  -- Work ends at 5 PM
    15,          -- 15 minutes grace period
    1,           -- Active
    1,           -- Notify on late arrival
    1,           -- Notify on early departure
    NOW(),
    NOW()
);
*/

-- ============================================
-- STEP 4: Recalculate late/early/overtime for existing records
-- ============================================
-- This will update existing records based on work schedule
-- Only run this if you have an active work schedule

/*
-- Update late arrivals
UPDATE attendance a
CROSS JOIN work_schedule ws
SET 
    a.is_late = CASE 
        WHEN TIME(a.sign_in_time) > ADDTIME(ws.work_start_time, SEC_TO_TIME(ws.grace_period_minutes * 60))
        THEN 1 
        ELSE 0 
    END,
    a.late_minutes = CASE 
        WHEN TIME(a.sign_in_time) > ADDTIME(ws.work_start_time, SEC_TO_TIME(ws.grace_period_minutes * 60))
        THEN TIMESTAMPDIFF(MINUTE, ws.work_start_time, TIME(a.sign_in_time))
        ELSE NULL
    END,
    a.status = CASE 
        WHEN TIME(a.sign_in_time) > ADDTIME(ws.work_start_time, SEC_TO_TIME(ws.grace_period_minutes * 60))
        THEN 'LATE'
        WHEN a.sign_out_time IS NOT NULL
        THEN 'SIGNED_OUT'
        ELSE 'SIGNED_IN'
    END
WHERE ws.is_active = 1
AND a.sign_in_time IS NOT NULL;

-- Update early departures and overtime
UPDATE attendance a
CROSS JOIN work_schedule ws
SET 
    a.is_early_departure = CASE 
        WHEN a.sign_out_time IS NOT NULL 
        AND TIME(a.sign_out_time) < ws.work_end_time
        THEN 1 
        ELSE 0 
    END,
    a.early_departure_minutes = CASE 
        WHEN a.sign_out_time IS NOT NULL 
        AND TIME(a.sign_out_time) < ws.work_end_time
        THEN TIMESTAMPDIFF(MINUTE, TIME(a.sign_out_time), ws.work_end_time)
        ELSE NULL
    END,
    a.overtime_minutes = CASE 
        WHEN a.sign_out_time IS NOT NULL 
        AND TIME(a.sign_out_time) > ws.work_end_time
        THEN TIMESTAMPDIFF(MINUTE, ws.work_end_time, TIME(a.sign_out_time))
        ELSE NULL
    END
WHERE ws.is_active = 1
AND a.sign_out_time IS NOT NULL;
*/

-- ============================================
-- STEP 5: Verify the updates
-- ============================================
SELECT 
    'After Update Status' as check_type,
    COUNT(*) as total_records,
    SUM(CASE WHEN is_late = 1 THEN 1 ELSE 0 END) as late_records,
    SUM(CASE WHEN late_minutes IS NOT NULL AND late_minutes > 0 THEN 1 ELSE 0 END) as with_late_minutes,
    SUM(CASE WHEN is_early_departure = 1 THEN 1 ELSE 0 END) as early_departure_records,
    SUM(CASE WHEN overtime_minutes IS NOT NULL AND overtime_minutes > 0 THEN 1 ELSE 0 END) as overtime_records
FROM attendance;

-- Show updated records
SELECT 
    u.full_name as employee_name,
    u.username,
    a.attendance_date,
    TIME(a.sign_in_time) as check_in_time,
    TIME(a.sign_out_time) as check_out_time,
    a.status,
    CASE WHEN a.is_late = 1 THEN 'Yes' ELSE 'No' END as late,
    a.late_minutes,
    CASE WHEN a.is_early_departure = 1 THEN 'Yes' ELSE 'No' END as early_departure,
    a.overtime_minutes,
    a.notes
FROM attendance a
JOIN users u ON a.user_id = u.id
ORDER BY a.attendance_date DESC, a.sign_in_time DESC
LIMIT 20;

-- Made with Bob
