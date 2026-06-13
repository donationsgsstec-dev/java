-- Script to create an admin user or upgrade existing user to admin
-- Run this script to access admin endpoints

-- Option 1: Update existing user to ADMIN role
-- Replace 'your_username' with your actual username
UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';

-- Option 2: Create a new admin user
-- Password is 'admin123' (BCrypt encoded)
-- You can change the username, email, and other details as needed
INSERT INTO users (username, email, password, first_name, last_name, enabled, role, created_at, updated_at)
VALUES (
    'admin',
    'pahapacomp@gmail.com',
    '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', -- password: admin123
    'Admin',
    'User',
    true,
    'ADMIN',
    NOW(),
    NOW()
);

-- Verify the change
SELECT id, username, email, first_name, last_name, role, enabled 
FROM users 
WHERE role = 'ADMIN';

-- Made with Bob
