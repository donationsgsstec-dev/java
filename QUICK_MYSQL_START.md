# Quick MySQL Start Guide

Your MySQL is located at: `C:\Users\Dell\Desktop\internship\mysql-9.7.0-winx64`

## First Time Setup (Do This Once)

1. **Right-click `setup-mysql.bat`** in your project folder
2. Select **"Run as administrator"**
3. Wait for it to complete (creates configuration and initializes database)

## Starting MySQL (Every Time You Need It)

**Option 1: Using the Batch File (Easiest)**
- Double-click **`start-mysql.bat`** in your project folder
- Keep the window open (MySQL is running)
- Press `Ctrl+C` to stop MySQL when done

**Option 2: Manual Command**
```cmd
cd C:\mysql-9.7.0-winx64\mysql-9.7.0-winx64\bin
mysqld --console
```

## Running Your Application

1. **Start MySQL** (using one of the methods above)
2. **Open a NEW Command Prompt** (keep MySQL running in the first one)
3. **Run your application**:
   ```cmd
   cd C:\Users\Dell\Desktop\internship
   .\apache-maven-3.9.16\bin\mvn.cmd spring-boot:run
   ```

## Verify MySQL is Running

Open a new Command Prompt and run:
```cmd
netstat -an | findstr 3306
```

You should see: `TCP    0.0.0.0:3306    0.0.0.0:0    LISTENING`

## Connect to MySQL (Optional)

```cmd
cd C:\mysql-9.7.0-winx64\mysql-9.7.0-winx64\bin
mysql -u root
```

## Stop MySQL

Press `Ctrl+C` in the window where MySQL is running.

## Troubleshooting

### "Access denied" or "Can't connect"
- Make sure MySQL is running (check with `netstat -an | findstr 3306`)
- Restart MySQL

### "Port 3306 already in use"
```cmd
# Find what's using port 3306
netstat -ano | findstr 3306

# Kill the process (replace PID with actual number)
taskkill /PID <PID> /F
```

### Need to Reinitialize MySQL
1. Stop MySQL
2. Delete the data folder: `C:\Users\Dell\Desktop\internship\mysql-9.7.0-winx64\data`
3. Run `setup-mysql.bat` again as Administrator

---

**That's it!** Once MySQL is running, your Spring Boot app will automatically create the database and tables.