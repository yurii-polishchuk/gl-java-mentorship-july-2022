package com.example.statussvc.service;

import com.example.statussvc.configuration.ApplicationProperties;
import com.example.statussvc.configuration.SchedulerConfiguration;
import com.example.statussvc.repository.HostsRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.net.http.HttpClient;
import java.time.Duration;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringJUnitConfig(
        classes = {
                MonitorService.class,
                SchedulerService.class,
                SchedulerConfiguration.class
        },
        initializers = ConfigDataApplicationContextInitializer.class
)
@EnableConfigurationProperties(ApplicationProperties.class)
@SpyBean(MonitorService.class)
@MockBean(
        classes = {
                HostsRepository.class,
                HttpClient.class
        },
        answer = Answers.RETURNS_SMART_NULLS
)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class SchedulerServiceTest {

    private final MonitorService monitorService;

    @Test
    @DisplayName("""
            GIVEN valid scheduled monitor task
            WHEN await at most 10 seconds
            THEN verify trigger task once
            """)
    void properlyTriggerAvailableHostCheck() {
        await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> verify(monitorService, atLeastOnce()).checkHostsAvailability());
    }

}
