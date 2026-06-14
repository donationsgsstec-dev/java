# Railway Quick Start - Deploy Now!

Your code is ready at: https://github.com/donationsgsstec-dev/java

## 🚀 Deploy in 10 Minutes

### Step 1: Sign Up for Railway (2 minutes)

1. Go to https://railway.app
2. Click **"Login"** or **"Start a New Project"**
3. Click **"Login with GitHub"**
4. Authorize Railway to access your GitHub
5. **Add your credit card** (required - won't be charged, just verification)

---

### Step 2: Create New Project (1 minute)

1. Click **"New Project"**
2. Select **"Deploy from GitHub repo"**
3. Choose: **donationsgsstec-dev/java**
4. Railway will start building automatically!

---

### Step 3: Add PostgreSQL Database (1 minute)

1. In your project, click **"New"** button
2. Select **"Database"**
3. Choose **"PostgreSQL"**
4. Railway creates it instantly
5. Database credentials are auto-generated

---

### Step 4: Configure Environment Variables (3 minutes)

1. Click on your **web service** (the one building)
2. Go to **"Variables"** tab
3. Click **"+ New Variable"**
4. Add these one by one:

```bash
SPRING_PROFILES_ACTIVE=production

SPRING_DATASOURCE_URL=postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}

SPRING_DATASOURCE_USERNAME=${{Postgres.PGUSER}}

SPRING_DATASOURCE_PASSWORD=${{Postgres.PGPASSWORD}}

QSSN_EMAIL_API_KEY=qssn_live_9e923f45d56ede8ec89ded81b13ccc246823cbb1bff67d31d07538c584a424fe

QSSN_EMAIL_API_URL=https://qssn-d1-api.gastonsoftwaresolutions234.workers.dev/api/v1/emails/send

QSSN_EMAIL_FROM_NAME=Pahappa Attendance System

QSSN_EMAIL_ENABLED=true

APP_ADMIN_EMAIL=admin@pahappa.com
```

**Important:** Use the `${{Postgres.XXX}}` syntax - Railway will replace these with actual database values!

---

### Step 5: Generate Public URL (1 minute)

1. Go to **"Settings"** tab
2. Scroll to **"Networking"** section
3. Click **"Generate Domain"**
4. You'll get: `your-app-name.up.railway.app`
5. Copy this URL!

---

### Step 6: Wait for Deployment (2-3 minutes)

1. Go to **"Deployments"** tab
2. Watch the build logs
3. Look for:
   ```
   ✓ Building
   ✓ Deploying
   ✓ Success
   ```
4. Your app is now live!

---

## ✅ Test Your Deployment

1. Open your Railway URL: `https://your-app.up.railway.app`
2. You should see the login page
3. Try registering a new user
4. Test the attendance features

---

## 📊 What You Get

**Performance:**
- ❌ No cold starts (always running!)
- ⚡ Fast response times (1-3 seconds)
- 🚀 Quick deployments (2-3 minutes)
- 💪 Better than Render

**Free Tier:**
- $5 credit per month
- Covers your app + database
- Resets monthly
- No surprise charges (if you set limits)

---

## 🔧 Important Settings

### Set Spending Limit (Recommended)

1. Go to **Account Settings**
2. Click **"Usage"**
3. Set **"Usage Limit"** to **$5**
4. This prevents overage charges

### Enable Health Checks

1. In your service settings
2. Go to **"Health Checks"**
3. Set path: `/actuator/health`
4. Timeout: 300 seconds

---

## 💡 Pro Tips

### 1. Monitor Usage
- Check dashboard daily
- Watch credit consumption
- Your app should use ~$4-5/month

### 2. View Logs
- Click **"Deployments"** tab
- Select latest deployment
- Click **"View Logs"**
- Monitor for errors

### 3. Redeploy if Needed
- Click **"Deployments"** tab
- Click **"⋮"** menu
- Select **"Redeploy"**

---

## 🚨 Troubleshooting

### Build Fails
**Check:**
- Dockerfile exists
- All files pushed to GitHub
- Build logs for errors

**Fix:**
- Click "Redeploy"
- Check GitHub repo

### App Won't Start
**Check:**
- Environment variables set correctly
- Database is running
- Deployment logs

**Fix:**
- Verify all variables
- Check database connection
- Redeploy

### Can't Access URL
**Check:**
- Domain generated
- Deployment successful
- No errors in logs

**Fix:**
- Generate domain again
- Wait 2-3 minutes
- Clear browser cache

---

## 📱 Railway Mobile App

Monitor your app on the go:
- **iOS:** Search "Railway" in App Store
- **Android:** Search "Railway" in Play Store

Get notifications for:
- Deployment status
- Usage alerts
- Error notifications

---

## 💰 Cost Breakdown

**Estimated Monthly Cost:**
```
Web Service:     $3.50
PostgreSQL:      $1.00
Bandwidth:       $0.50
-----------------------
Total:           $5.00 (covered by free credit!)
```

**If you exceed $5:**
- You'll be charged for overage
- Set usage limit to prevent this
- Monitor usage regularly

---

## 🎯 Next Steps After Deployment

1. **Test Everything:**
   - Register users
   - Login/logout
   - QR code features
   - Attendance tracking
   - Admin features

2. **Set Up Monitoring:**
   - Enable Railway alerts
   - Check logs regularly
   - Monitor performance

3. **Share Your URL:**
   - Give to team members
   - Use for presentations
   - Add to documentation

4. **Keep Updated:**
   - Push changes to GitHub
   - Railway auto-deploys
   - Monitor deployments

---

## 🔄 Auto-Deployment

Railway automatically deploys when you push to GitHub:

```bash
# Make changes locally
git add .
git commit -m "Your changes"
git push railway main

# Railway detects push and deploys automatically!
```

---

## 📞 Need Help?

**Railway Support:**
- Dashboard: https://railway.app/dashboard
- Docs: https://docs.railway.app
- Discord: https://discord.gg/railway
- Twitter: @Railway

**Your Repository:**
- https://github.com/donationsgsstec-dev/java

---

## ✨ Summary

**What You Did:**
1. ✅ Pushed code to donationsgsstec-dev/java
2. ✅ Ready to deploy to Railway

**What's Next:**
1. Sign up for Railway (add credit card)
2. Deploy from GitHub repo
3. Add PostgreSQL database
4. Configure environment variables
5. Generate public URL
6. Test your app!

**Expected Result:**
- Fast, always-on application
- No cold starts
- Professional hosting
- $5/month (free credit covers it!)

---

## 🎉 Ready to Deploy?

Go to: https://railway.app

Click: **"Start a New Project"**

Select: **"Deploy from GitHub repo"**

Choose: **donationsgsstec-dev/java**

**Your app will be live in 10 minutes!**
