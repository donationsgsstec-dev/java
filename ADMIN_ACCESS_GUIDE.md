# Admin Access Guide

## How to Access Admin Features

### 1. Admin Login URL
Access the admin login page at:
```
https://pahappa-attendance-system-production.up.railway.app/admin/login
```

**Important:** Do NOT use the regular `/login` page - that's for regular users only.

### 2. Admin Credentials
You need an account with ADMIN authority. Check if you have admin users by running:

```sql
SELECT id, username, firstName, lastName, email, authority 
FROM users 
WHERE authority = 'ADMIN';
```

### 3. Create Admin User (If Needed)

If you don't have an admin user, you can promote an existing user or create a new one:

#### Option A: Promote Existing User
```sql
UPDATE users SET authority = 'ADMIN' WHERE username = 'your-username';
```

#### Option B: Use the SQL Script
Run the existing script:
```bash
# On Railway MySQL, execute:
mysql -h your-railway-host -u root -p your-database < promote-joshmore-to-admin.sql
```

Or use the Railway MySQL console to run:
```sql
UPDATE users SET authority = 'ADMIN' WHERE username = 'joshmore';
```

### 4. Login Process

1. Go to: `https://pahappa-attendance-system-production.up.railway.app/admin/login`
2. Enter your admin username and password
3. You'll be redirected to: `/attendance/admin` (Admin Dashboard)

### 5. Access Room QR Codes

Once logged in as admin:
1. Navigate to: `https://pahappa-attendance-system-production.up.railway.app/attendance/admin/room-qr`
2. Click "Generate Check-In & Check-Out Codes"
3. The QR codes will be displayed

### 6. Troubleshooting 403 Errors

If you get a 403 Forbidden error:

**Check 1: Are you logged in as admin?**
- Make sure you used `/admin/login` (not `/login`)
- Verify your account has `authority = 'ADMIN'` in the database

**Check 2: Session expired?**
- Sessions expire after inactivity
- Log out and log back in through `/admin/login`

**Check 3: Using correct URL?**
- Admin endpoints: `/admin/**` and `/attendance/admin/**`
- Regular user endpoints: `/attendance/**` (non-admin paths)

### 7. Admin vs Regular User Login

| Feature | Regular User | Admin User |
|---------|-------------|------------|
| Login URL | `/login` | `/admin/login` |
| Dashboard | `/home` | `/attendance/admin` |
| Authority | `USER` | `ADMIN` |
| Can generate room QR | ❌ No | ✅ Yes |
| Can view all attendance | ❌ No | ✅ Yes |
| Can manage settings | ❌ No | ✅ Yes |

### 8. Security Notes

- Admin and regular user sessions are separate
- Logging in as a regular user won't give you admin access
- You must explicitly log in through `/admin/login` with admin credentials
- CSRF tokens are automatically included in all forms

## Quick Test

To verify admin access works:

1. Open browser in incognito/private mode
2. Go to: `https://pahappa-attendance-system-production.up.railway.app/admin/login`
3. Login with admin credentials
4. You should see the admin dashboard
5. Navigate to Room QR Codes
6. Generate codes - should work without 403 error

## Current Admin Endpoints

All these require ADMIN authority:
- `/admin/login` - Admin login page (public)
- `/attendance/admin` - Admin dashboard
- `/attendance/admin/settings` - Admin settings
- `/attendance/admin/room-qr` - Room QR code management
- `/attendance/admin/room-qr/generate` - Generate new QR codes (POST)
- `/attendance/admin/room-qr/revoke` - Revoke QR codes (POST)