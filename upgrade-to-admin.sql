-- Upgrade your existing user to ADMIN role
-- This is the simplest solution - just change your current user's role

-- First, let's see all users
SELECT id, username, email, role FROM users;

-- Update YOUR username to ADMIN (replace 'your_username' with your actual username)
-- For example, if your username is 'john', use: UPDATE users SET role = 'ADMIN' WHERE username = 'john';
UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';

-- Verify the change
SELECT id, username, email, role FROM users WHERE role = 'ADMIN';

-- After running this, logout and login again with your existing credentials
-- You will then have admin access

-- Made with Bob
