# Render Deployment Guide for Pahappa Attendance System

This guide provides step-by-step instructions for deploying the Spring Boot Attendance System to Render.

## 📋 Prerequisites

Before deploying to Render, ensure you have:

1. **GitHub Account** - Your code must be in a GitHub repository
2. **Render Account** - Sign up at [render.com](https://render.com)
3. **Email Service Credentials** - Gmail or other SMTP service for notifications
4. **Git** - Installed on your local machine

## 🚀 Deployment Steps

### Step 1: Prepare Your Repository

1. **Commit all changes to Git:**
   ```bash
   git add .
   git commit -m "Configure for Render deployment"
   git push origin main
   ```

2. **Verify these files are in your repository:**
   - `render.yaml` - Render blueprint configuration
   - `mvnw` and `mvnw.cmd` - Maven wrapper scripts
   - `.mvn/wrapper/maven-wrapper.properties` - Maven wrapper config
   - `src/main/resources/application-production.properties` - Production config
   - `pom.xml` - Updated with PostgreSQL dependency

### Step 2: Create Render Account

1. Go to [render.com](https://render.com)
2. Sign up using your GitHub account
3. Authorize Render to access your repositories

### Step 3: Deploy Using Blueprint

1. **From Render Dashboard:**
   - Click **"New +"** button
   - Select **"Blueprint"**
   - Connect your GitHub repository
   - Select the repository containing your application
   - Render will automatically detect `render.yaml`

2. **Review the Blueprint:**
   - Service Name: `pahappa-attendance-system`
   - Database Name: `pahappa-attendance-db`
   - Region: Oregon (or your preferred region)
   - Plan: Free tier

3. **Click "Apply"** to create the services

### Step 4: Configure Environment Variables

After the blueprint is applied, configure the following environment variables in the Render dashboard:

#### Database Configuration (Auto-configured)
These are automatically set by Render when you use the blueprint:
- `DATABASE_URL` - PostgreSQL connection string (auto-generated)
- `SPRING_DATASOURCE_URL` - Will use DATABASE_URL
- `SPRING_DATASOURCE_USERNAME` - Database username (auto-generated)
- `SPRING_DATASOURCE_PASSWORD` - Database password (auto-generated)

#### Email Configuration (Required - Manual Setup)
Go to your web service settings → Environment tab and add:

```
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
MAIL_FROM=noreply@pahappa.com
MAIL_ENABLED=true
```

**For Gmail:**
1. Enable 2-Factor Authentication on your Google account
2. Generate an App Password: https://myaccount.google.com/apppasswords
3. Use the generated 16-character password as `MAIL_PASSWORD`

#### Application Configuration (Optional)
```
APP_ADMIN_EMAIL=admin@pahappa.com
SPRING_PROFILES_ACTIVE=production
```

### Step 5: Database Setup

The database will be automatically created, but you need to create an admin user:

1. **Access Render Shell:**
   - Go to your web service in Render dashboard
   - Click **"Shell"** tab
   - Run database commands using the PostgreSQL client

2. **Create Admin User (After first user registration):**
   ```sql
   -- First, register a user through the web interface
   -- Then promote them to admin:
   UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
   ```

### Step 6: Verify Deployment

1. **Check Build Logs:**
   - Go to your web service
   - Click **"Logs"** tab
   - Verify the build completes successfully
   - Look for: `Started UserAuthApplication in X.XXX seconds`

2. **Access Your Application:**
   - Your app will be available at: `https://pahappa-attendance-system.onrender.com`
   - Or the custom URL shown in your Render dashboard

3. **Test Key Features:**
   - Registration: `https://your-app.onrender.com/register`
   - Login: `https://your-app.onrender.com/login`
   - Health Check: `https://your-app.onrender.com/actuator/health`

## 🔧 Configuration Details

### Database Migration from MySQL to PostgreSQL

The application now supports both MySQL (local development) and PostgreSQL (production on Render):

**Key Changes:**
- Added PostgreSQL driver dependency in `pom.xml`
- Created `application-production.properties` with PostgreSQL configuration
- Hibernate will automatically create tables on first run

**Data Types Mapping:**
- MySQL `DATETIME` → PostgreSQL `TIMESTAMP`
- MySQL `BIGINT` → PostgreSQL `BIGINT`
- MySQL `VARCHAR` → PostgreSQL `VARCHAR`

### Environment-Specific Configuration

**Local Development (MySQL):**
```properties
# Uses: src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/userdb
```

**Production (PostgreSQL on Render):**
```properties
# Uses: src/main/resources/application-production.properties
spring.datasource.url=${DATABASE_URL}
spring.profiles.active=production
```

## 🐛 Troubleshooting

### Build Failures

**Issue:** Maven build fails
```
Solution:
1. Check build logs in Render dashboard
2. Verify Java 17 is being used
3. Ensure all dependencies are in pom.xml
4. Check mvnw has execute permissions
```

**Issue:** "mvnw: Permission denied"
```bash
# Fix locally and push:
git update-index --chmod=+x mvnw
git commit -m "Make mvnw executable"
git push
```

### Database Connection Issues

**Issue:** Cannot connect to database
```
Solution:
1. Verify DATABASE_URL is set correctly
2. Check database service is running in Render
3. Ensure PostgreSQL driver is in pom.xml
4. Review connection pool settings
```

**Issue:** Tables not created
```
Solution:
1. Check spring.jpa.hibernate.ddl-auto=update in production properties
2. Review application logs for Hibernate errors
3. Verify database permissions
```

### Email Notification Issues

**Issue:** Emails not sending
```
Solution:
1. Verify MAIL_USERNAME and MAIL_PASSWORD are set
2. For Gmail, ensure App Password is used (not regular password)
3. Check MAIL_ENABLED=true
4. Review logs for SMTP errors
```

### Application Crashes

**Issue:** Application starts but crashes
```
Solution:
1. Check memory limits (free tier has 512MB)
2. Review JAVA_TOOL_OPTIONS: -Xmx512m -Xms256m
3. Check for memory leaks in logs
4. Reduce connection pool size if needed
```

### Slow Performance

**Issue:** Application is slow to respond
```
Solution:
1. Free tier services sleep after 15 minutes of inactivity
2. First request after sleep takes 30-60 seconds
3. Consider upgrading to paid tier for always-on service
4. Optimize database queries
5. Enable caching where appropriate
```

## 📊 Monitoring

### Health Checks

Render automatically monitors your application using:
- **Health Check Path:** `/actuator/health`
- **Expected Response:** `{"status":"UP"}`

### Logs

Access logs in Render dashboard:
1. Go to your web service
2. Click **"Logs"** tab
3. View real-time application logs
4. Filter by log level (INFO, WARN, ERROR)

### Metrics

Monitor your application:
- **CPU Usage** - Available in Render dashboard
- **Memory Usage** - Check for memory leaks
- **Response Times** - Monitor via logs
- **Database Connections** - Check connection pool metrics

## 🔒 Security Best Practices

1. **Environment Variables:**
   - Never commit sensitive data to Git
   - Use Render's environment variables for secrets
   - Rotate credentials regularly

2. **Database Security:**
   - Use strong passwords (auto-generated by Render)
   - Limit database access to your application only
   - Regular backups (available in paid tiers)

3. **Application Security:**
   - Keep dependencies updated
   - Enable HTTPS (automatic on Render)
   - Use secure session management
   - Implement rate limiting for APIs

## 💰 Cost Considerations

### Free Tier Limitations

**Web Service:**
- 750 hours/month free
- Sleeps after 15 minutes of inactivity
- 512MB RAM
- Shared CPU

**Database:**
- 90 days free trial
- 1GB storage
- Expires after 90 days (requires upgrade)

### Upgrading

When you need more resources:
1. Go to service settings in Render
2. Click **"Upgrade"**
3. Choose appropriate plan
4. Update billing information

## 🔄 Continuous Deployment

Render automatically deploys when you push to your main branch:

1. **Make changes locally**
2. **Commit and push:**
   ```bash
   git add .
   git commit -m "Your changes"
   git push origin main
   ```
3. **Render automatically:**
   - Detects the push
   - Builds the application
   - Runs tests (if configured)
   - Deploys the new version
   - Performs health checks

## 📚 Additional Resources

- [Render Documentation](https://render.com/docs)
- [Spring Boot on Render](https://render.com/docs/deploy-spring-boot)
- [PostgreSQL on Render](https://render.com/docs/databases)
- [Environment Variables](https://render.com/docs/environment-variables)

## 🆘 Support

If you encounter issues:

1. **Check Render Status:** https://status.render.com
2. **Review Documentation:** https://render.com/docs
3. **Community Forum:** https://community.render.com
4. **Contact Support:** support@render.com (paid plans)

## 📝 Deployment Checklist

Before going live, verify:

- [ ] All environment variables are set
- [ ] Database is connected and tables are created
- [ ] Email notifications are working
- [ ] Health check endpoint responds correctly
- [ ] User registration works
- [ ] User login works
- [ ] Attendance tracking works
- [ ] Admin panel is accessible
- [ ] QR code generation works
- [ ] SSL/HTTPS is enabled (automatic on Render)
- [ ] Custom domain configured (if applicable)
- [ ] Monitoring is set up
- [ ] Backup strategy is in place

## 🎉 Success!

Your Pahappa Attendance System is now deployed on Render!

**Next Steps:**
1. Share the URL with your team
2. Create admin users
3. Configure work schedules
4. Monitor application performance
5. Gather user feedback
6. Plan for scaling if needed

---

**Deployed with ❤️ on Render**