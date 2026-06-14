# Railway MySQL Configuration

## 🎯 Your MySQL Database Details

**Connection String:**
```
mysql://root:pJBHMeOhXsqXMkjoPUwosgQBVaJDHjxF@centerbeam.proxy.rlwy.net:47991/railway
```

**Parsed Details:**
- Host: `centerbeam.proxy.rlwy.net`
- Port: `47991`
- Username: `root`
- Password: `pJBHMeOhXsqXMkjoPUwosgQBVaJDHjxF`
- Database: `railway`

---

## 🔧 Configure Railway Environment Variables

### Step 1: Go to Railway Dashboard
1. Visit: https://railway.app/dashboard
2. Select your web service (not the MySQL service)
3. Click on "Variables" tab

### Step 2: Add These Environment Variables

```bash
SPRING_PROFILES_ACTIVE=production

SPRING_DATASOURCE_URL=jdbc:mysql://centerbeam.proxy.rlwy.net:47991/railway?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Africa/Kampala

SPRING_DATASOURCE_USERNAME=root

SPRING_DATASOURCE_PASSWORD=pJBHMeOhXsqXMkjoPUwosgQBVaJDHjxF

QSSN_EMAIL_API_KEY=qssn_live_9e923f45d56ede8ec89ded81b13ccc246823cbb1bff67d31d07538c584a424fe

QSSN_EMAIL_API_URL=https://qssn-d1-api.gastonsoftwaresolutions234.workers.dev/api/v1/emails/send

QSSN_EMAIL_FROM_NAME=Pahappa Attendance System

QSSN_EMAIL_ENABLED=true

APP_ADMIN_EMAIL=admin@pahappa.com
```

### Step 3: Save and Redeploy
1. Click "Add" for each variable
2. Railway will automatically redeploy
3. Wait 2-3 minutes for deployment

---

## 📊 Using Railway Variables (Alternative)

If your MySQL service is named "MySQL" in Railway, you can use:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Africa/Kampala

SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQLUSER}}

SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQLPASSWORD}}
```

---

## 🗄️ Create Admin User in MySQL

### Method 1: Using Railway CLI

1. **Install Railway CLI:**
   ```bash
   npm i -g @railway/cli
   ```

2. **Login:**
   ```bash
   railway login
   ```

3. **Connect to MySQL:**
   ```bash
   railway connect MySQL
   ```

4. **Run SQL:**
   ```sql
   USE railway;
   
   -- Create admin user
   INSERT INTO users (username, email, password, role, enabled, created_at) 
   VALUES (
       'JoshMore',
       'info@gss-tec.com',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCy',
       'ADMIN',
       true,
       NOW()
   );
   ```

### Method 2: Using MySQL Client

1. **Connect:**
   ```bash
   mysql -h centerbeam.proxy.rlwy.net -u root -ppJBHMeOhXsqXMkjoPUwosgQBVaJDHjxF --port 47991 --protocol=TCP railway
   ```

2. **Run SQL:**
   ```sql
   INSERT INTO users (username, email, password, role, enabled, created_at) 
   VALUES (
       'JoshMore',
       'info@gss-tec.com',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCy',
       'ADMIN',
       true,
       NOW()
   );
   ```

### Method 3: Register Then Promote (Easiest)

1. **Register at:**
   https://pahappa-attendance-system-production.up.railway.app/register
   - Username: JoshMore
   - Email: info@gss-tec.com
   - Password: [Your choice]

2. **Connect to MySQL and promote:**
   ```sql
   USE railway;
   UPDATE users SET role = 'ADMIN' WHERE username = 'JoshMore';
   ```

---

## ✅ Verify Configuration

### Check Database Connection

1. **View Application Logs:**
   - Go to Railway dashboard
   - Click on your web service
   - Go to "Deployments" → Latest deployment → "View Logs"

2. **Look for:**
   ```
   HikariPool-1 - Starting...
   HikariPool-1 - Start completed.
   Started UserAuthApplication in XX seconds
   ```

### Test Admin Login

1. **Go to:**
   https://pahappa-attendance-system-production.up.railway.app/admin/login

2. **Login with:**
   - Username: JoshMore
   - Password: Admin@123 (or what you set)

---

## 🔍 Troubleshooting

### "Communications link failure"
**Cause:** Can't connect to MySQL
**Fix:**
- Verify host, port, username, password
- Check MySQL service is running in Railway
- Ensure firewall allows connection

### "Access denied for user"
**Cause:** Wrong credentials
**Fix:**
- Double-check username and password
- Verify database name is 'railway'
- Check Railway MySQL service for correct credentials

### "Unknown database 'railway'"
**Cause:** Database doesn't exist
**Fix:**
- Create database: `CREATE DATABASE railway;`
- Or use existing database name from Railway

### Tables not created
**Cause:** Hibernate not creating tables
**Fix:**
- Check `spring.jpa.hibernate.ddl-auto=update`
- Verify MySQL user has CREATE permissions
- Check application logs for errors

---

## 📋 Complete Setup Checklist

- [ ] MySQL service running in Railway
- [ ] Environment variables configured in web service
- [ ] Application redeployed
- [ ] Database connection successful (check logs)
- [ ] Tables created automatically
- [ ] Admin user created
- [ ] Admin login works

---

## 🎯 Quick Commands

**Connect to MySQL:**
```bash
railway connect MySQL
```

**Check tables:**
```sql
USE railway;
SHOW TABLES;
```

**View users:**
```sql
SELECT id, username, email, role FROM users;
```

**Promote to admin:**
```sql
UPDATE users SET role = 'ADMIN' WHERE username = 'JoshMore';
```

---

## 🚀 After Setup

Your app will be fully functional with:
- ✅ MySQL database connected
- ✅ Tables auto-created
- ✅ Admin user ready
- ✅ All features working

**Live URL:**
https://pahappa-attendance-system-production.up.railway.app
