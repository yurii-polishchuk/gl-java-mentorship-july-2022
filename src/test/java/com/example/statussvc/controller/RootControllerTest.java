package com.example.statussvc.controller;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RootController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@MockBean(Tracer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RootControllerTest {

    private final MockMvc mockMvc;
    private final Tracer tracer;

    @BeforeEach
    public void mockTracer() {
        var span = mock(Span.class);
        given(tracer.currentSpan()).willReturn(span);
        given(span.context())
                .willReturn(TraceContext.newBuilder()
                        .traceId(1)
                        .spanId(1)
                        .build()
                );
    }

    @Test
    @DisplayName("""
            GIVEN valid root endpoint
            WHEN performing valid GET request
            THEN return empty success response
            """)
    void rootEndpointPositive() throws Exception {
        // GIVEN

        // WHEN
        MockHttpServletResponse actualResponse = mockMvc.perform(get("/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(StringUtils.EMPTY))

                // THEN
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // AND THEN
        assertThat(actualResponse.getContentAsString()).isEmpty();
    }

}
