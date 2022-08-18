package com.example.statussvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Various Schedulers
 */
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final MonitorService monitorService;

    /**
     * Triggers monitoring CRON scheduler job.
     */
    @Scheduled(cron = "${application.scheduler.host.availability.cron}")
    public void monitorHosts() {
        monitorService.checkHostsAvailability();
    }

}
