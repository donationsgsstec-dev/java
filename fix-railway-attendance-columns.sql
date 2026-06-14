-- ============================================
-- FIX ATTENDANCE REPORT MISSING COLUMNS
-- Run this on Railway MySQL Database
-- ============================================

-- STEP 1: Fix the work schedule active field
-- The active field is currently empty, needs to be set to 1 (true)
UPDATE work_schedule 
SET active = 1 
WHERE id = 1;

-- Verify the fix
SELECT 
    id,
    name,
    work_start_time,
    work_end_time,
    grace_period_minutes,
    active,
    notify_late_arrival,
    notify_early_departure
FROM work_schedule;

-- STEP 2: Check current attendance data status
SELECT 
    'Before Update' as status,
    COUNT(*) as total_records,
    SUM(CASE WHEN is_late = 1 THEN 1 ELSE 0 END) as late_count,
    SUM(CASE WHEN late_minutes IS NOT NULL AND late_minutes > 0 THEN 1 ELSE 0 END) as with_late_minutes,
    SUM(CASE WHEN is_early_departure = 1 THEN 1 ELSE 0 END) as early_departure_count,
    SUM(CASE WHEN overtime_minutes IS NOT NULL AND overtime_minutes > 0 THEN 1 ELSE 0 END) as overtime_count
FROM attendance;

-- STEP 3: Update existing attendance records with late calculations
-- Work schedule: Start 12:00 PM, Grace 15 min → Late after 12:15 PM
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
        ELSE a.status
    END
WHERE ws.active = 1
AND a.sign_in_time IS NOT NULL;

-- STEP 4: Update existing attendance records with early departure and overtime
-- Work schedule: End 20:00 (8 PM)
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
WHERE ws.active = 1
AND a.sign_out_time IS NOT NULL;

-- STEP 5: Verify the updates
SELECT 
    'After Update' as status,
    COUNT(*) as total_records,
    SUM(CASE WHEN is_late = 1 THEN 1 ELSE 0 END) as late_count,
    SUM(CASE WHEN late_minutes IS NOT NULL AND late_minutes > 0 THEN 1 ELSE 0 END) as with_late_minutes,
    SUM(CASE WHEN is_early_departure = 1 THEN 1 ELSE 0 END) as early_departure_count,
    SUM(CASE WHEN overtime_minutes IS NOT NULL AND overtime_minutes > 0 THEN 1 ELSE 0 END) as overtime_count
FROM attendance;

-- STEP 6: Show sample updated records
SELECT 
    a.id,
    u.full_name as employee_name,
    u.username,
    a.attendance_date,
    TIME(a.sign_in_time) as check_in_time,
    TIME(a.sign_out_time) as check_out_time,
    a.status,
    CASE WHEN a.is_late = 1 THEN 'Yes' ELSE 'No' END as late,
    a.late_minutes,
    CASE WHEN a.is_early_departure = 1 THEN 'Yes' ELSE 'No' END as early_departure,
    a.early_departure_minutes,
    a.overtime_minutes,
    a.notes
FROM attendance a
JOIN users u ON a.user_id = u.id
ORDER BY a.attendance_date DESC, a.sign_in_time DESC
LIMIT 20;

-- ============================================
-- EXPECTED RESULTS:
-- ============================================
-- Based on your work schedule (12:00 PM - 8:00 PM, 15 min grace):
-- 
-- LATE: Anyone who checked in after 12:15 PM
-- - Late = "Yes"
-- - Late Minutes = minutes after 12:00 PM
--
-- EARLY DEPARTURE: Anyone who checked out before 8:00 PM
-- - Early Departure = "Yes"  
-- - Early Departure Minutes = minutes before 8:00 PM
--
-- OVERTIME: Anyone who checked out after 8:00 PM
-- - Overtime Minutes = minutes after 8:00 PM
--
-- After running this script, download the Excel report again.
-- All columns should now have data!
-- ============================================

-- Made with Bob
