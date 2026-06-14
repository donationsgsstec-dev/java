# Pahappa Attendance System - All URLs

**Base URL:** https://pahappa-attendance-system-production.up.railway.app

---

## 🔐 PUBLIC URLS (No Login Required)

### Authentication
```
Login Page:
https://pahappa-attendance-system-production.up.railway.app/login

Register Page:
https://pahappa-attendance-system-production.up.railway.app/register

Logout:
https://pahappa-attendance-system-production.up.railway.app/logout
```

---

## 👤 INTERN/USER URLS (After Login)

### Home & Dashboard
```
Home Page:
https://pahappa-attendance-system-production.up.railway.app/home

Attendance Dashboard:
https://pahappa-attendance-system-production.up.railway.app/attendance/dashboard
```

### Attendance Management
```
Mark Attendance (Main):
https://pahappa-attendance-system-production.up.railway.app/attendance

Attendance History:
https://pahappa-attendance-system-production.up.railway.app/attendance/history

Attendance Calendar:
https://pahappa-attendance-system-production.up.railway.app/attendance/calendar
```

### QR Code Features
```
My QR Code (Personal):
https://pahappa-attendance-system-production.up.railway.app/attendance/my-qr

QR Code Scanner:
https://pahappa-attendance-system-production.up.railway.app/attendance/qr-scanner

Room QR Code:
https://pahappa-attendance-system-production.up.railway.app/attendance/room-qr
```

---

## 👨‍💼 ADMIN URLS (Admin Access Only)

### Admin Authentication
```
Admin Login:
https://pahappa-attendance-system-production.up.railway.app/admin/login
```

### Admin Dashboard
```
Admin Home:
https://pahappa-attendance-system-production.up.railway.app/attendance/admin

Admin Settings:
https://pahappa-attendance-system-production.up.railway.app/attendance/admin/settings
```

### Reports & Analytics
```
Attendance Report:
https://pahappa-attendance-system-production.up.railway.app/attendance/report

Export to Excel:
https://pahappa-attendance-system-production.up.railway.app/attendance/export/excel
```

---

## 🔧 API ENDPOINTS (For Developers)

### Health Check
```
Application Health:
https://pahappa-attendance-system-production.up.railway.app/actuator/health

Application Info:
https://pahappa-attendance-system-production.up.railway.app/actuator/info
```

### REST API Endpoints
```
Get Attendance Statistics:
GET https://pahappa-attendance-system-production.up.railway.app/api/attendance/statistics

Mark Attendance via API:
POST https://pahappa-attendance-system-production.up.railway.app/api/attendance/mark

Scan QR Code:
POST https://pahappa-attendance-system-production.up.railway.app/api/attendance/scan-qr
```

---

## 📱 QUICK ACCESS LINKS

### For Interns/Users:
```
1. Register: /register
2. Login: /login
3. Dashboard: /attendance/dashboard
4. Mark Attendance: /attendance
5. View History: /attendance/history
6. My QR Code: /attendance/my-qr
7. Scan QR: /attendance/qr-scanner
```

### For Admins:
```
1. Admin Login: /admin/login
2. Admin Dashboard: /attendance/admin
3. View Reports: /attendance/report
4. Settings: /attendance/admin/settings
5. Export Data: /attendance/export/excel
```

---

## 🎯 COMMON USER FLOWS

### New User Registration Flow:
```
1. https://pahappa-attendance-system-production.up.railway.app/register
   → Fill registration form
   
2. https://pahappa-attendance-system-production.up.railway.app/login
   → Login with credentials
   
3. https://pahappa-attendance-system-production.up.railway.app/home
   → Welcome page
   
4. https://pahappa-attendance-system-production.up.railway.app/attendance/dashboard
   → Start using attendance features
```

### Daily Attendance Flow:
```
1. https://pahappa-attendance-system-production.up.railway.app/login
   → Login
   
2. https://pahappa-attendance-system-production.up.railway.app/attendance
   → Mark attendance (Check In/Out)
   
3. https://pahappa-attendance-system-production.up.railway.app/attendance/history
   → View your attendance records
```

### QR Code Attendance Flow:
```
Option 1 - Personal QR:
1. https://pahappa-attendance-system-production.up.railway.app/attendance/my-qr
   → Generate your personal QR code
   
2. Show QR to admin/scanner
   → Attendance marked automatically

Option 2 - Room QR:
1. https://pahappa-attendance-system-production.up.railway.app/attendance/room-qr
   → View room QR code
   
2. https://pahappa-attendance-system-production.up.railway.app/attendance/qr-scanner
   → Scan room QR to mark attendance
```

### Admin Management Flow:
```
1. https://pahappa-attendance-system-production.up.railway.app/admin/login
   → Admin login
   
2. https://pahappa-attendance-system-production.up.railway.app/attendance/admin
   → View all users and attendance
   
3. https://pahappa-attendance-system-production.up.railway.app/attendance/report
   → Generate reports
   
4. https://pahappa-attendance-system-production.up.railway.app/attendance/export/excel
   → Export data to Excel
```

---

## 📊 URL STRUCTURE

```
Base: https://pahappa-attendance-system-production.up.railway.app

Public Routes:
├── /login                    (Login page)
├── /register                 (Registration page)
└── /logout                   (Logout action)

User Routes (Authenticated):
├── /home                     (Home page)
└── /attendance/
    ├── /                     (Mark attendance)
    ├── /dashboard            (Dashboard)
    ├── /history              (Attendance history)
    ├── /calendar             (Calendar view)
    ├── /my-qr                (Personal QR code)
    ├── /qr-scanner           (QR scanner)
    └── /room-qr              (Room QR code)

Admin Routes (Admin Only):
├── /admin/login              (Admin login)
└── /attendance/
    ├── /admin                (Admin dashboard)
    ├── /admin/settings       (Admin settings)
    ├── /report               (Reports)
    └── /export/excel         (Excel export)

API Routes:
└── /api/attendance/
    ├── /statistics           (GET - Statistics)
    ├── /mark                 (POST - Mark attendance)
    └── /scan-qr              (POST - Scan QR)

Health Check:
└── /actuator/
    ├── /health               (Health status)
    └── /info                 (App info)
```

---

## 🔑 DEFAULT CREDENTIALS

### Create Admin User:
After deployment, you need to create an admin user. Use the SQL script:

```sql
-- Run this in Railway PostgreSQL console
INSERT INTO users (username, email, password, role, enabled) 
VALUES ('admin', 'admin@pahappa.com', '$2a$10$encrypted_password', 'ADMIN', true);
```

Or use the promote script from your local setup.

---

## 📱 MOBILE-FRIENDLY URLS

All URLs are mobile-responsive. Users can access from:
- Desktop browsers
- Mobile browsers (iOS/Android)
- Tablets

---

## 🎓 FOR PRESENTATIONS

### Demo URLs (In Order):
```
1. Show Registration:
   https://pahappa-attendance-system-production.up.railway.app/register

2. Show Login:
   https://pahappa-attendance-system-production.up.railway.app/login

3. Show Dashboard:
   https://pahappa-attendance-system-production.up.railway.app/attendance/dashboard

4. Show QR Feature:
   https://pahappa-attendance-system-production.up.railway.app/attendance/my-qr

5. Show Admin Panel:
   https://pahappa-attendance-system-production.up.railway.app/attendance/admin

6. Show Reports:
   https://pahappa-attendance-system-production.up.railway.app/attendance/report
```

---

## 🔗 SHAREABLE LINKS

### For Team Members:
```
Main App: https://pahappa-attendance-system-production.up.railway.app
Login: https://pahappa-attendance-system-production.up.railway.app/login
Register: https://pahappa-attendance-system-production.up.railway.app/register
```

### For Admins:
```
Admin Login: https://pahappa-attendance-system-production.up.railway.app/admin/login
Admin Dashboard: https://pahappa-attendance-system-production.up.railway.app/attendance/admin
```

---

## ✅ TESTING CHECKLIST

Test these URLs to verify everything works:

**Public Access:**
- [ ] /login - Login page loads
- [ ] /register - Registration form works
- [ ] Can create new account

**User Features:**
- [ ] /home - Home page after login
- [ ] /attendance/dashboard - Dashboard loads
- [ ] /attendance - Can mark attendance
- [ ] /attendance/history - Shows attendance records
- [ ] /attendance/my-qr - QR code generates
- [ ] /attendance/qr-scanner - Scanner works

**Admin Features:**
- [ ] /admin/login - Admin login works
- [ ] /attendance/admin - Admin dashboard loads
- [ ] /attendance/report - Reports generate
- [ ] /attendance/export/excel - Excel export works

**API:**
- [ ] /actuator/health - Returns {"status":"UP"}
- [ ] API endpoints respond correctly

---

## 🎉 SUCCESS!

Your Pahappa Attendance System is now live on Railway at:
**https://pahappa-attendance-system-production.up.railway.app**

- ✅ No cold starts (always fast!)
- ✅ Professional hosting
- ✅ All features working
- ✅ Ready for production use
