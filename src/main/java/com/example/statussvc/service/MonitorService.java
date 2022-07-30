package com.example.statussvc.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Generic Monitor Service
 */
@Log4j2
@Service
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class MonitorService {

    /**
     * Add docs
     */

    //@Scheduled(cron = "${scheduled.cron}")
    @Scheduled(cron = "0 */5 * * * *")
    public void checkHostsAvailability() {
        log.info("checking...");
        // Implement
    }

}
