# Quick Deploy to Render - 5 Minutes Setup

This is a condensed guide to get your application deployed to Render quickly.

## 🚀 Quick Steps

### 1. Push to GitHub (2 minutes)

```bash
# Make scripts executable
git update-index --chmod=+x mvnw
git update-index --chmod=+x build.sh
git update-index --chmod=+x start.sh

# Commit and push
git add .
git commit -m "Configure for Render deployment"
git push origin main
```

### 2. Deploy on Render (2 minutes)

1. Go to [render.com](https://render.com) and sign in with GitHub
2. Click **"New +"** → **"Blueprint"**
3. Select your repository
4. Click **"Apply"** (Render detects `render.yaml` automatically)

### 3. Configure Environment Variables (1 minute)

In Render dashboard, go to your web service → **Environment** tab and add:

```
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-gmail-app-password
MAIL_FROM=noreply@pahappa.com
APP_ADMIN_EMAIL=admin@pahappa.com
```

**Get Gmail App Password:**
1. Enable 2FA on Google account
2. Visit: https://myaccount.google.com/apppasswords
3. Generate password for "Mail"
4. Use the 16-character password

### 4. Wait for Deployment

- Build takes ~5-10 minutes
- Watch logs in Render dashboard
- Look for: `Started UserAuthApplication`

### 5. Access Your App

Your app will be at: `https://pahappa-attendance-system.onrender.com`

## ✅ Quick Test

1. Visit: `https://your-app.onrender.com/register`
2. Register a new user
3. Check your email for welcome message
4. Login and test attendance features

## 🔧 Create Admin User

After registering your first user:

1. Go to Render dashboard → Your database service
2. Click **"Connect"** → Copy connection command
3. Run in terminal:
   ```bash
   psql <connection-string>
   ```
4. Execute:
   ```sql
   UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
   ```

## 📊 Monitor

- **Logs:** Render Dashboard → Your Service → Logs tab
- **Health:** `https://your-app.onrender.com/actuator/health`
- **Status:** Should return `{"status":"UP"}`

## ⚠️ Important Notes

- **Free Tier:** App sleeps after 15 minutes of inactivity
- **First Request:** Takes 30-60 seconds after sleep
- **Database:** Free for 90 days, then requires upgrade
- **Always-On:** Upgrade to paid plan ($7/month)

## 🐛 Common Issues

**Build fails?**
- Check Java 17 is available
- Verify `mvnw` has execute permissions
- Review build logs in Render

**Can't connect to database?**
- Wait for database to finish provisioning
- Check DATABASE_URL is set automatically
- Verify PostgreSQL dependency in pom.xml

**Emails not sending?**
- Verify Gmail App Password (not regular password)
- Check MAIL_USERNAME and MAIL_PASSWORD are set
- Enable "Less secure app access" if needed

## 📚 Full Documentation

For detailed information, see [`RENDER_DEPLOYMENT.md`](RENDER_DEPLOYMENT.md)

---

**That's it! Your app is now live on Render! 🎉**