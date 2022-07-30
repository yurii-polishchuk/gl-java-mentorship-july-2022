package com.example.statussvc.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.Duration;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringJUnitConfig(MonitorService.class)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MonitorServiceTest {

    @SpyBean
    private final MonitorService monitorService;

    @Test
    @DisplayName("""
            GIVEN valid scheduled task
            WHEN await at most 10 minutes
            THEN verify trigger task at least twice
            """)
    public void properlyTrigger() {
        await()
                .atMost(Duration.ofMinutes(10L))
                .untilAsserted(() -> verify(monitorService, atLeast(2)).checkHostsAvailability());
    }
}
