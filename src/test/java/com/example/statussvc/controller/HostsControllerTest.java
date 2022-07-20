package com.example.statussvc.controller;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.example.statussvc.Constants;
import com.example.statussvc.service.HostsService;
import com.example.statussvc.wire.request.HostCreateDto;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
            GIVEN valid hostCreateDto object
            WHEN performing POST request
            THEN return response with code 201 and valid location
            """)
    void postCorrectData() throws Exception {
        // GIVEN
        HostCreateDto hostCreateDto = HostCreateDto.builder()
                .title("Google")
                .description("Google Description")
                .url("https://google.com/")
                .connectionTime(400)
                .status("ACTIVE")
                .build();
        given(hostsService.save(any())).willReturn(100L);
        // WHEN
        MockHttpServletResponse actualResponse = mockMvc
                .perform(post(Constants.API_V1 + Constants.URL_SEPARATOR + Constants.HOSTS)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(hostCreateDto))
                )
                // THEN
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        // AND THEN
        assertThat(actualResponse.containsHeader("location")).isTrue();
        assertThat(actualResponse.getHeader("location")).isEqualTo("/api/v1/hosts/100");
    }

    @Test
    @DisplayName("""
            GIVEN invalid hostCreateDto object
            WHEN performing POST request
            THEN return response with code 404 and message "Not Found"
            """)
    void postInvalidData() throws Exception {
        // GIVEN
        HostCreateDto hostCreateDto = HostCreateDto.builder()
                .title("Google")
                .description("Google Description")
                .url(null)
                .connectionTime(400)
                .status("ACTIVE")
                .build();
        given(hostsService.save(any())).willReturn(101L);
        // WHEN
        mockMvc
                .perform(post(Constants.API_V1 + Constants.URL_SEPARATOR + Constants.HOSTS)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(hostCreateDto))
                )
                // THEN
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();
    }
}
