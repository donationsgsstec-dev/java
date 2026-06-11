# Internship Attendance System

## Overview
This attendance system allows internship students to sign in and sign out, tracking their daily attendance. Administrators can view all attendance records and generate reports.

## Features

### For Students (INTERN role)
- **Sign In/Sign Out**: Students can sign in when they arrive and sign out when they leave
- **Attendance History**: View personal attendance history with dates, times, and durations
- **Dashboard**: See current status (signed in/out) and total attendance days
- **Real-time Tracking**: System tracks exact sign-in and sign-out times

### For Administrators (ADMIN role)
- **Live Dashboard**: View all students currently signed in
- **Daily Reports**: See all attendance records for any specific date
- **Date Range Reports**: Generate reports for custom date ranges
- **Statistics**: View total attendance, currently signed in count, and more

## User Roles

### INTERN (Default)
- Can access personal attendance features
- Can sign in/out
- Can view own attendance history
- Cannot access admin features

### ADMIN
- Has all INTERN permissions
- Can view all students' attendance records
- Can access admin dashboard
- Can generate reports

## Database Schema

### User Entity Updates
```java
- role: UserRole (INTERN/ADMIN) - Default: INTERN
- attendanceRecords: List<Attendance> - One-to-many relationship
```

### Attendance Entity
```java
- id: Long (Primary Key)
- user: User (Many-to-one relationship)
- signInTime: LocalDateTime
- signOutTime: LocalDateTime
- attendanceDate: LocalDate
- status: AttendanceStatus (SIGNED_IN/SIGNED_OUT)
- notes: String
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

## API Endpoints

### Student Endpoints (Authenticated)
- `GET /attendance` - View attendance dashboard
- `POST /attendance/sign-in` - Sign in for the day
- `POST /attendance/sign-out` - Sign out
- `GET /attendance/history` - View personal attendance history

### Admin Endpoints (ADMIN role required)
- `GET /attendance/admin` - Admin dashboard (today's attendance)
- `GET /attendance/admin?date=YYYY-MM-DD` - View attendance for specific date
- `GET /attendance/admin/report` - Generate date range reports

## How to Use

### For Students

1. **Sign In**
   - Navigate to the Attendance page from the home dashboard
   - Click "Sign In" button
   - System records your sign-in time

2. **Sign Out**
   - Return to the Attendance page
   - Click "Sign Out" button
   - System calculates and displays your duration

3. **View History**
   - Click "History" in the navigation
   - View all your past attendance records
   - Filter by date range if needed

### For Administrators

1. **View Today's Attendance**
   - Click "Admin View" from the home page or attendance page
   - See all students currently signed in
   - View complete attendance list for today

2. **View Specific Date**
   - Use the date picker on the admin dashboard
   - Click "View Attendance"
   - See all records for that date

3. **Generate Reports**
   - Click "Reports" in the admin navigation
   - Select date range
   - View comprehensive attendance data

## Business Rules

1. **Single Sign-In**: A student can only be signed in once at a time
2. **Must Sign Out**: Students must sign out before signing in again
3. **Date Tracking**: Each attendance record is associated with a specific date
4. **Duration Calculation**: System automatically calculates time spent
5. **Status Management**: Status automatically updates on sign-in/sign-out

## Security

- All attendance endpoints require authentication
- Admin endpoints require ADMIN role
- CSRF protection enabled for all POST requests
- Session management prevents unauthorized access

## Views

### Student Views
1. **attendance.jsp** - Main attendance page with sign-in/out buttons
2. **attendance-history.jsp** - Personal attendance history (to be created if needed)

### Admin Views
1. **attendance-admin.jsp** - Admin dashboard with live tracking
2. **attendance-report.jsp** - Date range reports (to be created if needed)

## Technical Implementation

### Backend Components
- **Entity**: `Attendance.java` - JPA entity for attendance records
- **Repository**: `AttendanceRepository.java` - Data access layer
- **Service**: `AttendanceService.java` & `AttendanceServiceImpl.java` - Business logic
- **Controller**: `AttendanceController.java` - Request handling

### Frontend Components
- **JSP Views**: attendance.jsp, attendance-admin.jsp
- **Styling**: Inline CSS with responsive design
- **JSTL Tags**: For dynamic content rendering

## Testing the System

### Test as Student
1. Register a new user (default role: INTERN)
2. Login with the new user
3. Navigate to Attendance page
4. Test sign-in functionality
5. Test sign-out functionality
6. View attendance history

### Test as Admin
1. Manually update a user's role to ADMIN in the database
2. Login with admin user
3. Access admin dashboard
4. View all attendance records
5. Test date filtering
6. Verify live tracking of signed-in users

## Database Setup

The system uses H2 in-memory database. Tables are auto-created on startup.

To manually set a user as admin:
```sql
UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
```

## Future Enhancements

Potential improvements:
- Export attendance reports to CSV/PDF
- Email notifications for missed sign-outs
- Attendance statistics and analytics
- Mobile app integration
- Biometric sign-in support
- Geolocation verification
- Automated reminders

## Troubleshooting

### Common Issues

1. **Cannot Sign In**
   - Ensure you're not already signed in
   - Check if you signed out from previous session

2. **Admin Features Not Visible**
   - Verify user role is set to ADMIN
   - Clear browser cache and re-login

3. **Attendance Not Showing**
   - Verify database connection
   - Check application logs for errors

## Support

For issues or questions, contact the system administrator.

---

**Made with Bob** - Internship Attendance System v1.0