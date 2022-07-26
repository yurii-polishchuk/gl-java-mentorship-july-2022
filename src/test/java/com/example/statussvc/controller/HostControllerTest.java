package com.example.statussvc.controller;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.example.statussvc.service.HostsService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HostsController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@MockBean(Tracer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class HostControllerTest {

    private final MockMvc mockMvc;
    private final Tracer tracer;
    private final HostsService hostsService;

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
            GIVEN endpoint with existing id
            WHEN performing GET request
            THEN return host data with code 200
            """)

    void HostByExistedId() throws Exception {
        // GIVEN
        HostCreateRequest hostCreateRequest = HostCreateRequest.builder()
                .title("Google")
                .description("Google Description")
                .url("https://google.com/")
                .build();
        given(hostsService.create(hostCreateRequest)).willReturn(100L);
        // WHEN
        MockHttpServletResponse actualResponse = mockMvc.perform(MockMvcRequestBuilders
                        .get("/hosts/{id}", 100L)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                // AND THEN
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(100L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Google"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Google Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.url").value("https://google.com/"))
                .andReturn()
                .getResponse();
    }

    @Test
    @DisplayName("""
            GIVEN endpoint with not existing id
            WHEN performing GET request
            THEN return 404 Not Found
            """)
    void HostByNotExistingId() throws Exception {
        // GIVEN

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hosts/{id}", 666L)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                // AND THEN
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("get endpoint with not existing id - expected 404 Not Found")
    void HostByNotExistedId() throws Exception {

    }

}
