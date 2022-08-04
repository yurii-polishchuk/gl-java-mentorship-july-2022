package com.example.statussvc.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Generic Monitor Service
 */
@Log4j2
@Service
@EnableScheduling
public class MonitorService {

    /**
     * Add docs
     */
    @Scheduled(cron = "${application.scheduler.host.availability.cron}")
    public void checkHostsAvailability() {
        log.info("checking...");
        // Implement
    }

}
