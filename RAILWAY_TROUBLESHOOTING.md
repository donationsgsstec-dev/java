# Railway Deployment Troubleshooting

## 🚨 Current Issue: "Service Unavailable" Errors

You're seeing repeated "service unavailable" errors. This typically means the app can't connect to the database.

---

## 🔧 IMMEDIATE FIX

### Step 1: Check Database Status

1. Go to Railway dashboard
2. Click on your **PostgreSQL** service
3. Check if it shows **"Active"** or **"Running"**
4. If not, wait for it to start (1-2 minutes)

---

### Step 2: Verify Environment Variables

The issue is likely with database connection variables. Let me show you the CORRECT way:

#### ❌ WRONG (What might be causing the issue):
```bash
SPRING_DATASOURCE_URL=postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}
```

#### ✅ CORRECT (Use this instead):
```bash
SPRING_DATASOURCE_URL=${{Postgres.DATABASE_URL}}
```

**Why?** Railway provides `DATABASE_URL` which is already in the correct format!

---

### Step 3: Update Environment Variables

1. Click on your **web service**
2. Go to **"Variables"** tab
3. **Delete** the old database variables
4. **Add** these new ones:

```bash
SPRING_PROFILES_ACTIVE=production

SPRING_DATASOURCE_URL=${{Postgres.DATABASE_URL}}

QSSN_EMAIL_API_KEY=qssn_live_9e923f45d56ede8ec89ded81b13ccc246823cbb1bff67d31d07538c584a424fe

QSSN_EMAIL_API_URL=https://qssn-d1-api.gastonsoftwaresolutions234.workers.dev/api/v1/emails/send

QSSN_EMAIL_FROM_NAME=Pahappa Attendance System

QSSN_EMAIL_ENABLED=true

APP_ADMIN_EMAIL=admin@pahappa.com
```

**Note:** We removed `SPRING_DATASOURCE_USERNAME` and `SPRING_DATASOURCE_PASSWORD` because they're included in `DATABASE_URL`!

---

### Step 4: Update application-production.properties

We need to handle Railway's `DATABASE_URL` format. Let me create a fix:

**The Issue:**
Railway provides: `postgresql://user:pass@host:port/db`
Spring Boot expects: `jdbc:postgresql://host:port/db`

**The Fix:**
Update your environment variable to:
```bash
SPRING_DATASOURCE_URL=jdbc:${{Postgres.DATABASE_URL}}
```

Wait, that won't work either. Let's use a better approach...

---

## 🎯 BEST SOLUTION: Use Railway's Variables Correctly

### Option 1: Individual Variables (Recommended)

Delete all database variables and add these:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}

SPRING_DATASOURCE_USERNAME=${{Postgres.PGUSER}}

SPRING_DATASOURCE_PASSWORD=${{Postgres.PGPASSWORD}}
```

**Important:** Make sure your PostgreSQL service is named **"Postgres"** (check in Railway dashboard)

---

### Option 2: Use DATABASE_URL with Conversion

If Option 1 doesn't work, we need to modify the application properties.

1. Keep this variable:
```bash
DATABASE_URL=${{Postgres.DATABASE_URL}}
```

2. We'll handle the conversion in code (I'll create a fix for this)

---

## 🔍 Check Deployment Logs

1. Go to **"Deployments"** tab
2. Click on the failing deployment
3. Look for these errors:

### Error: "Connection refused"
**Cause:** Database not ready
**Fix:** Wait 2-3 minutes, then redeploy

### Error: "Authentication failed"
**Cause:** Wrong credentials
**Fix:** Use `${{Postgres.PGUSER}}` and `${{Postgres.PGPASSWORD}}`

### Error: "Unknown database"
**Cause:** Wrong database name
**Fix:** Use `${{Postgres.PGDATABASE}}`

### Error: "Could not connect to server"
**Cause:** Wrong host or port
**Fix:** Use `${{Postgres.PGHOST}}` and `${{Postgres.PGPORT}}`

---

## 📋 Complete Variable Setup (Copy-Paste Ready)

**Delete all existing variables, then add these:**

```bash
SPRING_PROFILES_ACTIVE=production

SPRING_DATASOURCE_URL=jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}

SPRING_DATASOURCE_USERNAME=${{Postgres.PGUSER}}

SPRING_DATASOURCE_PASSWORD=${{Postgres.PGPASSWORD}}

QSSN_EMAIL_API_KEY=qssn_live_9e923f45d56ede8ec89ded81b13ccc246823cbb1bff67d31d07538c584a424fe

QSSN_EMAIL_API_URL=https://qssn-d1-api.gastonsoftwaresolutions234.workers.dev/api/v1/emails/send

QSSN_EMAIL_FROM_NAME=Pahappa Attendance System

QSSN_EMAIL_ENABLED=true

APP_ADMIN_EMAIL=admin@pahappa.com
```

---

## 🔄 After Updating Variables

1. Click **"Redeploy"** button
2. Wait 2-3 minutes
3. Check deployment logs
4. Look for: "Started UserAuthApplication"

---

## ✅ Success Indicators

You'll know it's working when you see:

```
✓ Building
✓ Deploying
✓ Started UserAuthApplication in XX seconds
✓ Tomcat started on port XXXXX
```

---

## 🚨 If Still Failing

### Check PostgreSQL Service Name

1. Go to Railway dashboard
2. Look at your PostgreSQL service
3. Check the exact name (might be "PostgreSQL" not "Postgres")
4. Update variables to match:
   - If named "PostgreSQL": use `${{PostgreSQL.PGHOST}}`
   - If named "Postgres": use `${{Postgres.PGHOST}}`

---

## 💡 Alternative: Manual Database Connection

If Railway variables aren't working, get the actual values:

1. Click on **PostgreSQL** service
2. Go to **"Connect"** tab
3. Copy the connection details
4. Add as plain text (not recommended for security):

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://actual-host:5432/actual-db-name
SPRING_DATASOURCE_USERNAME=actual-username
SPRING_DATASOURCE_PASSWORD=actual-password
```

---

## 📞 Need More Help?

**Share these with me:**
1. Screenshot of your environment variables
2. Last 50 lines of deployment logs
3. PostgreSQL service status

**Railway Support:**
- Discord: https://discord.gg/railway
- Docs: https://docs.railway.app/databases/postgresql

---

## 🎯 Quick Checklist

Before asking for help, verify:
- [ ] PostgreSQL service is running
- [ ] Environment variables use correct syntax
- [ ] Service name matches (Postgres vs PostgreSQL)
- [ ] Waited 2-3 minutes after redeploy
- [ ] Checked deployment logs for specific errors

---

## ✨ Most Common Fix

**90% of the time, this works:**

1. Delete ALL environment variables
2. Add ONLY these:
```bash
SPRING_PROFILES_ACTIVE=production
SPRING_DATASOURCE_URL=jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}
SPRING_DATASOURCE_USERNAME=${{Postgres.PGUSER}}
SPRING_DATASOURCE_PASSWORD=${{Postgres.PGPASSWORD}}
```
3. Click "Redeploy"
4. Wait 3 minutes

If PostgreSQL service is named differently, replace "Postgres" with the actual name!
