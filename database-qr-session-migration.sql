-- Database migration script for QR Code Session tracking
-- This script adds the qr_code_sessions table for tracking QR code generation, expiration, and usage

-- Create qr_code_sessions table
CREATE TABLE IF NOT EXISTS qr_code_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    qr_code_data VARCHAR(1000) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL,
    expires_at DATETIME NOT NULL,
    is_used BOOLEAN NOT NULL DEFAULT FALSE,
    used_at DATETIME NULL,
    ip_address VARCHAR(45) NULL,
    
    -- Foreign key constraint
    CONSTRAINT fk_qr_session_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    
    -- Indexes for performance
    INDEX idx_qr_code_data (qr_code_data),
    INDEX idx_user_created_at (user_id, created_at),
    INDEX idx_expires_at (expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add comment to table
ALTER TABLE qr_code_sessions COMMENT = 'Tracks QR code generation, expiration (10 minutes), and single-use validation';

-- Verify table creation
SELECT 'QR Code Sessions table created successfully' AS status;

-- Show table structure
DESCRIBE qr_code_sessions;

-- Made with Bob
