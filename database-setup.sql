-- MySQL Database Setup Script for User Authentication Application
-- This script is OPTIONAL - the application will automatically create the database
-- Use this only if you want to manually set up the database with specific configurations

-- Create database (if not exists)
CREATE DATABASE IF NOT EXISTS userdb
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Use the database
USE userdb;

-- Grant privileges to root user (adjust username/password as needed)
-- GRANT ALL PRIVILEGES ON userdb.* TO 'root'@'localhost';
-- FLUSH PRIVILEGES;

-- Note: Tables will be automatically created by Hibernate when the application starts
-- The following tables will be created:
-- 1. users (id, username, email, password, role, created_at)
-- 2. attendance (id, user_id, check_in_time, check_out_time, date, status)

-- Verify database creation
SHOW DATABASES LIKE 'userdb';

-- Show tables (after running the application)
-- SHOW TABLES;

-- Made with Bob
