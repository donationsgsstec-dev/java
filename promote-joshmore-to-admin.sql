-- Promote JoshMore to Admin
-- Run this AFTER registering JoshMore through the web interface

-- Step 1: Check if user exists
SELECT id, username, email, role, enabled 
FROM users 
WHERE username = 'JoshMore' OR email = 'info@gss-tec.com';

-- Step 2: Update user to ADMIN role
UPDATE users 
SET role = 'ADMIN',
    enabled = true
WHERE username = 'JoshMore' OR email = 'info@gss-tec.com';

-- Step 3: Verify the update
SELECT id, username, email, role, enabled, created_at 
FROM users 
WHERE username = 'JoshMore' OR email = 'info@gss-tec.com';

-- Display success message
SELECT 
    'User promoted to admin successfully!' as status,
    username,
    email,
    role,
    'https://pahappa-attendance-system-production.up.railway.app/admin/login' as admin_login_url
FROM users 
WHERE username = 'JoshMore' OR email = 'info@gss-tec.com';

-- Made with Bob
