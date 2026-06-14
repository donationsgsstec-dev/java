# 🚨 URGENT FIX: Railway MySQL Connection

## Your Current Issue

Your Spring Boot app can't connect to MySQL because the environment variables are incorrectly configured.

---

## ✅ STEP-BY-STEP FIX

### Step 1: Find Your MySQL Service Name

1. Go to Railway dashboard
2. Look at your MySQL service card
3. Check the name at the top of the card

**Common names:**
- `MySQL` (most common)
- `mysql` 
- `database`

**Write down the EXACT name (case-sensitive!)**

---

### Step 2: Delete Old Variables

In your **web service** (Spring Boot app):

1. Click on "Variables" tab
2. **Delete** these variables:
   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`

---

### Step 3: Add Correct Variables

Based on your MySQL service name, add these variables:

#### If your MySQL service is named "MySQL":

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Africa/Kampala

SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQLUSER}}

SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQLPASSWORD}}
```

#### If your MySQL service is named "mysql":

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://${{mysql.MYSQLHOST}}:${{mysql.MYSQLPORT}}/${{mysql.MYSQLDATABASE}}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Africa/Kampala

SPRING_DATASOURCE_USERNAME=${{mysql.MYSQLUSER}}

SPRING_DATASOURCE_PASSWORD=${{mysql.MYSQLPASSWORD}}
```

#### If your MySQL service is named "database":

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://${{database.MYSQLHOST}}:${{database.MYSQLPORT}}/${{database.MYSQLDATABASE}}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Africa/Kampala

SPRING_DATASOURCE_USERNAME=${{database.MYSQLUSER}}

SPRING_DATASOURCE_PASSWORD=${{database.MYSQLPASSWORD}}
```

---

### Step 4: Add Email Configuration (Optional but Recommended)

While you're in the Variables tab, also add these:

```bash
QSSN_EMAIL_API_KEY=qssn_live_9e923f45d56ede8ec89ded81b13ccc246823cbb1bff67d31d07538c584a424fe

QSSN_EMAIL_API_URL=https://qssn-d1-api.gastonsoftwaresolutions234.workers.dev/api/v1/emails/send

QSSN_EMAIL_FROM_NAME=Pahappa Attendance System

QSSN_EMAIL_ENABLED=true

APP_ADMIN_EMAIL=admin@pahappa.com
```

---

### Step 5: Verify SPRING_PROFILES_ACTIVE

Make sure you have:

```bash
SPRING_PROFILES_ACTIVE=production
```

---

### Step 6: Redeploy

1. Click "Redeploy" button
2. Wait 2-3 minutes
3. Watch the deployment logs

---

## 🎯 What You Should See

**Success indicators:**
```
✓ Building
✓ Deploying
✓ Started UserAuthApplication in XX seconds
✓ Tomcat started on port XXXXX
✓ Healthcheck passed
```

---

## 🔍 If Still Failing

### Check the exact variable values

Click on each variable and verify:

**SPRING_DATASOURCE_URL should show:**
```
jdbc:mysql://mysql.railway.internal:3306/railway?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Africa/Kampala
```

**SPRING_DATASOURCE_USERNAME should show:**
```
root
```

**SPRING_DATASOURCE_PASSWORD should show:**
```
pJBHMeOhXsqXMkjoPUwosgQBVaJDHjxF
```

If they show the `${{...}}` syntax instead, Railway is not resolving the variables correctly. This means your MySQL service name is wrong.

---

## 💡 Alternative: Use Direct Values (Quick Test)

If the variable references aren't working, temporarily use direct values:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://mysql.railway.internal:3306/railway?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Africa/Kampala

SPRING_DATASOURCE_USERNAME=root

SPRING_DATASOURCE_PASSWORD=pJBHMeOhXsqXMkjoPUwosgQBVaJDHjxF
```

**Note:** This is less secure but will help confirm the connection works.

---

## 📊 Complete Variable List (Copy-Paste Ready)

**Replace `MySQL` with your actual service name:**

```bash
SPRING_PROFILES_ACTIVE=production

SPRING_DATASOURCE_URL=jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Africa/Kampala

SPRING_DATASOURCE_USERNAME=${{MySQL.MYSQLUSER}}

SPRING_DATASOURCE_PASSWORD=${{MySQL.MYSQLPASSWORD}}

QSSN_EMAIL_API_KEY=qssn_live_9e923f45d56ede8ec89ded81b13ccc246823cbb1bff67d31d07538c584a424fe

QSSN_EMAIL_API_URL=https://qssn-d1-api.gastonsoftwaresolutions234.workers.dev/api/v1/emails/send

QSSN_EMAIL_FROM_NAME=Pahappa Attendance System

QSSN_EMAIL_ENABLED=true

APP_ADMIN_EMAIL=admin@pahappa.com
```

---

## 🚀 After Successful Connection

Once your app is running:

1. Visit your Railway app URL
2. You should see the login page
3. Register a new user
4. Use the SQL script to promote to admin (see `RAILWAY_CREATE_ADMIN.md`)

---

## 📞 Still Need Help?

Share:
1. Screenshot of your MySQL service card (showing the name)
2. Screenshot of your web service variables
3. Last 50 lines of deployment logs

---

**Made with Bob** 🤖