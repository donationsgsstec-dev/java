# Email Notifications Feature

## Overview

The application now includes email notification functionality using the **QSSN Mail Services API**. Users receive email notifications for the following events:

1. **Registration** - Welcome email when a new user registers
2. **Login** - Login notification when a user successfully logs in
3. **Logout** - Logout notification when a user logs out

## Configuration

### API Settings

Email notifications are configured in [`application.properties`](application.properties):

```properties
# QSSN Email Service Configuration
qssn.email.api.key=qssn_live_9e923f45d56ede8ec89ded81b13ccc246823cbb1bff67d31d07538c584a424fe
qssn.email.api.url=https://qssn-d1-api.gastonsoftwaresolutions234.workers.dev/api/v1/emails/send
qssn.email.from.name=User Auth App
qssn.email.enabled=true
```

### Configuration Properties

| Property | Description | Default |
|----------|-------------|---------|
| `qssn.email.api.key` | QSSN API authentication key | Required |
| `qssn.email.api.url` | QSSN API endpoint URL | Required |
| `qssn.email.from.name` | Sender name for emails | "User Auth App" |
| `qssn.email.enabled` | Enable/disable email sending | true |

### Disabling Email Notifications

To disable email notifications (e.g., for testing), set:

```properties
qssn.email.enabled=false
```

## Architecture

### Components

1. **[`EmailService`](src/main/java/com/pahappa/app/service/EmailService.java)** - Interface defining email operations
2. **[`EmailServiceImpl`](src/main/java/com/pahappa/app/service/impl/EmailServiceImpl.java)** - Implementation using QSSN API
3. **[`CustomAuthenticationSuccessHandler`](src/main/java/com/pahappa/app/config/CustomAuthenticationSuccessHandler.java)** - Sends login notifications
4. **[`CustomLogoutSuccessHandler`](src/main/java/com/pahappa/app/config/CustomLogoutSuccessHandler.java)** - Sends logout notifications
5. **[`AuthController`](src/main/java/com/pahappa/app/controller/AuthController.java)** - Sends registration welcome emails

### Email Flow

#### Registration Flow
```
User submits registration form
    ↓
AuthController.registerUser()
    ↓
UserService.registerUser() - Saves user to database
    ↓
EmailService.sendWelcomeEmail() - Sends welcome email
    ↓
Redirect to login page
```

#### Login Flow
```
User submits login form
    ↓
Spring Security authenticates user
    ↓
CustomAuthenticationSuccessHandler.onAuthenticationSuccess()
    ↓
EmailService.sendLoginNotification() - Sends login notification
    ↓
Redirect to home page
```

#### Logout Flow
```
User clicks logout
    ↓
Spring Security processes logout
    ↓
CustomLogoutSuccessHandler.onLogoutSuccess()
    ↓
EmailService.sendLogoutNotification() - Sends logout notification
    ↓
Redirect to login page
```

## Email Templates

### 1. Welcome Email (Registration)

**Subject:** Welcome to User Auth App!

**Content:**
- Greeting with username
- Confirmation of successful registration
- Username reminder
- Security notice

**Styling:** Green header (#4CAF50)

### 2. Login Notification

**Subject:** Login Notification - User Auth App

**Content:**
- Greeting with username
- Login timestamp
- Security warning if unauthorized

**Styling:** Blue header (#2196F3)

### 3. Logout Notification

**Subject:** Logout Notification - User Auth App

**Content:**
- Goodbye message
- Logout timestamp
- Security notice

**Styling:** Orange header (#FF9800)

## API Integration

### QSSN Mail Services API

**Base URL:** `https://qssn-d1-api.gastonsoftwaresolutions234.workers.dev`

**Endpoint:** `POST /api/v1/emails/send`

**Authentication:** Bearer token in Authorization header

**Request Format:**
```json
{
  "to": "recipient@example.com",
  "subject": "Email Subject",
  "html": "<h1>HTML Content</h1>",
  "from_name": "User Auth App"
}
```

**Response Format:**
```json
{
  "success": true,
  "message_id": "msg_1234567890",
  "from": "your-email@gmail.com",
  "to": "recipient@example.com"
}
```

### Rate Limits

- **Shared Service:** 100 emails/day (Free plan)
- **Your Gmail (BYOG):** 2,000 emails/day
- **Rate Limit:** 10 requests per minute

## Error Handling

### Graceful Degradation

Email failures **do not block** user operations:

- **Registration:** User is registered even if welcome email fails
- **Login:** User is logged in even if notification email fails
- **Logout:** User is logged out even if notification email fails

### Error Logging

All email errors are logged to console:

```java
System.err.println("Failed to send welcome email: " + exception.getMessage());
```

### Common Issues

1. **Invalid API Key**
   - Check `qssn.email.api.key` in application.properties
   - Verify key is active and not expired

2. **Rate Limit Exceeded**
   - Wait for rate limit window to reset
   - Consider upgrading to BYOG plan

3. **Network Issues**
   - Check internet connectivity
   - Verify QSSN API endpoint is accessible

4. **Invalid Email Address**
   - Ensure user provides valid email during registration
   - Email validation is enforced by `@Email` annotation

## Testing

### Manual Testing

1. **Test Registration Email:**
   ```
   1. Go to http://localhost:8080/register
   2. Fill in registration form with valid email
   3. Submit form
   4. Check email inbox for welcome email
   ```

2. **Test Login Email:**
   ```
   1. Go to http://localhost:8080/login
   2. Enter credentials
   3. Submit form
   4. Check email inbox for login notification
   ```

3. **Test Logout Email:**
   ```
   1. Log in to application
   2. Click logout button
   3. Check email inbox for logout notification
   ```

### Disabling for Testing

To test without sending emails:

```properties
qssn.email.enabled=false
```

## Security Considerations

1. **API Key Protection**
   - API key is stored in application.properties
   - **Do not commit API keys to version control**
   - Use environment variables in production

2. **Email Content**
   - Emails include timestamps for audit trail
   - Security warnings included in notifications
   - No sensitive data (passwords) in emails

3. **User Privacy**
   - Only registered email addresses receive notifications
   - Users cannot opt-out (security feature)
   - Emails sent only for actual user actions

## Production Deployment

### Environment Variables

For production, use environment variables instead of hardcoded values:

```bash
export QSSN_EMAIL_API_KEY=your_api_key_here
export QSSN_EMAIL_API_URL=https://qssn-d1-api.gastonsoftwaresolutions234.workers.dev/api/v1/emails/send
export QSSN_EMAIL_FROM_NAME="Your App Name"
```

Update application.properties:

```properties
qssn.email.api.key=${QSSN_EMAIL_API_KEY}
qssn.email.api.url=${QSSN_EMAIL_API_URL}
qssn.email.from.name=${QSSN_EMAIL_FROM_NAME}
```

### Monitoring

Monitor email delivery:

1. Check application logs for email errors
2. Monitor QSSN API rate limits
3. Track email delivery success rate
4. Set up alerts for repeated failures

## Future Enhancements

Potential improvements:

1. **Async Email Sending**
   - Use `@Async` annotation for non-blocking email sending
   - Implement email queue for retry logic

2. **Email Templates**
   - Move HTML templates to separate files
   - Support multiple languages

3. **User Preferences**
   - Allow users to opt-in/out of notifications
   - Customize notification frequency

4. **Email Verification**
   - Add email verification during registration
   - Send verification link in welcome email

5. **Advanced Notifications**
   - Password reset emails
   - Account activity summaries
   - Security alerts

## Troubleshooting

### Email Not Received

1. Check spam/junk folder
2. Verify email address is correct
3. Check application logs for errors
4. Verify QSSN API key is valid
5. Check rate limits not exceeded

### Application Logs

Enable debug logging:

```properties
logging.level.com.pahappa.app.service.impl.EmailServiceImpl=DEBUG
```

### Support

For QSSN API issues:
- Documentation: Check QSSN API documentation
- Support: Contact QSSN support team

---

**Made with Bob** 🤖