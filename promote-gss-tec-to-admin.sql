-- Promote GSS-TEC user to ADMIN role

-- Update GSS-TEC to ADMIN role
UPDATE users SET role = 'ADMIN' WHERE username = 'GSS-TEC';

-- Verify the change
SELECT id, username, email, role, enabled FROM users WHERE username = 'GSS-TEC';

-- Show all admin users
SELECT id, username, email, role FROM users WHERE role = 'ADMIN';

-- Made with Bob
