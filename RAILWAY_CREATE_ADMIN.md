# Create Admin User in Railway PostgreSQL

## 🎯 Create Admin: JoshMore

**Username:** JoshMore
**Email:** info@gss-tec.com
**Password:** Admin@123
**Role:** ADMIN

---

## 📋 Step-by-Step Instructions

### Method 1: Using Railway Dashboard (Recommended)

1. **Go to Railway Dashboard**
   - Visit: https://railway.app/dashboard
   - Select your project: "pahappa-attendance-system"

2. **Open PostgreSQL Service**
   - Click on your **PostgreSQL** database service
   - Click on **"Data"** tab

3. **Open Query Console**
   - Click **"Query"** button
   - Or go to **"Connect"** → **"Query"**

4. **Run the SQL Script**
   - Copy the entire content from `create-joshmore-admin.sql`
   - Paste into the query console
   - Click **"Run"** or press **Ctrl+Enter**

5. **Verify Creation**
   - You should see: "Admin user created successfully!"
   - Check the user details displayed

---

### Method 2: Using Railway CLI

1. **Install Railway CLI** (if not installed)
   ```bash
   npm i -g @railway/cli
   ```

2. **Login to Railway**
   ```bash
   railway login
   ```

3. **Link to Your Project**
   ```bash
   railway link
   ```
   - Select: "pahappa-attendance-system"

4. **Connect to PostgreSQL**
   ```bash
   railway connect Postgres
   ```

5. **Run the SQL Script**
   ```bash
   \i create-joshmore-admin.sql
   ```
   
   Or copy-paste the SQL commands directly.

---

### Method 3: Using psql (Local Connection)

1. **Get Database Connection String**
   - Go to Railway Dashboard
   - Click on PostgreSQL service
   - Go to **"Connect"** tab
   - Copy the **"Postgres Connection URL"**

2. **Connect Using psql**
   ```bash
   psql "postgresql://user:password@host:port/database"
   ```

3. **Run the SQL Script**
   ```sql
   \i create-joshmore-admin.sql
   ```

---

## 🔑 Admin Login Credentials

After running the script, use these credentials:

```
Login URL: https://pahappa-attendance-system-production.up.railway.app/admin/login

Username: JoshMore
Email: info@gss-tec.com
Password: Admin@123
```

**⚠️ IMPORTANT:** Change the password after first login!

---

## ✅ Verification Steps

1. **Check User Created**
   ```sql
   SELECT id, username, email, role, enabled 
   FROM users 
   WHERE username = 'JoshMore';
   ```

2. **Test Login**
   - Go to: https://pahappa-attendance-system-production.up.railway.app/admin/login
   - Username: `JoshMore`
   - Password: `Admin@123`
   - Should redirect to admin dashboard

3. **Verify Admin Access**
   - Access admin dashboard: `/attendance/admin`
   - Check admin settings: `/attendance/admin/settings`
   - View reports: `/attendance/report`

---

## 🔧 Troubleshooting

### Error: "User already exists"
**Solution:** The script automatically deletes existing user first. If error persists:
```sql
DELETE FROM users WHERE username = 'JoshMore';
-- Then run the INSERT statement again
```

### Error: "Password authentication failed"
**Solution:** 
1. Verify you're using the correct database connection string
2. Check Railway dashboard for updated credentials
3. Try reconnecting

### Error: "Table 'users' does not exist"
**Solution:** 
1. Make sure the application has run at least once
2. Check if Hibernate created the tables
3. Verify `spring.jpa.hibernate.ddl-auto=update` in properties

---

## 📝 SQL Script Content

The script does the following:

1. **Deletes existing user** (if any)
   ```sql
   DELETE FROM users WHERE username = 'JoshMore' OR email = 'info@gss-tec.com';
   ```

2. **Creates admin user**
   ```sql
   INSERT INTO users (username, email, password, role, enabled, created_at) 
   VALUES ('JoshMore', 'info@gss-tec.com', '$2a$10$...', 'ADMIN', true, CURRENT_TIMESTAMP);
   ```

3. **Verifies creation**
   ```sql
   SELECT * FROM users WHERE username = 'JoshMore';
   ```

---

## 🔐 Password Information

**Default Password:** Admin@123

**BCrypt Hash:** 
```
$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCy
```

**To Change Password:**
1. Login as JoshMore
2. Go to profile/settings
3. Update password
4. Or run SQL:
   ```sql
   UPDATE users 
   SET password = '$2a$10$YOUR_NEW_BCRYPT_HASH' 
   WHERE username = 'JoshMore';
   ```

---

## 🎯 Quick Access

**Admin URLs:**
```
Admin Login:    /admin/login
Admin Dashboard: /attendance/admin
Admin Settings:  /attendance/admin/settings
Reports:        /attendance/report
Export Excel:   /attendance/export/excel
```

**Full URLs:**
```
https://pahappa-attendance-system-production.up.railway.app/admin/login
https://pahappa-attendance-system-production.up.railway.app/attendance/admin
https://pahappa-attendance-system-production.up.railway.app/attendance/admin/settings
https://pahappa-attendance-system-production.up.railway.app/attendance/report
https://pahappa-attendance-system-production.up.railway.app/attendance/export/excel
```

---

## 📊 Admin Capabilities

As admin, JoshMore can:
- ✅ View all users and their attendance
- ✅ Generate attendance reports
- ✅ Export data to Excel
- ✅ Manage system settings
- ✅ View attendance statistics
- ✅ Access admin dashboard
- ✅ Manage QR code sessions
- ✅ Configure work schedules

---

## 🚀 Next Steps

1. **Run the SQL script** in Railway PostgreSQL
2. **Test admin login** with provided credentials
3. **Change password** after first login
4. **Explore admin features** in the dashboard
5. **Share credentials** with JoshMore securely

---

## 📞 Support

If you encounter issues:
1. Check Railway logs for errors
2. Verify database connection
3. Ensure application is running
4. Check SQL script syntax

**Railway Dashboard:** https://railway.app/dashboard
