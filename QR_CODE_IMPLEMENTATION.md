# QR Code Implementation - Features & Usage

## Overview
This document describes the implementation of QR code features with expiration, single-use validation, and rate limiting for the attendance system.

## Features Implemented

### 1. QR Code Expiration (10 Minutes)
- **Entity**: `QRCodeSession` tracks each generated QR code
- **Expiration**: QR codes automatically expire 10 minutes after generation
- **Database**: `qr_code_sessions` table stores generation time and expiration time
- **Validation**: System checks expiration before allowing QR code usage

### 2. Single-Use QR Codes
- **Usage Tracking**: Each QR code can only be used once
- **Status Field**: `is_used` boolean flag prevents reuse
- **Timestamp**: `used_at` records when QR code was scanned
- **Validation**: System rejects already-used QR codes

### 3. Rate Limiting (2 QR Codes per Minute)
- **Limit**: Users can generate maximum 2 QR codes per minute
- **Implementation**: `QRCodeService.checkRateLimit()` method
- **Database Query**: Counts QR codes generated in last 60 seconds
- **Error Handling**: Returns HTTP 429 (Too Many Requests) when limit exceeded

### 4. Real-Time Weekly Attendance Overview
- **REST API**: `/api/attendance/weekly-overview` endpoint
- **Auto-Refresh**: Frontend updates every 30 seconds
- **Data**: Shows attendance for current week (Monday-Sunday)
- **Display**: Hours worked per day with real-time updates

### 5. Once-Per-Day Check-In Restriction
- **Validation**: Users can only check in once per 24-hour period
- **Check**: System verifies no existing attendance record for current date
- **Error Message**: "You have already checked in today. You can only check in once per day."
- **Implementation**: Database query checks `attendance_date` field before allowing sign-in

## Database Schema

### QR Code Sessions Table
```sql
CREATE TABLE qr_code_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    qr_code_data VARCHAR(1000) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL,
    expires_at DATETIME NOT NULL,
    is_used BOOLEAN NOT NULL DEFAULT FALSE,
    used_at DATETIME NULL,
    ip_address VARCHAR(45) NULL,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_qr_code_data (qr_code_data),
    INDEX idx_user_created_at (user_id, created_at),
    INDEX idx_expires_at (expires_at)
);
```

## API Endpoints

### 1. Generate QR Code
- **Endpoint**: `GET /attendance/qr-code/generate`
- **Rate Limit**: 2 requests per minute
- **Response**: PNG image (Base64 encoded)
- **Error**: HTTP 429 if rate limit exceeded

### 2. Weekly Overview (Real-Time)
- **Endpoint**: `GET /api/attendance/weekly-overview`
- **Response**: JSON with weekly attendance data
- **Auto-Refresh**: Every 30 seconds on frontend
- **Data Structure**:
```json
{
  "weeklyData": {
    "MONDAY": {
      "present": true,
      "signInTime": "2026-06-09T08:00:00",
      "signOutTime": "2026-06-09T17:00:00",
      "isLate": false,
      "status": "SIGNED_OUT",
      "duration": "8h 30m",
      "date": "2026-06-09"
    }
  },
  "weekStart": "2026-06-09",
  "weekEnd": "2026-06-15",
  "lastUpdated": 1718298000000
}
```

### 3. Current Status
- **Endpoint**: `GET /api/attendance/current-status`
- **Response**: Current sign-in status
- **Real-Time**: Immediate status check

### 4. Today's Attendance
- **Endpoint**: `GET /api/attendance/today`
- **Response**: Today's attendance record
- **Real-Time**: Current day status

## Implementation Files

### Backend Files Created/Modified
1. **`QRCodeSession.java`** - Entity for tracking QR codes
2. **`QRCodeSessionRepository.java`** - Repository for QR code sessions
3. **`QRCodeService.java`** - Updated with expiration and rate limiting
4. **`AttendanceRestController.java`** - REST API for real-time data
5. **`AttendanceController.java`** - Updated QR generation endpoint

### Frontend Files Modified
1. **`attendance-dashboard.jsp`** - Added real-time weekly chart updates

### Database Files
1. **`database-qr-session-migration.sql`** - Migration script for new table

## Usage Instructions

### For Users

#### Generating QR Code
1. Navigate to attendance page
2. Click "Generate QR Code" button
3. QR code is valid for 10 minutes
4. Can generate maximum 2 QR codes per minute
5. Each QR code can only be used once

#### Viewing Weekly Attendance
1. Go to Attendance Dashboard
2. Weekly chart updates automatically every 30 seconds
3. Shows hours worked for each day of current week
4. Real-time data from database

### For Administrators

#### Database Setup
```bash
# Run migration script
mysql -u root -p userdb < database-qr-session-migration.sql
```

#### Monitoring QR Code Usage
```sql
-- View recent QR code sessions
SELECT u.username, q.created_at, q.expires_at, q.is_used, q.used_at
FROM qr_code_sessions q
JOIN users u ON q.user_id = u.id
ORDER BY q.created_at DESC
LIMIT 20;

-- Check expired but unused QR codes
SELECT COUNT(*) as expired_unused
FROM qr_code_sessions
WHERE expires_at < NOW() AND is_used = FALSE;
```

## Security Features

### 1. Encryption
- QR code data is AES encrypted
- Contains: user_id:username:timestamp
- Prevents tampering and forgery

### 2. Expiration Validation
- Server-side validation on every scan
- Checks both database record and timestamp
- Rejects expired codes immediately

### 3. Single-Use Enforcement
- Database constraint prevents reuse
- Atomic update operation marks as used
- Race condition protection

### 4. Rate Limiting
- Prevents QR code generation abuse
- Per-user limit enforcement
- Sliding window algorithm (last 60 seconds)

## Error Handling

### Rate Limit Exceeded
```
HTTP 429 Too Many Requests
Message: "Rate limit exceeded. You can only generate 2 QR codes per minute."
```

### Expired QR Code
```
Validation returns null
User sees: "QR code has expired. Please generate a new one."
```

### Already Used QR Code
```
Validation returns null
User sees: "QR code has already been used."
```

### Already Checked In Today
```
IllegalStateException thrown
Message: "You have already checked in today. You can only check in once per day."
```

## Performance Considerations

### Database Indexes
- `idx_qr_code_data`: Fast QR code lookup
- `idx_user_created_at`: Efficient rate limit checks
- `idx_expires_at`: Quick expiration queries

### Cleanup Strategy
- Expired sessions older than 24 hours can be deleted
- Use `QRCodeService.cleanupExpiredSessions()` method
- Recommended: Schedule daily cleanup task

### Frontend Optimization
- 30-second refresh interval balances real-time vs load
- Chart updates use efficient data structure
- Minimal DOM manipulation

## Testing Checklist

- [ ] Generate QR code successfully
- [ ] QR code expires after 10 minutes
- [ ] QR code cannot be reused
- [ ] Rate limit enforced (max 2 per minute)
- [ ] User cannot check in twice in same day
- [ ] Weekly chart updates in real-time
- [ ] Database migration runs successfully
- [ ] API endpoints return correct data
- [ ] Error messages display properly

## Future Enhancements

1. **Push Notifications**: Alert users when QR code is about to expire
2. **QR Code History**: View all generated QR codes
3. **Admin Dashboard**: Monitor QR code usage patterns
4. **Geolocation**: Track where QR codes are scanned
5. **Analytics**: QR code generation and usage statistics

## Support

For issues or questions:
- Check application logs for errors
- Verify database migration completed
- Ensure all dependencies are installed
- Review API endpoint responses

---
**Last Updated**: 2026-06-13
**Version**: 1.0