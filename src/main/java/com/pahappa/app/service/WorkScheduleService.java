package com.pahappa.app.service;

import com.pahappa.app.entity.WorkSchedule;

import java.util.Optional;

/**
 * Service interface for WorkSchedule operations.
 *
 * Defines business logic for managing the active work schedule
 * used to evaluate attendance punctuality.
 *
 * @author Pahappa
 * @version 1.0
 */
public interface WorkScheduleService {

    /**
     * Get the currently active work schedule.
     * Creates a default schedule if none exists.
     *
     * @return the active WorkSchedule
     */
    WorkSchedule getActiveSchedule();

    /**
     * Save (create or update) a work schedule.
     * Ensures only one schedule is active at a time.
     *
     * @param schedule the schedule to save
     * @return the saved WorkSchedule
     */
    WorkSchedule saveSchedule(WorkSchedule schedule);
}

// Made with Bob
