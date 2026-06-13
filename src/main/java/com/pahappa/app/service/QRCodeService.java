package com.pahappa.app.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.pahappa.app.entity.QRCodeSession;
import com.pahappa.app.entity.User;
import com.pahappa.app.repository.QRCodeSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service for QR Code generation and validation.
 * 
 * Generates unique encrypted QR codes for users and validates them.
 * 
 * @author Pahappa
 * @version 1.0
 */
@Service
public class QRCodeService {

    private static final String ENCRYPTION_KEY = "PahappaAttendanceSystemSecretKey2024"; // Should be in config
    private static final String ALGORITHM = "AES";
    private static final int QR_CODE_WIDTH = 300;
    private static final int QR_CODE_HEIGHT = 300;
    private static final int QR_CODE_EXPIRY_MINUTES = 10;
    private static final int RATE_LIMIT_PER_MINUTE = 2;

    @Autowired
    private QRCodeSessionRepository qrCodeSessionRepository;

    /**
     * Generate QR code for a user with rate limiting and expiration tracking
     * @param user User object
     * @return Base64 encoded QR code image
     * @throws WriterException if QR code generation fails
     * @throws IOException if image conversion fails
     * @throws IllegalStateException if rate limit exceeded
     */
    @Transactional
    public String generateQRCode(User user) throws WriterException, IOException {
        // Check rate limit: max 2 QR codes per minute
        checkRateLimit(user);
        
        // Create encrypted data with timestamp
        String data = encryptData(user.getId() + ":" + user.getUsername() + ":" + System.currentTimeMillis());
        
        // Create QR code session
        QRCodeSession session = new QRCodeSession(user, data);
        qrCodeSessionRepository.save(session);
        
        // Generate QR code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE,
                QR_CODE_WIDTH, QR_CODE_HEIGHT, hints);
        
        // Convert to image
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        
        // Return as Base64
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    /**
     * Generate QR code data string (encrypted) with rate limiting
     * @param user User object
     * @return Encrypted QR code data
     * @throws IllegalStateException if rate limit exceeded
     */
    @Transactional
    public String generateQRCodeData(User user) {
        // Check rate limit
        checkRateLimit(user);
        
        // Create encrypted data
        String data = encryptData(user.getId() + ":" + user.getUsername() + ":" + System.currentTimeMillis());
        
        // Create QR code session
        QRCodeSession session = new QRCodeSession(user, data);
        qrCodeSessionRepository.save(session);
        
        return data;
    }

    /**
     * Validate QR code data - checks expiration and single-use
     * @param qrCodeData Encrypted QR code data
     * @return User ID if valid, null otherwise
     */
    @Transactional
    public Long validateQRCode(String qrCodeData) {
        try {
            // Find QR code session
            Optional<QRCodeSession> sessionOpt = qrCodeSessionRepository.findByQrCodeData(qrCodeData);
            
            if (sessionOpt.isEmpty()) {
                return null; // QR code not found
            }
            
            QRCodeSession session = sessionOpt.get();
            
            // Check if QR code is valid (not used and not expired)
            if (!session.isValid()) {
                return null; // QR code expired or already used
            }
            
            // Mark as used
            session.markAsUsed();
            qrCodeSessionRepository.save(session);
            
            // Decrypt and return user ID
            String decrypted = decryptData(qrCodeData);
            String[] parts = decrypted.split(":");
            if (parts.length >= 2) {
                return Long.parseLong(parts[0]);
            }
        } catch (Exception e) {
            // Invalid QR code
            return null;
        }
        return null;
    }

    /**
     * Extract username from QR code
     * @param qrCodeData Encrypted QR code data
     * @return Username if valid, null otherwise
     */
    public String extractUsername(String qrCodeData) {
        try {
            String decrypted = decryptData(qrCodeData);
            String[] parts = decrypted.split(":");
            if (parts.length >= 2) {
                return parts[1];
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Encrypt data using AES
     * @param data Data to encrypt
     * @return Encrypted data as Base64 string
     */
    private String encryptData(String data) {
        try {
            SecretKeySpec secretKey = getSecretKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    /**
     * Decrypt data using AES
     * @param encryptedData Encrypted data as Base64 string
     * @return Decrypted data
     */
    private String decryptData(String encryptedData) {
        try {
            SecretKeySpec secretKey = getSecretKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }

    /**
     * Get secret key for encryption
     * @return SecretKeySpec
     */
    private SecretKeySpec getSecretKey() {
        try {
            byte[] key = ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = java.util.Arrays.copyOf(key, 16); // Use only first 128 bits
            return new SecretKeySpec(key, ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException("Error creating secret key", e);
        }
    }

    /**
     * Generate QR code as byte array with rate limiting
     * @param user User object
     * @return QR code image as byte array
     * @throws WriterException if QR code generation fails
     * @throws IOException if image conversion fails
     * @throws IllegalStateException if rate limit exceeded
     */
    @Transactional
    public byte[] generateQRCodeBytes(User user) throws WriterException, IOException {
        // Check rate limit
        checkRateLimit(user);
        
        String data = encryptData(user.getId() + ":" + user.getUsername() + ":" + System.currentTimeMillis());
        
        // Create QR code session
        QRCodeSession session = new QRCodeSession(user, data);
        qrCodeSessionRepository.save(session);
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE,
                QR_CODE_WIDTH, QR_CODE_HEIGHT, hints);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        
        return outputStream.toByteArray();
    }

    /**
     * Check rate limit for QR code generation
     * @param user User object
     * @throws IllegalStateException if rate limit exceeded
     */
    private void checkRateLimit(User user) {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        LocalDateTime now = LocalDateTime.now();
        
        long recentCount = qrCodeSessionRepository.countByUserAndCreatedAtBetween(user, oneMinuteAgo, now);
        
        if (recentCount >= RATE_LIMIT_PER_MINUTE) {
            throw new IllegalStateException("Rate limit exceeded. You can only generate " + RATE_LIMIT_PER_MINUTE + " QR codes per minute.");
        }
    }

    /**
     * Clean up expired QR code sessions
     * This should be called periodically (e.g., via scheduled task)
     */
    @Transactional
    public void cleanupExpiredSessions() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);
        qrCodeSessionRepository.deleteExpiredSessionsOlderThan(cutoffTime);
    }

    /**
     * Check if QR code is valid (not used and not expired)
     * @param qrCodeData Encrypted QR code data
     * @return true if valid
     */
    public boolean isQRCodeValid(String qrCodeData) {
        Optional<QRCodeSession> sessionOpt = qrCodeSessionRepository.findByQrCodeData(qrCodeData);
        return sessionOpt.map(QRCodeSession::isValid).orElse(false);
    }
}

// Made with Bob