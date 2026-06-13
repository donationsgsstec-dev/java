# Spring Boot User Authentication & Attendance System

A complete Spring Boot 3.3+ application demonstrating user registration, login functionality, attendance tracking, and email notifications with Spring Security, JPA/Hibernate, and JSP views.  Java Persistence API (Now officially known as Jakarta Persistence) and JavaServer Pages (Now officially known as Jakarta Server Pages)

## 🚀 Features

### Core Features
- **User Registration**: Complete registration form with validation
- **User Login**: Secure form-based authentication
- **Attendance Tracking**: Sign-in/sign-out system with history
- **Email Notifications**: Automated emails via QSSN API
- **Admin Panel**: View all users' attendance records
- **Spring Security**: BCrypt password encoding and custom UserDetailsService
- **JPA/Hibernate**: Database operations with MySQL database
- **JSP Views**: Server-side rendering with JSTL tags
- **Validation**: Form validation with error messages
- **Responsive Design**: Mobile-friendly UI

### Advanced QR Code Features
- **QR Code Expiration**: QR codes automatically expire after 10 minutes
- **Single-Use Validation**: Each QR code can only be used once
- **Rate Limiting**: Users can generate maximum 2 QR codes per minute
- **Encrypted QR Data**: AES encryption for secure QR code data
- **Once-Per-Day Check-In**: Users can only check in once per 24-hour period

### Real-Time Features
- **Live Weekly Overview**: Attendance dashboard updates every 30 seconds
- **REST API**: JSON endpoints for real-time attendance data
- **Auto-Refresh Charts**: Weekly attendance chart with live database sync

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6 or higher (included in project: `apache-maven-3.9.16`)
- MySQL Server 8.0 or higher (running on localhost:3306)
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## 🛠️ Technology Stack

- **Spring Boot**: 3.3.0
- **Spring Security**: 6.x
- **Spring Data JPA**: Database operations
- **MySQL Database**: Production-ready relational database
- **JSP & JSTL**: View layer
- **Maven**: Build tool
- **BCrypt**: Password encryption
- **QSSN API**: Email notification service
- **RestTemplate**: HTTP client for API calls

## 📁 Project Structure

```
user-auth-app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── pahappa/
│   │   │           └── app/
│   │   │               ├── UserAuthApplication.java
│   │   │               ├── config/
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   ├── CustomAuthenticationSuccessHandler.java
│   │   │               │   └── CustomLogoutSuccessHandler.java
│   │   │               ├── controller/
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── HomeController.java
│   │   │               │   └── AttendanceController.java
│   │   │               ├── entity/
│   │   │               │   ├── User.java
│   │   │               │   └── Attendance.java
│   │   │               ├── repository/
│   │   │               │   ├── UserRepository.java
│   │   │               │   └── AttendanceRepository.java
│   │   │               └── service/
│   │   │                   ├── UserService.java
│   │   │                   ├── AttendanceService.java
│   │   │                   ├── EmailService.java
│   │   │                   └── impl/
│   │   │                       ├── UserServiceImpl.java
│   │   │                       ├── AttendanceServiceImpl.java
│   │   │                       ├── EmailServiceImpl.java
│   │   │                       └── CustomUserDetailsService.java
│   │   ├── resources/
│   │   │   └── application.properties
│   │   └── webapp/
│   │       └── WEB-INF/
│   │           └── views/
│   │               ├── register.jsp
│   │               ├── login.jsp
│   │               ├── home.jsp
│   │               ├── attendance.jsp
│   │               ├── attendance-history.jsp
│   │               └── attendance-admin.jsp
│   └── test/
├── apache-maven-3.9.16/
└── pom.xml
```

## 🚦 Getting Started

### Step 1: Setup MySQL Database

**The application automatically creates the database on startup!** You only need to:

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

📖 **For detailed MySQL setup instructions, see [MYSQL_SETUP.md](MYSQL_SETUP.md)**

### Step 2: Compile the Project

Open terminal in the project directory and run:

```bash
.\apache-maven-3.9.16\bin\mvn.cmd clean compile
```

This will:
- Clean any previous builds
- Download dependencies
- Compile the Java source code

### Step 3: Run the Application

```bash
.\apache-maven-3.9.16\bin\mvn.cmd spring-boot:run
```

Wait until you see:
```
Started UserAuthApplication in X.XXX seconds
```

The application will be available at: **http://localhost:8080**

### Step 4: Access the Application

Open your browser and navigate to:
- **Registration**: http://localhost:8080/register
- **Login**: http://localhost:8080/login
- **Home**: http://localhost:8080/home (requires login)
- **Attendance**: http://localhost:8080/attendance (requires login)
## 🗄️ Database Management

The application uses **MySQL** database with automatic schema creation.

### Accessing MySQL Database

Use MySQL Workbench, command line, or any MySQL client:

```bash
mysql -u root -p
USE userdb;
```

### Useful SQL Queries:

```sql
-- View all users
SELECT * FROM users;

-- View all attendance records
SELECT * FROM attendance;

-- Create an admin user (after registering normally)
UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';

-- Check user roles
SELECT username, email, role FROM users;

-- View attendance with user details
SELECT u.username, a.date, a.check_in_time, a.check_out_time, a.status
FROM attendance a
JOIN users u ON a.user_id = u.id
ORDER BY a.date DESC;
```

### Database Configuration

- **Database Name**: `userdb` (auto-created)
- **Connection URL**: `jdbc:mysql://localhost:3306/userdb?createDatabaseIfNotExist=true`
- **Tables**: Automatically created by Hibernate
  - `users` - User accounts and authentication
  - `attendance` - Attendance records

📖 **For troubleshooting and advanced configuration, see [MYSQL_SETUP.md](MYSQL_SETUP.md)**

## 📧 QSSN Email Notification Service

This application integrates with **QSSN (Quantum Secure Social Network)** API for sending bulk email notifications.

### What is QSSN?

QSSN is a secure email notification service that provides:
- **Transactional Emails**: Automated notifications for user actions
- **Bulk Email Support**: Send multiple emails efficiently
- **RESTful API**: Simple HTTP-based integration
- **Secure Authentication**: Bearer token authentication
- **HTML Email Support**: Rich formatted emails

### Email Notifications Sent:

1. **Welcome Email** - Sent after successful registration
2. **Login Notification** - Security alert when user logs in
3. **Logout Notification** - Confirmation when user logs out
4. **Attendance Sign-In** - Confirmation of attendance sign-in
5. **Attendance Sign-Out** - Summary with duration worked

### QSSN Configuration

Configuration is in `src/main/resources/application.properties`:

```properties
# QSSN Email Service Configuration
qssn.email.api.key=qssn_live_9e923f45d56ede8ec89ded81b13ccc246823cbb1bff67d31d07538c584a424fe
qssn.email.api.url=https://qssn-d1-api.gastonsoftwaresolutions234.workers.dev/api/v1/emails/send
qssn.email.from.name=User Auth App
qssn.email.enabled=true
```

### How It Works:

1. **User Action** → Registration, Login, Logout, Attendance
2. **Service Layer** → Calls EmailService
3. **EmailService** → Builds HTML email template
4. **RestTemplate** → Sends HTTP POST to QSSN API
5. **QSSN API** → Delivers email to recipient

### Email Service Implementation:

The email service uses Spring's `RestTemplate` to communicate with QSSN API:

```java
// HTTP Headers with Bearer Authentication
HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.APPLICATION_JSON);
headers.setBearerAuth(apiKey);

// Request Body
Map<String, String> requestBody = new HashMap<>();
requestBody.put("to", toEmail);
requestBody.put("subject", subject);
requestBody.put("html", htmlContent);
requestBody.put("from_name", fromName);

// Send POST Request
ResponseEntity<String> response = restTemplate.exchange(
    apiUrl, HttpMethod.POST, request, String.class
);
```

### Disable Email Notifications (Optional):

To disable email sending during development:

```properties
qssn.email.enabled=false
```

## 📝 Usage

### Register a New User

1. Go to http://localhost:8080/register
2. Fill in the registration form:
   - Username (3-50 characters)
   - Email (valid email format)
   - First Name
   - Last Name
   - Password (minimum 6 characters)
3. Click "Register"
4. Check your email for welcome message
5. You'll be redirected to the login page

### Login

1. Go to http://localhost:8080/login
2. Enter your username and password
3. Click "Login"
4. Check your email for login notification
5. You'll be redirected to the home page

### Attendance Tracking

1. **Sign In**: Click "Sign In" button on attendance page
2. **Sign Out**: Click "Sign Out" button when leaving
3. **View History**: See your attendance records
4. **Email Notifications**: Receive confirmation emails

### Admin Access

To access the admin panel at http://localhost:8080/attendance/admin:

1. Register a user account
2. Access H2 Console
3. Run: `UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';`
4. Logout and login again
5. Access admin panel to view all users' attendance

### Logout

Click the "Logout" button in the navigation bar on any page.

## 🔒 Security Features

- **BCrypt Password Encoding**: Passwords are hashed using BCrypt with automatic salt generation
- **CSRF Protection**: Cross-Site Request Forgery protection enabled
- **Session Management**: Secure session handling (one session per user)
- **Form-Based Authentication**: Standard form login with Spring Security
- **Custom UserDetailsService**: Custom implementation for loading user details
- **Role-Based Access Control**: INTERN and ADMIN roles
- **Email Notifications**: Security alerts for login/logout activities

## 🎨 Validation Rules

### Registration Form Validation

- **Username**: 
  - Required
  - 3-50 characters
  - Must be unique
  
- **Email**: 
  - Required
  - Valid email format
  - Must be unique
  
- **First Name**: 
  - Required
  - Maximum 50 characters
  
- **Last Name**: 
  - Required
  - Maximum 50 characters
  
- **Password**: 
  - Required
  - Minimum 6 characters

## 🧪 Testing

### Manual Testing

1. **Test Registration**:
   - Register with valid data
   - Try registering with duplicate username
   - Try registering with duplicate email
   - Try registering with invalid data
   - Check welcome email

2. **Test Login**:
   - Login with correct credentials
   - Try login with incorrect password
   - Try login with non-existent username
   - Check login notification email

3. **Test Attendance**:
   - Sign in for attendance
   - Sign out from attendance
   - View attendance history
   - Check email notifications

4. **Test Security**:
   - Try accessing /home without login (should redirect to login)
   - Login and access /home (should work)
   - Logout and try accessing /home again (should redirect to login)
   - Try accessing /attendance/admin without ADMIN role (should get 403)

## 📦 Maven Commands

```bash
# Clean and compile
.\apache-maven-3.9.16\bin\mvn.cmd clean compile

# Run the application
.\apache-maven-3.9.16\bin\mvn.cmd spring-boot:run

# Clean and build
.\apache-maven-3.9.16\bin\mvn.cmd clean install

# Run tests
.\apache-maven-3.9.16\bin\mvn.cmd test

# Package as WAR
.\apache-maven-3.9.16\bin\mvn.cmd package

# Skip tests during build
.\apache-maven-3.9.16\bin\mvn.cmd clean install -DskipTests
```

## 🐛 Troubleshooting

### 403 Forbidden Error

If you get a 403 error when accessing the application:
- You're trying to access a protected page without logging in
- Solution: Go to `/register` or `/login` first

### JSP Pages Not Loading

If JSP pages are not rendering:
1. Ensure `tomcat-embed-jasper` dependency is in pom.xml
2. Check that JSP files are in `src/main/webapp/WEB-INF/views/`
3. Verify `spring.mvc.view.prefix` and `spring.mvc.view.suffix` in application.properties

### Database Issues

If you encounter database errors:
1. Check H2 console at http://localhost:8080/h2-console
2. Verify database URL: `jdbc:h2:mem:userdb`
3. Check application.properties for correct database configuration

### Email Not Sending

If emails are not being sent:
1. Check `qssn.email.enabled=true` in application.properties
2. Verify QSSN API key is correct
3. Check application logs for error messages
4. Ensure internet connection is available

### Port Already in Use

If port 8080 is already in use:
1. Change the port in application.properties: `server.port=8081`
2. Or stop the process using port 8080

### Maven Conflicts

If you get "ModuleNotFoundError: No module named 'pwd'" error:
- You have a Python `mvn` package conflicting with Maven
- Solution: Use `.\apache-maven-3.9.16\bin\mvn.cmd` instead of `mvn`

## 🔧 Additional Documentation

- **[TROUBLESHOOTING.md](TROUBLESHOOTING.md)** - Comprehensive troubleshooting guide
- **[ATTENDANCE_SYSTEM.md](ATTENDANCE_SYSTEM.md)** - Attendance system documentation
- **[EMAIL_NOTIFICATIONS.md](EMAIL_NOTIFICATIONS.md)** - Email notification details
- **[QUICK_START.md](QUICK_START.md)** - Quick start guide

## 📚 Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/)
- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [H2 Database Documentation](https://www.h2database.com/html/main.html)

## 👨‍💻 Author

**Pahappa Development Team**

## 📄 License

This project is created for educational purposes.

## 🤝 Contributing

Feel free to fork this project and submit pull requests for any improvements.

## 🔗 Repository

GitHub: https://github.com/GSS-creator/java.git

---

**Happy Coding! 🎉**