package com.pahappa.app.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.pahappa.app.entity.RoomQRSession;
import com.pahappa.app.entity.RoomQRSession.SessionType;
import com.pahappa.app.repository.RoomQRSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.imageio.ImageIO;

/**
 * Service for managing admin-generated room QR codes.
 *
 * The admin generates two QR codes per session: one for CHECK_IN and one for
 * CHECK_OUT.  Each code has a large label rendered in the centre of the image
 * so there is no ambiguity about which code does what.
 *
 * The check-out code is rejected by the server if the intern has not yet
 * checked in today.
 *
 * @author Pahappa
 * @version 1.0
 */
@Service
@Transactional
public class RoomQRService {

    /** Fallback expiry in minutes when today's deadline has already passed */
    private static final int FALLBACK_EXPIRY_MINUTES = 60;

    /** Size of the generated QR code image in pixels */
    private static final int QR_SIZE = 400;
    
    /** Base URL for QR code scanning */
    private static final String BASE_URL = "https://pahappa-attendance-system-production.up.railway.app";

    private final RoomQRSessionRepository roomQRSessionRepository;
    private final WorkScheduleService workScheduleService;
    private final EmailService emailService;

    @Autowired
    public RoomQRService(RoomQRSessionRepository roomQRSessionRepository,
                         WorkScheduleService workScheduleService,
                         EmailService emailService) {
        this.roomQRSessionRepository = roomQRSessionRepository;
        this.workScheduleService = workScheduleService;
        this.emailService = emailService;
    }

    // ── Admin side ────────────────────────────────────────────────────────────

    /**
     * Generate a CHECK_IN and a CHECK_OUT QR code for today.
     *
     * Check-in codes expire at the configured work-start time (09:00 by
     * default).  Check-out codes expire at the configured work-end time
     * (17:00 by default).  If the deadline has already passed, both fall back
     * to now + FALLBACK_EXPIRY_MINUTES.
     *
     * Any previously active sessions are invalidated first.
     *
     * @param label optional session label displayed on both codes
     */
    public void generateNewSession(String label) {
        roomQRSessionRepository.invalidateAll();

        LocalTime startTime = workScheduleService.getActiveSchedule().getWorkStartTime();
        LocalTime endTime   = workScheduleService.getActiveSchedule().getWorkEndTime();

        LocalDateTime checkInExpiry  = resolveExpiry(startTime);
        LocalDateTime checkOutExpiry = resolveExpiry(endTime);

        RoomQRSession checkIn = new RoomQRSession(
                UUID.randomUUID().toString().replace("-", ""),
                SessionType.CHECK_IN, checkInExpiry, label);

        RoomQRSession checkOut = new RoomQRSession(
                UUID.randomUUID().toString().replace("-", ""),
                SessionType.CHECK_OUT, checkOutExpiry, label);

        roomQRSessionRepository.save(checkIn);
        roomQRSessionRepository.save(checkOut);

        // Notify all interns — failure must never block QR generation
        try {
            String expiresAtStr = checkInExpiry.format(DateTimeFormatter.ofPattern("HH:mm"));
            emailService.sendRoomQRNotification(expiresAtStr, label);
        } catch (Exception e) {
            System.err.println("Room QR notification failed (non-fatal): " + e.getMessage());
        }
    }

    /** Get the active CHECK_IN session, if any. */
    @Transactional(readOnly = true)
    public Optional<RoomQRSession> getActiveCheckInSession() {
        return roomQRSessionRepository.findLatestActiveByType(SessionType.CHECK_IN, LocalDateTime.now());
    }

    /** Get the active CHECK_OUT session, if any. */
    @Transactional(readOnly = true)
    public Optional<RoomQRSession> getActiveCheckOutSession() {
        return roomQRSessionRepository.findLatestActiveByType(SessionType.CHECK_OUT, LocalDateTime.now());
    }

    /** Explicitly invalidate all active sessions (admin revokes). */
    public void invalidateCurrentSession() {
        roomQRSessionRepository.invalidateAll();
    }

    // ── QR image rendering ────────────────────────────────────────────────────

    /**
     * Render a session as a Base64-encoded PNG with the session type label
     * ("CHECK IN" or "CHECK OUT") overlaid in the centre of the image.
     */
    public String renderQRCodeBase64(RoomQRSession session) throws WriterException, IOException {
        return Base64.getEncoder().encodeToString(renderQRCodeBytes(session));
    }

    /**
     * Render a session as raw PNG bytes with a centred label overlay.
     */
    public byte[] renderQRCodeBytes(RoomQRSession session) throws WriterException, IOException {
        // 1. Generate the raw QR matrix
        QRCodeWriter writer = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // H = 30% recovery
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);

        // Create full URL with token and session type
        String qrCodeUrl = BASE_URL + "/attendance/qr/room-scan?token=" + session.getToken() +
                          "&type=" + session.getSessionType().name();
        
        BitMatrix matrix = writer.encode(qrCodeUrl, BarcodeFormat.QR_CODE,
                QR_SIZE, QR_SIZE, hints);

        // 2. Convert BitMatrix → BufferedImage
        BufferedImage qrImage = new BufferedImage(QR_SIZE, QR_SIZE, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < QR_SIZE; x++) {
            for (int y = 0; y < QR_SIZE; y++) {
                qrImage.setRGB(x, y, matrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }

        // 3. Overlay the label in the centre (white rounded rectangle + bold text)
        Graphics2D g = qrImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        String labelText = session.getSessionType() == SessionType.CHECK_IN ? "CHECK IN" : "CHECK OUT";

        // Choose font size — CHECK OUT is longer so scale down slightly
        int fontSize = session.getSessionType() == SessionType.CHECK_IN ? 28 : 24;
        Font font = new Font("SansSerif", Font.BOLD, fontSize);
        g.setFont(font);

        FontMetrics fm = g.getFontMetrics();
        int textWidth  = fm.stringWidth(labelText);
        int textHeight = fm.getAscent();

        int padding = 12;
        int boxW = textWidth  + padding * 2;
        int boxH = textHeight + padding * 2;
        int boxX = (QR_SIZE - boxW) / 2;
        int boxY = (QR_SIZE - boxH) / 2;

        // White rounded rectangle background
        g.setColor(Color.WHITE);
        g.fillRoundRect(boxX, boxY, boxW, boxH, 16, 16);

        // Coloured border — green for check-in, purple for check-out
        Color borderColor = session.getSessionType() == SessionType.CHECK_IN
                ? new Color(40, 167, 69)   // Bootstrap success green
                : new Color(102, 126, 234); // Brand purple
        g.setColor(borderColor);
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(boxX, boxY, boxW, boxH, 16, 16);

        // Draw text
        g.setColor(borderColor);
        g.drawString(labelText, boxX + padding, boxY + padding + textHeight - 2);
        g.dispose();

        // 4. Encode to PNG bytes
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", out);
        return out.toByteArray();
    }

    // ── Intern side ───────────────────────────────────────────────────────────

    /**
     * Validate a token and return its session type, or empty if invalid.
     * The caller uses this to know whether the scanned code is CHECK_IN or
     * CHECK_OUT without the intern having to choose.
     */
    @Transactional(readOnly = true)
    public Optional<SessionType> validateToken(String token) {
        return roomQRSessionRepository.findByToken(token)
                .filter(RoomQRSession::isValid)
                .map(RoomQRSession::getSessionType);
    }

    // ── Maintenance ───────────────────────────────────────────────────────────

    /** Remove sessions that expired more than 24 hours ago. */
    public void purgeOldSessions() {
        roomQRSessionRepository.deleteOlderThan(LocalDateTime.now().minusHours(24));
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Calculate the expiry LocalDateTime for a given wall-clock time today.
     * Falls back to now + FALLBACK_EXPIRY_MINUTES if the time has already passed.
     */
    private LocalDateTime resolveExpiry(LocalTime wallClock) {
        LocalDateTime deadline = LocalDate.now().atTime(wallClock);
        if (!LocalDateTime.now().isBefore(deadline)) {
            deadline = LocalDateTime.now().plusMinutes(FALLBACK_EXPIRY_MINUTES);
        }
        return deadline;
    }
}
