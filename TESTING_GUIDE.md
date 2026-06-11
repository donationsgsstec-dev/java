# Testing Guide for Spring Boot User Authentication Application

This guide provides comprehensive testing instructions for the user authentication application.

## 🧪 Testing Checklist

### 1. Build and Run Tests

#### Build the Project
```bash
mvn clean install
```

#### Run the Application
```bash
mvn spring-boot:run
```

The application will start on: **http://localhost:8080**

---

## 🔐 Security Features Testing

### Test 1: CSRF Protection
**Purpose**: Verify that CSRF tokens are properly included in all forms

**Steps**:
1. Open browser developer tools (F12)
2. Navigate to http://localhost:8080/register
3. Inspect the registration form
4. Verify presence of hidden CSRF input field: `<input type="hidden" name="_csrf" value="...">`
5. Repeat for login form at http://localhost:8080/login
6. Verify logout form on home page also includes CSRF token

**Expected Result**: All POST forms must include CSRF token

---

### Test 2: Protected Routes
**Purpose**: Verify that protected routes require authentication

**Steps**:
1. Open browser in incognito/private mode
2. Try to access http://localhost:8080/home directly
3. Verify you are redirected to login page
4. Try to access http://localhost:8080/ directly
5. Verify you are redirected to login page

**Expected Result**: Unauthenticated users cannot access protected routes

---

### Test 3: Password Encryption
**Purpose**: Verify passwords are encrypted using BCrypt

**Steps**:
1. Register a new user with password "test123"
2. Access H2 Console: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:userdb`
   - Username: `sa`
   - Password: (leave empty)
3. Run query: `SELECT * FROM USERS;`
4. Check the PASSWORD column
5. Verify password is hashed (starts with `$2a$` or `$2b$`)

**Expected Result**: Password should be BCrypt hash, not plain text

---

## 📝 Registration Testing

### Test 4: Successful Registration
**Purpose**: Verify user can register with valid data

**Steps**:
1. Navigate to http://localhost:8080/register
2. Fill in the form:
   - Username: `testuser`
   - Email: `test@example.com`
   - First Name: `Test`
   - Last Name: `User`
   - Password: `password123`
3. Click "Create Account"
4. Verify redirect to login page with success message
5. Verify message: "Registration successful! Please login."

**Expected Result**: User registered successfully and redirected to login

---

### Test 5: Duplicate Username
**Purpose**: Verify duplicate username validation

**Steps**:
1. Register a user with username `john`
2. Try to register another user with same username `john`
3. Verify error message appears
4. Verify message contains: "Username already exists"

**Expected Result**: Registration fails with appropriate error message

---

### Test 6: Duplicate Email
**Purpose**: Verify duplicate email validation

**Steps**:
1. Register a user with email `john@test.com`
2. Try to register another user with same email `john@test.com`
3. Verify error message appears
4. Verify message contains: "Email already exists"

**Expected Result**: Registration fails with appropriate error message

---

### Test 7: Validation Errors
**Purpose**: Verify form validation works correctly

**Test Cases**:

**a) Empty Fields**
- Leave all fields empty
- Click "Create Account"
- Verify validation errors appear for all required fields

**b) Short Username**
- Username: `ab` (less than 3 characters)
- Verify error: "Username must be between 3 and 50 characters"

**c) Short Password**
- Password: `12345` (less than 6 characters)
- Verify error: "Password must be at least 6 characters"

**d) Invalid Email**
- Email: `notanemail`
- Verify error: "Please provide a valid email address"

**Expected Result**: All validation rules are enforced

---

## 🔑 Login Testing

### Test 8: Successful Login
**Purpose**: Verify user can login with correct credentials

**Steps**:
1. Register a user (username: `testuser`, password: `password123`)
2. Navigate to http://localhost:8080/login
3. Enter correct credentials
4. Click "Sign In"
5. Verify redirect to home page
6. Verify welcome message with username

**Expected Result**: User logged in and redirected to home page

---

### Test 9: Failed Login - Wrong Password
**Purpose**: Verify login fails with incorrect password

**Steps**:
1. Navigate to http://localhost:8080/login
2. Enter username: `testuser`
3. Enter wrong password: `wrongpassword`
4. Click "Sign In"
5. Verify error message appears
6. Verify message: "Invalid username or password"

**Expected Result**: Login fails with error message

---

### Test 10: Failed Login - Non-existent User
**Purpose**: Verify login fails for non-existent user

**Steps**:
1. Navigate to http://localhost:8080/login
2. Enter username: `nonexistentuser`
3. Enter any password
4. Click "Sign In"
5. Verify error message appears

**Expected Result**: Login fails with error message

---

## 🏠 Home Page Testing

### Test 11: Access Home Page
**Purpose**: Verify authenticated users can access home page

**Steps**:
1. Login with valid credentials
2. Verify home page displays
3. Verify username appears in navigation bar
4. Verify all information cards are displayed
5. Verify logout button is present

**Expected Result**: Home page displays correctly for authenticated users

---

### Test 12: Session Management
**Purpose**: Verify session is maintained across requests

**Steps**:
1. Login successfully
2. Navigate to home page
3. Refresh the page
4. Verify you remain logged in
5. Verify no redirect to login page

**Expected Result**: Session persists across page refreshes

---

## 🚪 Logout Testing

### Test 13: Successful Logout
**Purpose**: Verify user can logout successfully

**Steps**:
1. Login with valid credentials
2. Navigate to home page
3. Click "Logout" button
4. Verify redirect to login page
5. Verify success message: "You have been logged out successfully"
6. Try to access http://localhost:8080/home
7. Verify redirect to login page

**Expected Result**: User logged out and session invalidated

---

### Test 14: Logout CSRF Protection
**Purpose**: Verify logout requires CSRF token

**Steps**:
1. Login successfully
2. Open browser developer tools
3. Inspect logout form
4. Verify CSRF token is present in form
5. Click logout
6. Verify logout succeeds

**Expected Result**: Logout form includes CSRF token and works correctly

---

## 🗄️ Database Testing

### Test 15: H2 Console Access
**Purpose**: Verify H2 console is accessible for debugging

**Steps**:
1. Navigate to http://localhost:8080/h2-console
2. Enter connection details:
   - JDBC URL: `jdbc:h2:mem:userdb`
   - Username: `sa`
   - Password: (empty)
3. Click "Connect"
4. Run query: `SELECT * FROM USERS;`
5. Verify registered users appear in table

**Expected Result**: H2 console accessible and shows user data

---

### Test 16: Data Persistence
**Purpose**: Verify data persists during application runtime

**Steps**:
1. Register 3 different users
2. Access H2 console
3. Run: `SELECT COUNT(*) FROM USERS;`
4. Verify count is 3
5. Login with each user
6. Verify all logins work

**Expected Result**: All registered users persist in database

---

## 🎨 UI/UX Testing

### Test 17: Responsive Design
**Purpose**: Verify application works on different screen sizes

**Steps**:
1. Open application in browser
2. Use browser developer tools to test different screen sizes:
   - Desktop (1920x1080)
   - Tablet (768x1024)
   - Mobile (375x667)
3. Verify all pages display correctly
4. Verify forms are usable on all screen sizes

**Expected Result**: Application is responsive on all screen sizes

---

### Test 18: Error Message Display
**Purpose**: Verify error messages are clearly visible

**Steps**:
1. Trigger various validation errors
2. Verify error messages are:
   - Clearly visible
   - Red colored
   - Positioned near relevant fields
   - Easy to read

**Expected Result**: Error messages are user-friendly and visible

---

### Test 19: Success Message Display
**Purpose**: Verify success messages are clearly visible

**Steps**:
1. Complete successful registration
2. Verify success message on login page
3. Complete successful logout
4. Verify success message on login page
5. Verify messages are:
   - Clearly visible
   - Green colored
   - Easy to read

**Expected Result**: Success messages are user-friendly and visible

---

## 🔄 Integration Testing

### Test 20: Complete User Journey
**Purpose**: Test complete user flow from registration to logout

**Steps**:
1. Navigate to http://localhost:8080
2. Click "Register here"
3. Complete registration form
4. Verify redirect to login with success message
5. Login with registered credentials
6. Verify redirect to home page
7. Verify username displayed correctly
8. Click logout
9. Verify redirect to login with logout message
10. Try to access home page
11. Verify redirect to login

**Expected Result**: Complete user journey works seamlessly

---

## 📊 Performance Testing

### Test 21: Multiple User Registration
**Purpose**: Verify system handles multiple users

**Steps**:
1. Register 10 different users
2. Verify all registrations succeed
3. Login with each user
4. Verify all logins work
5. Check H2 console for all users

**Expected Result**: System handles multiple users correctly

---

## 🐛 Error Handling Testing

### Test 22: Server Error Handling
**Purpose**: Verify graceful error handling

**Steps**:
1. Try to register with extremely long input (>1000 characters)
2. Verify application doesn't crash
3. Verify appropriate error message
4. Try special characters in all fields
5. Verify proper handling

**Expected Result**: Application handles errors gracefully

---

## ✅ Test Results Template

Use this template to record your test results:

```
Test #: [Test Number]
Test Name: [Test Name]
Date: [Date]
Tester: [Your Name]
Status: [PASS/FAIL]
Notes: [Any observations]
```

---

## 🎯 Success Criteria

All tests should PASS for the application to be considered production-ready:

- ✅ All security features working (CSRF, authentication, authorization)
- ✅ All validation rules enforced
- ✅ Password encryption working
- ✅ Session management working
- ✅ Error messages displayed correctly
- ✅ Success messages displayed correctly
- ✅ Responsive design working
- ✅ Database operations working
- ✅ Complete user journey working

---

## 🚀 Quick Test Commands

```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run

# Run with specific port
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081

# Package as WAR
mvn package

# Skip tests during build
mvn clean install -DskipTests
```

---

## 📞 Support

If any tests fail, check:
1. Application logs in console
2. H2 database console
3. Browser developer console
4. Network tab in developer tools

---

**Happy Testing! 🎉**