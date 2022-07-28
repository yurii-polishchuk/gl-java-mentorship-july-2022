package com.example.statussvc.service;

import com.example.statussvc.configuration.ApplicationProperties;
import com.example.statussvc.mapper.HostMapperImpl;
import com.example.statussvc.repository.HostsRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(
        classes = {
                HostsService.class,
                HostMapperImpl.class
        },
        initializers = ConfigDataApplicationContextInitializer.class
)
@EnableConfigurationProperties(ApplicationProperties.class)
@MockBean(
        classes = HostsRepository.class,
        answer = Answers.RETURNS_SMART_NULLS
)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class HostServiceTest {

    private final HostsService hostsService;
    private final HostsRepository hostsRepository;

    // ========== CREATE ==========

    @Test
    @DisplayName("""
            GIVEN valid createHostRequest object
            WHEN create
            THEN return unique id of created entry in storage
            """)
    void createPositive() {
        // Implement Me
    }

    @Test
    @DisplayName("""
            GIVEN valid createHostRequest object
            WHEN create
            THEN return exception from storage
            """)
    void createNegativeException() {
        // Implement Me
    }

    /**
     * Checks if any of given mocks has any unverified interaction.
     */
    private void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(hostsRepository);
    }

}
