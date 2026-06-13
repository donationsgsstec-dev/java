# How to Run the Application

## ⚠️ IMPORTANT: Maven Conflict Issue

You have a **Python `mvn` package** in your conda environment that conflicts with the real Apache Maven. When you type `mvn`, Python's version runs instead of Apache Maven.

---

## ✅ SOLUTION: Use These Commands

**IMPORTANT:** Make sure you're in the project directory `C:\Users\Dell\Desktop\internship` (NOT in the apache-maven folder)

### Option 1: Use the Local Maven Script (RECOMMENDED)

**For PowerShell (add .\ prefix):**
```powershell
# Make sure you're in the project directory
cd C:\Users\Dell\Desktop\internship

# Then run (note the .\ prefix for PowerShell)
.\use-local-maven.bat clean install
.\use-local-maven.bat spring-boot:run
```

**For Command Prompt (no .\ prefix needed):**
```cmd
# Make sure you're in the project directory
cd C:\Users\Dell\Desktop\internship

# Then run
use-local-maven.bat clean install
use-local-maven.bat spring-boot:run
```

This uses your Maven installation at: `C:\Users\Dell\Desktop\internship\apache-maven-3.9.16`

### Option 2: Use Full Path to Maven (PowerShell)

```powershell
# Make sure you're in the project directory
cd C:\Users\Dell\Desktop\internship

# Use .\ prefix for PowerShell
.\apache-maven-3.9.16\bin\mvn.cmd clean install
.\apache-maven-3.9.16\bin\mvn.cmd spring-boot:run
```

### Option 3: Use Full Path to Maven (Command Prompt)

```cmd
# Make sure you're in the project directory
cd C:\Users\Dell\Desktop\internship

# No .\ prefix needed in cmd
apache-maven-3.9.16\bin\mvn.cmd clean install
apache-maven-3.9.16\bin\mvn.cmd spring-boot:run
```

### Option 3: Deactivate Conda Environment

```bash
# Deactivate conda
conda deactivate

# Now use Maven normally
mvn clean install
mvn spring-boot:run

# Reactivate conda when done
conda activate clinic
```

### Option 4: Remove Python mvn Package (Permanent Fix)

```bash
# Remove the conflicting Python package
pip uninstall mvn

# Restart your terminal
# Now you can use Maven normally
mvn clean install
mvn spring-boot:run
```

---

## 🚀 Step-by-Step Instructions

### Step 0: Setup MySQL Database (REQUIRED)

**The application now uses MySQL instead of H2!**

1. **Install MySQL Server** (if not already installed)
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Ensure MySQL is running on `localhost:3306`

2. **Configure Database Credentials** (if needed)
   
   Edit [`src/main/resources/application.properties`](src/main/resources/application.properties):
   ```properties
   spring.datasource.username=root
   spring.datasource.password=your_password_here
   ```

3. **That's it!** The database `userdb` will be created automatically when you run the application.

📖 **For detailed MySQL setup and troubleshooting, see [MYSQL_SETUP.md](MYSQL_SETUP.md)**

### Step 1: Open a NEW Command Prompt

**Important:** Close any existing command prompts and open a fresh one in the project directory:

```bash
cd C:\Users\Dell\Desktop\internship
```

### Step 2: Build the Project

Choose ONE of these commands:

```bash
# Option A: Use local Maven script
use-local-maven.bat clean install

# Option B: Use full path
apache-maven-3.9.16\bin\mvn.cmd clean install

# Option C: Deactivate conda first
conda deactivate
mvn clean install
```

**Wait for:** `BUILD SUCCESS`

### Step 3: Run the Application

```bash
# Option A: Use local Maven script
use-local-maven.bat spring-boot:run

# Option B: Use full path
apache-maven-3.9.16\bin\mvn.cmd spring-boot:run

# Option C: If conda deactivated
mvn spring-boot:run
```

**Wait for:** `Started UserAuthApplication in X.XXX seconds`

### Step 4: Access the Application

Open your browser and go to:

**http://localhost:8080**

---

## 🧪 Quick Test

### Register a User

1. Go to: http://localhost:8080/register
2. Fill in:
   - Username: `testuser`
   - Email: `test@example.com`
   - First Name: `Test`
   - Last Name: `User`
   - Password: `password123`
3. Click "Create Account"

### Login

1. Go to: http://localhost:8080/login
2. Enter:
   - Username: `testuser`
   - Password: `password123`
3. Click "Sign In"

### View Home Page

You should see your dashboard with welcome message!

---

## 🛑 Stop the Application

Press `Ctrl+C` in the terminal where the application is running.

---

## 🔍 Verify Maven Installation

To check if Maven is properly accessible:

```bash
# This will fail (Python mvn package)
mvn --version

# This should work (real Maven)
apache-maven-3.9.16\bin\mvn.cmd --version

# Or use the script
use-local-maven.bat --version
```

---

## 📊 Expected Output

### During Build:
```
[INFO] Scanning for projects...
[INFO] 
[INFO] ----------------< com.pahappa:user-auth-app >----------------
[INFO] Building User Authentication Application 1.0.0
[INFO] --------------------------------[ war ]---------------------------------
...
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

### During Run:
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.3.0)

...
Started UserAuthApplication in 5.123 seconds (process running for 5.456)
```

---

## ❌ Common Errors and Solutions

### Error: "ModuleNotFoundError: No module named 'pwd'"

**Cause:** Python mvn package is running instead of Apache Maven

**Solution:** Use one of the methods above (use-local-maven.bat or full path)

### Error: "Port 8080 already in use"

**Solution:** 
```bash
# Find and kill the process
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Or change port in application.properties
server.port=8081
```

### Error: "JAVA_HOME is not set"

**Solution:**
```bash
# Set JAVA_HOME temporarily
set JAVA_HOME=C:\Program Files\Java\jdk-17

# Or set it permanently in System Environment Variables
```

---

## 🎯 Quick Command Reference

```bash
# Build project
use-local-maven.bat clean install

# Run application
use-local-maven.bat spring-boot:run

# Build without tests
use-local-maven.bat clean install -DskipTests

# Check Maven version
use-local-maven.bat --version

# Stop application
Ctrl+C
```

---

## 📱 Access Points

Once running, access these URLs:

- **Main Application**: http://localhost:8080
- **Registration**: http://localhost:8080/register
- **Login**: http://localhost:8080/login
- **Home (requires login)**: http://localhost:8080/home
- **Attendance**: http://localhost:8080/attendance

### MySQL Database Access

Use MySQL Workbench or command line:
```bash
mysql -u root -p
USE userdb;
SELECT * FROM users;
```

---

## ✅ Success Checklist

- [ ] MySQL Server is installed and running
- [ ] Database credentials configured in application.properties
- [ ] Opened NEW command prompt in project directory
- [ ] Used correct Maven command (use-local-maven.bat or full path)
- [ ] Build completed with "BUILD SUCCESS"
- [ ] Application started with "Started UserAuthApplication"
- [ ] Database `userdb` created automatically
- [ ] Can access http://localhost:8080
- [ ] Can register a new user
- [ ] Can login successfully
- [ ] Can view home page
- [ ] Can logout

---

## 💡 Pro Tips

1. **Ensure MySQL is running** before starting the application
2. **Always use a fresh terminal** after changing environment variables
3. **Use `use-local-maven.bat`** to avoid conda conflicts
4. **Check Java version** before building: `java -version` (should be 17+)
5. **Clear browser cache** if pages don't load correctly
6. **Use MySQL client** to verify users are being saved: `mysql -u root -p`

---

## 🆘 Still Having Issues?

1. Check [`TROUBLESHOOTING.md`](TROUBLESHOOTING.md) for detailed solutions
2. Verify Java is installed: `java -version`
3. Verify Maven exists: `dir apache-maven-3.9.16\bin\mvn.cmd`
4. Try deactivating conda: `conda deactivate`
5. Delete `target` folder and rebuild

---

## 📚 Additional Documentation

- [`MYSQL_SETUP.md`](MYSQL_SETUP.md) - MySQL database setup guide
- [`QUICK_START.md`](QUICK_START.md) - Quick start guide
- [`README.md`](README.md) - Full documentation
- [`TROUBLESHOOTING.md`](TROUBLESHOOTING.md) - Troubleshooting guide
- [`TESTING_GUIDE.md`](TESTING_GUIDE.md) - Testing instructions
- [`PROJECT_SUMMARY.md`](PROJECT_SUMMARY.md) - Project overview

---

**Remember:** Always use `use-local-maven.bat` or the full path to Maven to avoid the Python mvn conflict!