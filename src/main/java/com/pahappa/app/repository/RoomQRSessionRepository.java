package com.pahappa.app.repository;

import com.pahappa.app.entity.RoomQRSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository for RoomQRSession entities.
 *
 * @author Pahappa
 * @version 1.0
 */
@Repository
public interface RoomQRSessionRepository extends JpaRepository<RoomQRSession, Long> {

    /** Look up a session by its token string */
    Optional<RoomQRSession> findByToken(String token);

    /** Find the most recently created active session of a given type */
    @Query("SELECT r FROM RoomQRSession r WHERE r.sessionType = :type AND r.invalidated = false AND r.expiresAt > :now ORDER BY r.createdAt DESC")
    Optional<RoomQRSession> findLatestActiveByType(RoomQRSession.SessionType type, LocalDateTime now);

    /** Invalidate all currently active sessions (called before generating a new pair) */
    @Modifying
    @Query("UPDATE RoomQRSession r SET r.invalidated = true WHERE r.invalidated = false")
    void invalidateAll();

    /** Purge sessions older than a given threshold */
    @Modifying
    @Query("DELETE FROM RoomQRSession r WHERE r.expiresAt < :threshold")
    void deleteOlderThan(LocalDateTime threshold);
}
