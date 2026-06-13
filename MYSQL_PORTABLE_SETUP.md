# MySQL Portable Setup Guide (No Installer, No Visual Studio)

This guide shows you how to set up MySQL using the ZIP archive - a portable version that doesn't require an installer or Visual Studio.

## Step 1: Download MySQL ZIP Archive

1. Go to: https://dev.mysql.com/downloads/mysql/
2. Select **Windows (x86, 64-bit), ZIP Archive**
3. Click **Download** (you can skip login by clicking "No thanks, just start my download")
4. Download the file (approximately 200-300 MB)

**Recommended version:** MySQL 8.0.x ZIP Archive

## Step 2: Extract MySQL

1. Extract the downloaded ZIP file to a location of your choice
2. **Recommended location:** `C:\mysql` or `C:\Users\Dell\Desktop\mysql`
3. After extraction, you should have a folder like: `C:\mysql\mysql-8.0.xx-winx64`

For this guide, we'll assume you extracted to: `C:\mysql\mysql-8.0.40-winx64`

## Step 3: Create MySQL Configuration File

Create a file named `my.ini` in the MySQL root directory (e.g., `C:\mysql\mysql-8.0.40-winx64\my.ini`):

```ini
[mysqld]
# Set the base directory
basedir=C:/mysql/mysql-8.0.40-winx64
# Set the data directory
datadir=C:/mysql/mysql-8.0.40-winx64/data
# Set the port
port=3306
# Set the default character set
character-set-server=utf8mb4
# Set the default authentication plugin
default-authentication-plugin=mysql_native_password
# Disable strict mode for easier development
sql_mode=NO_ENGINE_SUBSTITUTION

[mysql]
# Set the default character set for mysql client
default-character-set=utf8mb4

[client]
# Set the port for client connections
port=3306
```

**Important:** Update the paths in `my.ini` to match your actual MySQL location!

## Step 4: Initialize MySQL Data Directory

Open **Command Prompt as Administrator** and run:

```cmd
# Navigate to MySQL bin directory
cd C:\mysql\mysql-8.0.40-winx64\bin

# Initialize the data directory (this creates the root user)
mysqld --initialize-insecure --console
```

**What this does:**
- Creates the `data` directory
- Sets up system databases
- Creates root user with **no password** (for development)

**Note:** You'll see output showing the initialization process. Wait for it to complete.

## Step 5: Start MySQL Server

### Option A: Start Manually (Recommended for Testing)

```cmd
# In the same Command Prompt (as Administrator)
cd C:\mysql\mysql-8.0.40-winx64\bin

# Start MySQL server
mysqld --console
```

**Keep this window open!** MySQL is now running. You'll see logs in this window.

### Option B: Install as Windows Service (Recommended for Regular Use)

```cmd
# In Command Prompt as Administrator
cd C:\mysql\mysql-8.0.40-winx64\bin

# Install MySQL as a service
mysqld --install MySQL80

# Start the service
net start MySQL80
```

**To remove the service later:**
```cmd
net stop MySQL80
mysqld --remove MySQL80
```

## Step 6: Verify MySQL is Running

Open a **new Command Prompt** (keep the server running in the first one if using Option A):

```cmd
# Check if MySQL is listening on port 3306
netstat -an | findstr 3306
```

You should see: `TCP    0.0.0.0:3306    0.0.0.0:0    LISTENING`

## Step 7: Connect to MySQL

```cmd
# Navigate to MySQL bin directory
cd C:\mysql\mysql-8.0.40-winx64\bin

# Connect to MySQL (no password needed)
mysql -u root
```

You should see the MySQL prompt: `mysql>`

### Set Root Password (Optional but Recommended)

```sql
-- In the MySQL prompt
ALTER USER 'root'@'localhost' IDENTIFIED BY 'your_password';
FLUSH PRIVILEGES;
EXIT;
```

If you set a password, update your Spring Boot application's `application.properties`:
```properties
spring.datasource.password=your_password
```

## Step 8: Create Database (Optional - App Does This Automatically)

```cmd
# Connect to MySQL
mysql -u root -p

# Create database
CREATE DATABASE userdb;
EXIT;
```

**Note:** The Spring Boot application will create this automatically, so this step is optional.

## Step 9: Run Your Spring Boot Application

Now that MySQL is running, start your application:

```cmd
cd C:\Users\Dell\Desktop\internship
.\apache-maven-3.9.16\bin\mvn.cmd spring-boot:run
```

The application will:
1. Connect to MySQL on `localhost:3306`
2. Automatically create the `userdb` database
3. Create tables `users` and `attendance`
4. Start on http://localhost:8080

## Quick Start Script

Create a batch file `start-mysql.bat` in your MySQL directory:

```batch
@echo off
echo Starting MySQL Server...
cd /d C:\mysql\mysql-8.0.40-winx64\bin
start "MySQL Server" mysqld --console
echo MySQL Server started!
echo Keep this window open while using MySQL.
pause
```

Double-click this file to start MySQL quickly!

## Stop MySQL Server

### If Running Manually (Option A):
- Press `Ctrl+C` in the Command Prompt where MySQL is running
- Or close the Command Prompt window

### If Running as Service (Option B):
```cmd
net stop MySQL80
```

## Troubleshooting

### Error: "mysqld: Can't create directory" or "Can't find file"

**Solution:** Make sure paths in `my.ini` match your actual MySQL location. Use forward slashes `/` or double backslashes `\\`.

### Error: "Port 3306 is already in use"

**Solution:** Another MySQL instance is running. Stop it first:
```cmd
# Find the process using port 3306
netstat -ano | findstr 3306

# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F
```

### Error: "Access denied for user 'root'@'localhost'"

**Solution:** 
1. Stop MySQL
2. Start with skip-grant-tables:
   ```cmd
   mysqld --skip-grant-tables --console
   ```
3. In another terminal:
   ```cmd
   mysql -u root
   FLUSH PRIVILEGES;
   ALTER USER 'root'@'localhost' IDENTIFIED BY '';
   EXIT;
   ```
4. Restart MySQL normally

### MySQL Won't Start

**Check the error log:**
```
C:\mysql\mysql-8.0.40-winx64\data\*.err
```

**Common fixes:**
1. Delete the `data` directory and reinitialize:
   ```cmd
   rmdir /s C:\mysql\mysql-8.0.40-winx64\data
   mysqld --initialize-insecure --console
   ```

2. Check if `my.ini` paths are correct

3. Run Command Prompt as Administrator

## Adding MySQL to PATH (Optional)

To run MySQL commands from anywhere:

1. Press `Win + X` → System
2. Click "Advanced system settings"
3. Click "Environment Variables"
4. Under "System variables", find "Path"
5. Click "Edit" → "New"
6. Add: `C:\mysql\mysql-8.0.40-winx64\bin`
7. Click "OK" on all dialogs
8. Restart Command Prompt

Now you can run `mysql` from anywhere!

## Advantages of ZIP Archive

✅ No installer required
✅ No Visual Studio needed
✅ Portable - can move to different location
✅ Multiple versions can coexist
✅ Easy to remove (just delete the folder)
✅ Full control over configuration

## Next Steps

1. Start MySQL using one of the methods above
2. Run your Spring Boot application
3. Access http://localhost:8080
4. Register and login!

## Quick Reference

```cmd
# Start MySQL manually
cd C:\mysql\mysql-8.0.40-winx64\bin
mysqld --console

# Connect to MySQL
mysql -u root

# Check if MySQL is running
netstat -an | findstr 3306

# Stop MySQL (if running as service)
net stop MySQL80
```

---

**Need help?** Check [MYSQL_SETUP.md](MYSQL_SETUP.md) for more troubleshooting tips.