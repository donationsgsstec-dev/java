# Render Deployment Troubleshooting

## 🔍 Current Issue: "Deployed" but Not "Running"

Your service shows **"Deployed"** but not **"Running"** - this means the build succeeded but the application isn't starting.

---

## 🚨 IMMEDIATE STEPS TO FIX

### Step 1: Check Render Logs
1. Go to https://dashboard.render.com
2. Click on **"pahappa-attendance-system"**
3. Click on **"Logs"** tab
4. Look for error messages in the logs

**Common errors to look for:**
- `Port already in use`
- `Failed to bind to port`
- `Application failed to start`
- `OutOfMemoryError`
- Database connection errors

---

### Step 2: Manual Redeploy
Sometimes Render needs a manual trigger:

1. In your service dashboard
2. Click **"Manual Deploy"** button
3. Select **"Deploy latest commit"**
4. Wait 5-7 minutes for rebuild

---

### Step 3: Check Environment Variables
Verify these are set in Render:

```
SPRING_PROFILES_ACTIVE=production
SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-d8mv6s8js32c73d4j92g-a.oregon-postgres.render.com:5432/java_vbxv
SPRING_DATASOURCE_USERNAME=java_vbxv_user
SPRING_DATASOURCE_PASSWORD=jiQ79h9KMpiwcN1W0KQ9Y7iRPx4iZD8e
QSSN_EMAIL_API_KEY=qssn_live_9e923f45d56ede8ec89ded81b13ccc246823cbb1bff67d31d07538c584a424fe
QSSN_EMAIL_API_URL=https://qssn-d1-api.gastonsoftwaresolutions234.workers.dev/api/v1/emails/send
```

---

## 🔧 LIKELY CAUSES

### 1. Port Binding Issue
**Problem:** App trying to bind to wrong port

**Solution:** Render sets `PORT` environment variable automatically. Our optimized Dockerfile should handle this.

**Check logs for:**
```
Failed to bind to 0.0.0.0:8080
```

---

### 2. Memory Issues
**Problem:** App using too much memory

**Solution:** Our optimizations reduced memory to 384MB max

**Check logs for:**
```
OutOfMemoryError
Killed
```

---

### 3. Database Connection
**Problem:** Can't connect to PostgreSQL

**Solution:** Verify database credentials

**Check logs for:**
```
Connection refused
Authentication failed
Unknown database
```

---

### 4. Health Check Failing
**Problem:** `/actuator/health` not responding

**Solution:** We added Spring Boot Actuator

**Check logs for:**
```
Health check failed
Endpoint not found
```

---

## 📋 STEP-BY-STEP FIX

### Option 1: Force Rebuild (Recommended)

1. **Go to Render Dashboard**
   - https://dashboard.render.com

2. **Click on your service**
   - "pahappa-attendance-system"

3. **Click "Manual Deploy"**
   - Select "Clear build cache & deploy"
   - This forces a complete rebuild

4. **Wait 5-7 minutes**
   - Watch the logs
   - Look for "Deploy live" message

5. **Test the URL**
   - https://pahappa-attendance-system.onrender.com

---

### Option 2: Check Logs First

1. **Open Logs Tab**
   - Look at the last 100 lines

2. **Find the Error**
   - Copy the error message

3. **Common Fixes:**

   **If you see "Port 8080 already in use":**
   - This shouldn't happen with our fix
   - Try manual redeploy

   **If you see "OutOfMemoryError":**
   - Our optimizations should fix this
   - Try manual redeploy

   **If you see "Database connection failed":**
   - Check environment variables
   - Verify database is running

   **If you see "Health check timeout":**
   - Increase health check timeout in render.yaml
   - Or remove health check temporarily

---

## 🎯 QUICK FIX: Remove Health Check

If health check is causing issues:

1. **Edit render.yaml:**
   ```yaml
   services:
     - type: web
       name: pahappa-attendance-system
       env: docker
       # Comment out or remove this line:
       # healthCheckPath: /actuator/health
   ```

2. **Commit and push:**
   ```bash
   git add render.yaml
   git commit -m "Remove health check temporarily"
   git push origin main
   ```

3. **Wait for redeploy**

---

## 📊 What Logs Should Show (Success)

When working correctly, logs should show:

```
Starting Spring Boot application...
Tomcat started on port(s): 10000 (http)
Started UserAuthApplication in 25.3 seconds
```

---

## 🚀 AFTER IT'S RUNNING

Once you see "Running" status:

1. **Test the URL**
   - https://pahappa-attendance-system.onrender.com
   - Should load in 25-30 seconds (first time)

2. **Set up UptimeRobot**
   - Follow UPTIMEROBOT_SETUP.md
   - Keep app warm

3. **Monitor Performance**
   - Check Render metrics
   - Watch response times

---

## 💡 PREVENTION

To avoid this in future:

1. **Always check logs after deploy**
2. **Test locally before pushing**
3. **Use manual deploy for important changes**
4. **Keep UptimeRobot running**

---

## 📞 NEED HELP?

**Share these with me:**
1. Last 50 lines of Render logs
2. Screenshot of service status
3. Any error messages you see

**Render Support:**
- Dashboard: https://dashboard.render.com
- Docs: https://render.com/docs
- Community: https://community.render.com

---

## ✅ CHECKLIST

Before asking for help, verify:
- [ ] Checked Render logs for errors
- [ ] Tried manual redeploy
- [ ] Verified environment variables
- [ ] Confirmed database is running
- [ ] Waited at least 5 minutes after deploy
- [ ] Cleared browser cache

---

## 🎯 MOST LIKELY FIX

**Try this first:**

1. Go to Render dashboard
2. Click "Manual Deploy"
3. Select "Clear build cache & deploy"
4. Wait 5-7 minutes
5. Check if status changes to "Running"

This fixes 90% of deployment issues!
