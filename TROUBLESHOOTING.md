# Troubleshooting Guide

This guide helps resolve common issues when running the Spring Boot User Authentication Application.

---

## 🔧 Maven Command Issues

### Issue: "ModuleNotFoundError: No module named 'pwd'" when running `mvn`

**Problem**: You have a Python package called `mvn` installed in your conda/Python environment that conflicts with the actual Maven command.

**Solution Options**:

#### Option 1: Use the Batch Script (Recommended for Windows)
```bash
# Instead of: mvn clean install
# Use:
run-maven.bat clean install

# Instead of: mvn spring-boot:run
# Use:
run-maven.bat spring-boot:run
```

#### Option 2: Use mvn.cmd directly
```bash
# Use the full Maven command name
mvn.cmd clean install
mvn.cmd spring-boot:run
```

#### Option 3: Deactivate Conda Environment
```bash
# Deactivate conda environment temporarily
conda deactivate

# Run Maven commands
mvn clean install
mvn spring-boot:run

# Reactivate conda when done
conda activate clinic
```

#### Option 4: Remove Python mvn Package
```bash
# If you don't need the Python mvn package
pip uninstall mvn

# Then use Maven normally
mvn clean install
```

#### Option 5: Use Full Path to Maven
```bash
# Find Maven installation
where mvn.cmd

# Use full path (example)
"C:\Program Files\Apache\maven\bin\mvn.cmd" clean install
```

---

## 📦 Maven Not Found

### Issue: "mvn is not recognized as an internal or external command"

**Problem**: Maven is not installed or not in your system PATH.

**Solution**:

1. **Download Maven**:
   - Go to https://maven.apache.org/download.cgi
   - Download the Binary zip archive (e.g., apache-maven-3.9.x-bin.zip)

2. **Install Maven**:
   - Extract to a directory (e.g., `C:\Program Files\Apache\maven`)
   - Add Maven's `bin` directory to your system PATH:
     - Right-click "This PC" → Properties
     - Advanced system settings → Environment Variables
     - Under System Variables, find "Path"
     - Click Edit → New
     - Add: `C:\Program Files\Apache\maven\bin`
     - Click OK on all dialogs

3. **Verify Installation**:
   ```bash
   mvn.cmd --version
   ```

---

## ☕ Java Issues

### Issue: "JAVA_HOME is not set"

**Problem**: Maven cannot find Java installation.

**Solution**:

1. **Find Java Installation**:
   ```bash
   where java
   ```

2. **Set JAVA_HOME**:
   - Right-click "This PC" → Properties
   - Advanced system settings → Environment Variables
   - Under System Variables, click New
   - Variable name: `JAVA_HOME`
   - Variable value: `C:\Program Files\Java\jdk-17` (your Java path)
   - Click OK

3. **Verify**:
   ```bash
   echo %JAVA_HOME%
   java -version
   ```

### Issue: "Java version is too old"

**Problem**: Project requires Java 17 or higher.

**Solution**:
1. Download Java 17 or higher from:
   - https://adoptium.net/ (recommended)
   - https://www.oracle.com/java/technologies/downloads/

2. Install and update JAVA_HOME as shown above

---

## 🚀 Application Startup Issues

### Issue: "Port 8080 is already in use"

**Problem**: Another application is using port 8080.

**Solution Options**:

#### Option 1: Stop the Process Using Port 8080
```bash
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F
```

#### Option 2: Change Application Port
Edit `src/main/resources/application.properties`:
```properties
server.port=8081
```

Then access application at: http://localhost:8081

---

## 🗄️ Database Issues

### Issue: "H2 Console not accessible"

**Problem**: H2 console configuration issue.

**Solution**:
1. Verify in `application.properties`:
   ```properties
   spring.h2.console.enabled=true
   spring.h2.console.path=/h2-console
   ```

2. Access at: http://localhost:8080/h2-console

3. Use these credentials:
   - JDBC URL: `jdbc:h2:mem:userdb`
   - Username: `sa`
   - Password: (leave empty)

---

## 🔐 Security Issues

### Issue: "403 Forbidden" on form submission

**Problem**: CSRF token missing or invalid.

**Solution**:
1. Verify CSRF token in form:
   ```jsp
   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
   ```

2. Clear browser cache and cookies

3. Restart application

### Issue: "Login fails with correct credentials"

**Problem**: Password encoding mismatch.

**Solution**:
1. Check H2 console to verify user exists
2. Delete user and re-register
3. Verify BCrypt encoder is configured in SecurityConfig

---

## 📄 JSP Issues

### Issue: "JSP pages not rendering / showing as download"

**Problem**: JSP configuration issue.

**Solution**:
1. Verify `pom.xml` includes:
   ```xml
   <dependency>
       <groupId>org.apache.tomcat.embed</groupId>
       <artifactId>tomcat-embed-jasper</artifactId>
   </dependency>
   ```

2. Verify `application.properties`:
   ```properties
   spring.mvc.view.prefix=/WEB-INF/views/
   spring.mvc.view.suffix=.jsp
   ```

3. Verify JSP files are in: `src/main/webapp/WEB-INF/views/`

4. Verify packaging in `pom.xml`:
   ```xml
   <packaging>war</packaging>
   ```

---

## 🏗️ Build Issues

### Issue: "Build failure - dependencies not found"

**Problem**: Maven cannot download dependencies.

**Solution**:
1. Check internet connection
2. Clear Maven cache:
   ```bash
   mvn.cmd dependency:purge-local-repository
   ```
3. Try again:
   ```bash
   mvn.cmd clean install -U
   ```

### Issue: "Compilation errors"

**Problem**: Java version mismatch.

**Solution**:
1. Verify Java version:
   ```bash
   java -version
   ```
   Should be 17 or higher

2. Verify Maven is using correct Java:
   ```bash
   mvn.cmd --version
   ```

3. Update `pom.xml` if needed:
   ```xml
   <properties>
       <java.version>17</java.version>
   </properties>
   ```

---

## 🌐 Browser Issues

### Issue: "Styles not loading"

**Problem**: Static resources not served.

**Solution**:
1. Clear browser cache (Ctrl+Shift+Delete)
2. Hard refresh (Ctrl+F5)
3. Try incognito/private mode

### Issue: "CSRF token error in browser console"

**Problem**: CSRF token not being sent.

**Solution**:
1. Verify form method is POST
2. Check CSRF token is in form
3. Clear cookies and try again

---

## 🔍 Debugging Tips

### Enable Debug Logging

Add to `application.properties`:
```properties
logging.level.org.springframework.security=DEBUG
logging.level.com.pahappa=DEBUG
logging.level.org.springframework.web=DEBUG
```

### Check Application Logs

Look for errors in console output when running:
```bash
mvn.cmd spring-boot:run
```

### Test with H2 Console

1. Access: http://localhost:8080/h2-console
2. Run queries to check data:
   ```sql
   SELECT * FROM USERS;
   SELECT COUNT(*) FROM USERS;
   ```

---

## 📞 Getting Help

If issues persist:

1. **Check Logs**: Review console output for error messages
2. **Check H2 Console**: Verify database state
3. **Check Browser Console**: Look for JavaScript errors
4. **Clear Everything**: 
   - Clear browser cache
   - Delete `target/` folder
   - Rebuild: `mvn.cmd clean install`
5. **Restart**: Stop application and start fresh

---

## ✅ Quick Checklist

Before asking for help, verify:
- [ ] Java 17+ is installed
- [ ] Maven is installed and in PATH
- [ ] Using `mvn.cmd` or `run-maven.bat` (not `mvn`)
- [ ] Port 8080 is available
- [ ] All files are in correct locations
- [ ] No compilation errors
- [ ] Application starts without errors
- [ ] Can access http://localhost:8080

---

## 🎯 Common Command Reference

```bash
# Build project (use one of these)
run-maven.bat clean install
mvn.cmd clean install

# Run application (use one of these)
run-maven.bat spring-boot:run
mvn.cmd spring-boot:run

# Skip tests during build
run-maven.bat clean install -DskipTests
mvn.cmd clean install -DskipTests

# Clean build
run-maven.bat clean
mvn.cmd clean

# Check dependencies
run-maven.bat dependency:tree
mvn.cmd dependency:tree
```

---

**Remember**: On Windows with conda environments, always use `mvn.cmd` or the provided `run-maven.bat` script instead of just `mvn`.