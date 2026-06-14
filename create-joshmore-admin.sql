-- Create Admin User: JoshMore
-- Email: info@gss-tec.com
-- Password: Admin@123 (change after first login)
-- Role: ADMIN

-- First, check if user already exists and delete if needed
DELETE FROM users WHERE username = 'JoshMore' OR email = 'info@gss-tec.com';

-- Create the admin user
-- Password is 'Admin@123' (BCrypt encrypted)
INSERT INTO users (username, email, password, role, enabled, created_at) 
VALUES (
    'JoshMore',
    'info@gss-tec.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhCy',
    'ADMIN',
    true,
    CURRENT_TIMESTAMP
);

-- Verify the user was created
SELECT id, username, email, role, enabled, created_at 
FROM users 
WHERE username = 'JoshMore';

-- Display login credentials
SELECT 
    'Admin user created successfully!' as status,
    'JoshMore' as username,
    'info@gss-tec.com' as email,
    'Admin@123' as password,
    'ADMIN' as role,
    'https://pahappa-attendance-system-production.up.railway.app/admin/login' as login_url;

-- Made with Bob
