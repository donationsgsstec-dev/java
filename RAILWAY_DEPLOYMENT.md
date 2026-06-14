# Railway Deployment Guide

## ⚠️ IMPORTANT: Credit Card Required

Railway requires a credit card for verification, even for the free $5 credit. If you don't have a credit card, stick with Render.

**Free Tier:**
- $5 free credit per month
- No cold starts (always running!)
- Fast deployment (2-3 minutes)
- PostgreSQL included

---

## 🚀 STEP-BY-STEP DEPLOYMENT

### Step 1: Sign Up for Railway

1. Go to https://railway.app
2. Click **"Start a New Project"**
3. Sign in with GitHub
4. **Add credit card** (required for verification - won't be charged)
5. Verify your account

---

### Step 2: Create New Project

1. Click **"New Project"**
2. Select **"Deploy from GitHub repo"**
3. Choose your repository: **GSS-creator/java**
4. Railway will auto-detect it's a Docker project

---

### Step 3: Add PostgreSQL Database

1. In your project dashboard, click **"New"**
2. Select **"Database"**
3. Choose **"PostgreSQL"**
4. Railway creates the database automatically
5. Note: Database credentials are auto-generated

---

### Step 4: Configure Environment Variables

Railway auto-detects some variables, but you need to add these:

1. Click on your **web service**
2. Go to **"Variables"** tab
3. Add these variables:

```bash
# Spring Profile
SPRING_PROFILES_ACTIVE=production

# Database (Railway provides these automatically as DATABASE_URL)
# But we need them in Spring format
SPRING_DATASOURCE_URL=postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}
SPRING_DATASOURCE_USERNAME=${{Postgres.PGUSER}}
SPRING_DATASOURCE_PASSWORD=${{Postgres.PGPASSWORD}}

# Email Service
QSSN_EMAIL_API_KEY=qssn_live_9e923f45d56ede8ec89ded81b13ccc246823cbb1bff67d31d07538c584a424fe
QSSN_EMAIL_API_URL=https://qssn-d1-api.gastonsoftwaresolutions234.workers.dev/api/v1/emails/send
QSSN_EMAIL_FROM_NAME=Pahappa Attendance System
QSSN_EMAIL_ENABLED=true

# Admin Email
APP_ADMIN_EMAIL=admin@pahappa.com

# JVM Options (optional - Railway has good defaults)
JAVA_TOOL_OPTIONS=-Xmx384m -Xms192m -XX:+UseSerialGC -XX:TieredStopAtLevel=1
```

---

### Step 5: Configure Build Settings

Railway should auto-detect your Dockerfile, but verify:

1. Go to **"Settings"** tab
2. Under **"Build"**:
   - Builder: **Dockerfile**
   - Dockerfile Path: **Dockerfile**
3. Under **"Deploy"**:
   - Start Command: (leave empty - Dockerfile handles it)
   - Health Check Path: **/actuator/health**

---

### Step 6: Deploy

1. Click **"Deploy"**
2. Railway will:
   - Build your Docker image (5-7 minutes)
   - Deploy to their infrastructure
   - Assign a public URL
3. Watch the deployment logs

---

### Step 7: Get Your URL

1. Go to **"Settings"** tab
2. Under **"Networking"**:
   - Click **"Generate Domain"**
   - You'll get: `your-app.up.railway.app`
3. Your app is now live!

---

## 📊 Railway vs Render Comparison

| Feature | Railway | Render |
|---------|---------|--------|
| **Cold Starts** | ❌ None | ✅ 50s+ |
| **Always Running** | ✅ Yes | ❌ No |
| **Build Time** | 2-3 min | 5-7 min |
| **Startup Time** | 60-90s | 60-90s |
| **Free Tier** | $5 credit/month | Truly free |
| **Credit Card** | ✅ Required | ❌ Not required |
| **Database** | ✅ PostgreSQL | ✅ PostgreSQL |
| **Custom Domain** | ✅ Yes | ✅ Yes |
| **Performance** | 🏆 Excellent | ⚠️ Good |

---

## 💰 Cost Estimate

**Your App on Railway:**
- Web Service: ~$3-4/month
- PostgreSQL: ~$1/month
- **Total: ~$4-5/month**

**Free Credit:** $5/month covers everything!

---

## 🎯 Railway Configuration File (Optional)

Create `railway.json` for better control:

```json
{
  "build": {
    "builder": "DOCKERFILE",
    "dockerfilePath": "Dockerfile"
  },
  "deploy": {
    "startCommand": "",
    "healthcheckPath": "/actuator/health",
    "healthcheckTimeout": 300,
    "restartPolicyType": "ON_FAILURE",
    "restartPolicyMaxRetries": 3
  }
}
```

---

## 🔧 Troubleshooting

### Build Fails
1. Check Railway build logs
2. Verify Dockerfile is correct
3. Try "Redeploy" button

### App Won't Start
1. Check deployment logs
2. Verify environment variables
3. Check database connection

### Database Connection Error
1. Verify PostgreSQL service is running
2. Check database credentials in variables
3. Use Railway's reference variables: `${{Postgres.PGHOST}}`

---

## 📈 Expected Performance

### Railway Performance:
- **Build:** 2-3 minutes
- **Startup:** 60-90 seconds
- **Response:** 1-3 seconds (always warm!)
- **No cold starts!**

### Render Performance (for comparison):
- **Build:** 5-7 minutes
- **Startup:** 60-90 seconds
- **Response:** 1-3s (warm) or 50s+ (cold)
- **Cold starts after 15 min idle**

---

## 🚀 Quick Start Commands

If you prefer CLI deployment:

```bash
# Install Railway CLI
npm i -g @railway/cli

# Login
railway login

# Link to project
railway link

# Deploy
railway up
```

---

## ✅ Post-Deployment Checklist

After deployment:
- [ ] App is accessible at Railway URL
- [ ] Database connection works
- [ ] Can register new users
- [ ] Can log in
- [ ] QR code features work
- [ ] Email notifications work
- [ ] No errors in logs

---

## 🎓 Student Discount

If you're a student:
1. Apply for GitHub Student Developer Pack
2. Get extra Railway credits
3. Apply at: https://education.github.com/pack

---

## 💡 Pro Tips

### 1. Monitor Usage
- Check Railway dashboard daily
- Watch credit consumption
- Set up usage alerts

### 2. Optimize Costs
- Use smaller instance if possible
- Monitor database size
- Clean up old data

### 3. Backup Database
- Railway provides automatic backups
- Export data regularly
- Keep local backups

---

## 🔄 Migration from Render

If migrating from Render:

1. **Export Render Database:**
   ```bash
   pg_dump $RENDER_DATABASE_URL > backup.sql
   ```

2. **Import to Railway:**
   ```bash
   psql $RAILWAY_DATABASE_URL < backup.sql
   ```

3. **Update DNS** (if using custom domain)

4. **Test thoroughly**

5. **Delete Render service** (optional)

---

## 📞 Support

**Railway Help:**
- Docs: https://docs.railway.app
- Discord: https://discord.gg/railway
- Twitter: @Railway

**Common Issues:**
- Build fails: Check Dockerfile
- App won't start: Check logs
- Database error: Verify credentials
- Out of credit: Add payment method

---

## 🎯 Decision Guide

**Choose Railway if:**
- ✅ You have a credit card
- ✅ You need no cold starts
- ✅ You want faster performance
- ✅ $5/month budget is okay

**Stick with Render if:**
- ✅ No credit card available
- ✅ Truly free hosting needed
- ✅ Can accept cold starts
- ✅ Use UptimeRobot to keep warm

---

## 🚨 IMPORTANT NOTES

1. **Credit Card Required:** Railway won't work without it
2. **Free Credit:** $5/month is usually enough
3. **Overage Charges:** If you exceed $5, you'll be charged
4. **Monitor Usage:** Check dashboard regularly
5. **Set Limits:** Configure spending limits in settings

---

## ✨ Summary

**Railway Advantages:**
- No cold starts (always fast!)
- Better performance
- Faster deployments
- Great developer experience

**Railway Disadvantages:**
- Requires credit card
- Not truly free (credit-based)
- Can incur charges if over limit

**Recommendation:**
If you have a credit card and want the best performance, Railway is excellent. Otherwise, optimized Render + UptimeRobot works well too!
