package com.example.statussvc.controller;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.example.statussvc.service.HostsService;
import com.example.statussvc.wire.request.HostCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HostsController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@MockBean(Tracer.class)
@MockBean(HostsService.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class HostsControllerTest {
    private final MockMvc mockMvc;
    private final Tracer tracer;
    @Autowired
    private final HostsService hostsService;

    private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @BeforeEach
    public void mockTracer() {
        var span = mock(Span.class);
        given(tracer.currentSpan()).willReturn(span);
        given(span.context())
                .willReturn(TraceContext.newBuilder()
                        .traceId(1)
                        .spanId(1)
                        .build());
    }

    @Test
    @DisplayName("""
            GIVEN valid hostCreateRequest object
            WHEN performing POST request
            THEN return response with code 201, valid location and empty body
            """)
    void postCorrectData() throws Exception {
        // GIVEN
        final String EXPECTED_RESPONSE_URL = "/api/v1/hosts/100";
        HostCreateRequest hostCreateRequest = HostCreateRequest.builder()
                .title("Google")
                .description("Google Description")
                .url("https://google.com/")
                .build();
        given(hostsService.create(hostCreateRequest)).willReturn(100L);
        // WHEN
        MockHttpServletResponse actualResponse = mockMvc
                .perform(post("/api/v1/hosts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(hostCreateRequest))
                )
                // THEN
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        // AND THEN
        assertThat(actualResponse.getHeader(HttpHeaders.LOCATION))
                .isNotEmpty()
                .isEqualTo(EXPECTED_RESPONSE_URL);
        assertThat(actualResponse.getHeader("location")).isEqualTo(EXPECTED_RESPONSE_URL);
        assertThat(actualResponse.getContentAsString()).isEmpty();
    }

    @Test
    @DisplayName("""
            GIVEN invalid hostCreateRequest object
            WHEN performing POST request
            THEN return response with code 400 and message "Bad Request"
            """)
    void postInvalidData() throws Exception {
        // GIVEN
        HostCreateRequest hostCreateRequest = HostCreateRequest.builder()
                .title("Google")
                .description("Google Description")
                .url(null)
                .build();
        given(hostsService.create(hostCreateRequest)).willReturn(101L);
        // WHEN
        mockMvc
                .perform(post("/api/v1/hosts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(hostCreateRequest))
                )
                // THEN
                .andExpect(status().isBadRequest());
    }
}
