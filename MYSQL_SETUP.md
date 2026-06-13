# MySQL Database Setup Guide

This guide explains how to configure and run the User Authentication Application with MySQL database.

## Prerequisites

Before running the application, ensure you have:

1. **MySQL Server** installed and running
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Recommended version: MySQL 8.0 or higher

2. **Java 17** or higher installed

3. **Maven** installed (or use the included Maven wrapper)

## Quick Start

The application is configured to **automatically create the database** when it starts. You only need to:

1. **Install and Start MySQL Server**
   ```bash
   # Check if MySQL is running
   mysql --version
   ```

2. **Configure Database Credentials** (if needed)
   
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=your_password_here
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

That's it! The application will:
- Automatically create the `userdb` database
- Create all necessary tables (users, attendance)
- Start the server on http://localhost:8080

## Database Configuration Details

### Connection URL
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/userdb?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
```

**URL Parameters Explained:**
- `createDatabaseIfNotExist=true` - Automatically creates the database if it doesn't exist
- `useSSL=false` - Disables SSL for local development
- `serverTimezone=UTC` - Sets the timezone to UTC
- `allowPublicKeyRetrieval=true` - Allows public key retrieval for authentication

### Default Credentials
- **Database Name:** `userdb`
- **Username:** `root`
- **Password:** (empty by default)
- **Port:** `3306`

## Manual Database Setup (Optional)

If you prefer to manually create the database, you can use the provided SQL script:

```bash
mysql -u root -p < database-setup.sql
```

Or manually in MySQL:
```sql
CREATE DATABASE IF NOT EXISTS userdb
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

## Database Schema

The application automatically creates the following tables:

### 1. Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 2. Attendance Table
```sql
CREATE TABLE attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    check_in_time TIMESTAMP,
    check_out_time TIMESTAMP,
    date DATE NOT NULL,
    status VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## Troubleshooting

### Issue: "Access denied for user 'root'@'localhost'"

**Solution:** Update the password in `application.properties`:
```properties
spring.datasource.password=your_mysql_password
```

### Issue: "Communications link failure"

**Solutions:**
1. Verify MySQL is running:
   ```bash
   # Windows
   net start MySQL80
   
   # Linux/Mac
   sudo systemctl start mysql
   ```

2. Check if MySQL is listening on port 3306:
   ```bash
   netstat -an | findstr 3306
   ```

### Issue: "Unknown database 'userdb'"

**Solution:** The database should be created automatically. If not:
1. Check the connection URL includes `createDatabaseIfNotExist=true`
2. Manually create the database:
   ```sql
   CREATE DATABASE userdb;
   ```

### Issue: "Public Key Retrieval is not allowed"

**Solution:** The connection URL already includes `allowPublicKeyRetrieval=true`. If still failing:
1. Update MySQL user authentication:
   ```sql
   ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'your_password';
   FLUSH PRIVILEGES;
   ```

## Switching Between H2 and MySQL

The application is configured for MySQL by default. To switch back to H2 (in-memory database):

1. Comment out MySQL configuration in `application.properties`:
   ```properties
   # MySQL Configuration (commented)
   #spring.datasource.url=jdbc:mysql://localhost:3306/userdb?createDatabaseIfNotExist=true...
   #spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
   #spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
   ```

2. Uncomment H2 configuration:
   ```properties
   # H2 Configuration (active)
   spring.datasource.url=jdbc:h2:mem:userdb
   spring.datasource.driverClassName=org.h2.Driver
   spring.h2.console.enabled=true
   spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
   ```

## Verifying Database Connection

After starting the application, check the logs for:

```
Hibernate: create table users (...)
Hibernate: create table attendance (...)
```

This confirms that:
- Database connection is successful
- Tables are being created automatically
- Application is ready to use

## Database Management Tools

You can use these tools to manage your MySQL database:

1. **MySQL Workbench** (GUI)
   - Download: https://dev.mysql.com/downloads/workbench/

2. **Command Line**
   ```bash
   mysql -u root -p
   USE userdb;
   SHOW TABLES;
   SELECT * FROM users;
   ```

3. **DBeaver** (Universal Database Tool)
   - Download: https://dbeaver.io/

## Production Considerations

For production deployment:

1. **Use Strong Passwords**
   ```properties
   spring.datasource.password=${DB_PASSWORD}
   ```

2. **Enable SSL**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/userdb?useSSL=true&requireSSL=true
   ```

3. **Change DDL Auto Mode**
   ```properties
   spring.jpa.hibernate.ddl-auto=validate
   ```

4. **Use Connection Pooling**
   ```properties
   spring.datasource.hikari.maximum-pool-size=10
   spring.datasource.hikari.minimum-idle=5
   ```

## Support

For additional help:
- Check the main [README.md](README.md)
- Review [TROUBLESHOOTING.md](TROUBLESHOOTING.md)
- Consult MySQL documentation: https://dev.mysql.com/doc/

---

**Note:** The application uses Hibernate's `ddl-auto=update` mode, which automatically creates and updates database schema based on your entity classes. This is convenient for development but should be changed to `validate` in production.