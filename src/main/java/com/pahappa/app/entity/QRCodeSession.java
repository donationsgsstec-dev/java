package com.pahappa.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * QRCodeSession Entity for tracking QR code generation and usage.
 * 
 * This entity tracks:
 * - QR code generation time
 * - Expiration time (10 minutes from generation)
 * - Whether the QR code has been used
 * - Which user generated it
 * 
 * @author Pahappa
 * @version 1.0
 */
@Entity
@Table(name = "qr_code_sessions", indexes = {
    @Index(name = "idx_qr_code_data", columnList = "qr_code_data"),
    @Index(name = "idx_user_created_at", columnList = "user_id,created_at"),
    @Index(name = "idx_expires_at", columnList = "expires_at")
})
public class QRCodeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the User who generated this QR code
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The encrypted QR code data
     */
    @Column(name = "qr_code_data", nullable = false, unique = true, length = 1000)
    private String qrCodeData;

    /**
     * When the QR code was generated
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * When the QR code expires (10 minutes from creation)
     */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Whether the QR code has been used
     */
    @Column(name = "is_used", nullable = false)
    private boolean isUsed = false;

    /**
     * When the QR code was used (if applicable)
     */
    @Column(name = "used_at")
    private LocalDateTime usedAt;

    /**
     * IP address from which QR code was generated (optional)
     */
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    /**
     * Default constructor
     */
    public QRCodeSession() {
    }

    /**
     * Constructor with essential fields
     */
    public QRCodeSession(User user, String qrCodeData) {
        this.user = user;
        this.qrCodeData = qrCodeData;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = this.createdAt.plusMinutes(10);
        this.isUsed = false;
    }

    /**
     * Check if QR code is expired
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    /**
     * Check if QR code is valid (not used and not expired)
     */
    public boolean isValid() {
        return !isUsed && !isExpired();
    }

    /**
     * Mark QR code as used
     */
    public void markAsUsed() {
        this.isUsed = true;
        this.usedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "QRCodeSession{" +
                "id=" + id +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", isUsed=" + isUsed +
                ", isExpired=" + isExpired() +
                '}';
    }
}

// Made with Bob