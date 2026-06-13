package com.pahappa.app.repository;

import com.pahappa.app.entity.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for WorkSchedule entity.
 * 
 * Provides database operations for work schedule management.
 * 
 * @author Pahappa
 * @version 1.0
 */
@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {

    /**
     * Find the active work schedule
     * @return Optional containing the active schedule
     */
    Optional<WorkSchedule> findByActiveTrue();

    /**
     * Find work schedule by name
     * @param name Schedule name
     * @return Optional containing the schedule
     */
    Optional<WorkSchedule> findByName(String name);
}

// Made with Bob