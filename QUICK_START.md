# Quick Start Guide

This guide will help you get the Spring Boot User Authentication Application up and running quickly.

---

## 🚀 Step-by-Step Installation

### Step 1: Install Maven (If Not Already Installed)

**Right-click** on [`install-maven.bat`](install-maven.bat) and select **"Run as administrator"**

This script will:
- ✅ Check if Java is installed
- ✅ Download Maven 3.9.6 automatically
- ✅ Install Maven to `C:\Program Files\Apache\Maven`
- ✅ Set up environment variables (MAVEN_HOME and PATH)
- ✅ Open a new command prompt with updated environment

**After installation:**
1. Close your current command prompt/PowerShell
2. Open a new command prompt
3. Verify Maven is installed:
   ```bash
   mvn --version
   ```

---

### Step 2: Build the Project

Open a **new** command prompt in the project directory and run:

```bash
run-maven.bat clean install
```

Or if Maven is properly installed:
```bash
mvn.cmd clean install
```

This will:
- Download all dependencies
- Compile the Java code
- Package the application

**Expected output:** `BUILD SUCCESS`

---

### Step 3: Run the Application

```bash
run-maven.bat spring-boot:run
```

Or:
```bash
mvn.cmd spring-boot:run
```

**Wait for:** `Started UserAuthApplication in X.XXX seconds`

---

### Step 4: Access the Application

Open your web browser and go to:

**🌐 http://localhost:8080**

You'll be automatically redirected to the login page.

---

## 📝 First Time Usage

### Register a New User

1. Click **"Register here"** on the login page
2. Fill in the registration form:
   - **Username**: testuser (3-50 characters)
   - **Email**: test@example.com (valid email format)
   - **First Name**: Test
   - **Last Name**: User
   - **Password**: password123 (minimum 6 characters)
3. Click **"Create Account"**
4. You'll be redirected to the login page with a success message

### Login

1. Enter your username: `testuser`
2. Enter your password: `password123`
3. Click **"Sign In"**
4. You'll be redirected to the home page

### Explore

- View your dashboard with user information
- Click **"Logout"** to sign out
- Try registering another user
- Test validation by entering invalid data

---

## 🔍 Testing the Application

### Test Registration Validation

Try these to see validation in action:

1. **Short username**: Enter `ab` (less than 3 characters)
2. **Invalid email**: Enter `notanemail`
3. **Short password**: Enter `12345` (less than 6 characters)
4. **Duplicate username**: Register same username twice
5. **Duplicate email**: Register same email twice

### Test Login

1. **Correct credentials**: Should login successfully
2. **Wrong password**: Should show error message
3. **Non-existent user**: Should show error message

### Test Security

1. **Try accessing home without login**: Should redirect to login
2. **Logout**: Should clear session and redirect to login
3. **Try accessing home after logout**: Should redirect to login

---

## 🗄️ View Database (Optional)

Access the H2 Database Console to see registered users:

1. Go to: **http://localhost:8080/h2-console**
2. Enter connection details:
   - **JDBC URL**: `jdbc:h2:mem:userdb`
   - **Username**: `sa`
   - **Password**: (leave empty)
3. Click **"Connect"**
4. Run query: `SELECT * FROM USERS;`
5. View all registered users and their encrypted passwords

---

## ⚠️ Common Issues

### Issue: "Maven is not installed"

**Solution**: Run [`install-maven.bat`](install-maven.bat) as administrator

### Issue: "Port 8080 already in use"

**Solution**: Stop the process using port 8080 or change the port in `application.properties`:
```properties
server.port=8081
```

### Issue: "ModuleNotFoundError: No module named 'pwd'"

**Solution**: You have a Python mvn package conflicting. Use:
```bash
run-maven.bat clean install
```
Instead of just `mvn`

### Issue: "BUILD FAILURE"

**Solution**: 
1. Check if Java 17+ is installed: `java -version`
2. Delete the `target` folder
3. Try again: `run-maven.bat clean install`

---

## 📚 More Information

- **Full Setup Guide**: See [`README.md`](README.md)
- **Troubleshooting**: See [`TROUBLESHOOTING.md`](TROUBLESHOOTING.md)
- **Testing Guide**: See [`TESTING_GUIDE.md`](TESTING_GUIDE.md)
- **Project Overview**: See [`PROJECT_SUMMARY.md`](PROJECT_SUMMARY.md)

---

## 🎯 Quick Command Reference

```bash
# Install Maven (run as administrator)
install-maven.bat

# Build project
run-maven.bat clean install

# Run application
run-maven.bat spring-boot:run

# Stop application
Press Ctrl+C in the terminal

# Access application
http://localhost:8080

# Access H2 console
http://localhost:8080/h2-console
```

---

## ✅ Success Checklist

- [ ] Maven installed successfully
- [ ] Project builds without errors
- [ ] Application starts successfully
- [ ] Can access http://localhost:8080
- [ ] Can register a new user
- [ ] Can login with registered user
- [ ] Can view home page
- [ ] Can logout successfully

---

## 🎉 You're All Set!

If all steps completed successfully, you now have a fully functional Spring Boot application with:
- ✅ User registration
- ✅ Secure login
- ✅ Password encryption
- ✅ Form validation
- ✅ Session management
- ✅ CSRF protection

**Enjoy exploring the application!**

---

## 💡 Next Steps

1. **Explore the code**: Check out the Java files to understand the implementation
2. **Run tests**: Follow the [`TESTING_GUIDE.md`](TESTING_GUIDE.md)
3. **Customize**: Modify the application to add your own features
4. **Learn**: Study the Spring Security configuration and JPA entities

---

**Need Help?** Check [`TROUBLESHOOTING.md`](TROUBLESHOOTING.md) for solutions to common problems.