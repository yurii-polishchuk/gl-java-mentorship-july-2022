package com.example.statussvc;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ApplicationTest {

    private final Application application;

    @Test
    @DisplayName("""
            GIVEN application
            WHEN spring context starts
            THEN verify that application has started
            """)
    void applicationContextLoads() {
        // GIVEN

        // WHEN
        Application.main(ArrayUtils.EMPTY_STRING_ARRAY);

        // THEN
        assertThat(application).isNotNull();
    }
}
