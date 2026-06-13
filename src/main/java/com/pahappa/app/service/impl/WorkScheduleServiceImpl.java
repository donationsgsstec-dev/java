package com.pahappa.app.service.impl;

import com.pahappa.app.entity.WorkSchedule;
import com.pahappa.app.repository.WorkScheduleRepository;
import com.pahappa.app.service.WorkScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Optional;

/**
 * Implementation of WorkScheduleService.
 *
 * Manages the single active work schedule used across the system.
 * If no schedule exists in the database, a sensible default is created
 * automatically so the settings page always has something to display.
 *
 * @author Pahappa
 * @version 1.0
 */
@Service
@Transactional
public class WorkScheduleServiceImpl implements WorkScheduleService {

    private final WorkScheduleRepository workScheduleRepository;

    @Autowired
    public WorkScheduleServiceImpl(WorkScheduleRepository workScheduleRepository) {
        this.workScheduleRepository = workScheduleRepository;
    }

    /**
     * Returns the active schedule, creating a default one if none exists.
     */
    @Override
    public WorkSchedule getActiveSchedule() {
        return workScheduleRepository.findByActiveTrue()
                .orElseGet(this::createDefaultSchedule);
    }

    /**
     * Saves the given schedule as the sole active schedule.
     * Any previously active schedule is deactivated first.
     */
    @Override
    public WorkSchedule saveSchedule(WorkSchedule schedule) {
        // Deactivate any existing active schedule (other than the one being saved)
        workScheduleRepository.findByActiveTrue().ifPresent(existing -> {
            if (!existing.getId().equals(schedule.getId())) {
                existing.setActive(false);
                workScheduleRepository.save(existing);
            }
        });

        schedule.setActive(true);
        return workScheduleRepository.save(schedule);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Creates and persists a default work schedule (Mon–Fri, 09:00–17:00).
     */
    private WorkSchedule createDefaultSchedule() {
        WorkSchedule defaultSchedule = new WorkSchedule(
                "Default Schedule",
                LocalTime.of(9, 0),
                LocalTime.of(17, 0)
        );
        defaultSchedule.setGracePeriodMinutes(15);
        defaultSchedule.setMinimumWorkHours(8.0);
        defaultSchedule.setApplicableDays("MON,TUE,WED,THU,FRI");
        defaultSchedule.setNotifyLateArrival(true);
        defaultSchedule.setNotifyEarlyDeparture(true);
        defaultSchedule.setActive(true);
        return workScheduleRepository.save(defaultSchedule);
    }
}

// Made with Bob
