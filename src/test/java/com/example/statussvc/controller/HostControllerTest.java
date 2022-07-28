package com.example.statussvc.controller;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.example.statussvc.service.HostsService;
import com.example.statussvc.wire.request.CreateHostRequest;
import com.example.statussvc.wire.response.RestContractExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static com.example.statussvc.controller.HostControllerFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HostsController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@MockBean(
        classes = {
                HostsService.class,
                Tracer.class
        },
        answer = Answers.RETURNS_SMART_NULLS
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class HostControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

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
                        .build());
    }

    @Test
    @DisplayName("""
            GIVEN valid createHostRequest object
            WHEN performing POST request
            THEN return response with code 201, valid location and empty body
            """)
    void createHostValid() throws Exception {
        // GIVEN
        given(hostsService.create(CREATE_HOST_REQUEST)).willReturn(HOST_ID_VALID);

        // WHEN
        MockHttpServletResponse actualResponse = mockMvc
                .perform(post(HOSTS_URL_VALID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(CREATE_HOST_REQUEST))
                )
                // THEN
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        // AND THEN
        assertThat(actualResponse.getHeader(HttpHeaders.LOCATION))
                .isNotEmpty()
                .isEqualTo(EXPECTED_CREATED_URL);
        assertThat(actualResponse.getContentAsString()).isEmpty();
    }

    @Test
    @DisplayName("""
            GIVEN invalid createHostRequest object
            WHEN performing POST request
            THEN return response with code 400 and message "Bad Request"
            """)
    void createHostBadRequest() throws Exception {
        // GIVEN

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(post(HOSTS_URL_VALID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CREATE_HOST_REQUEST_INVALID)
                        )
                        // THEN
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(CREATE_HOST_RESPONSE_BAD_REQUEST_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN endpoint with existing id
            WHEN performing GET request
            THEN return host data with code 200
            """)

    void HostByExistedId() throws Exception {
        // GIVEN
        CreateHostRequest hostCreateRequest = CreateHostRequest.builder()
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

    @SneakyThrows(JsonProcessingException.class)
    private String toJson(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    @SneakyThrows(JsonProcessingException.class)
    public <T> T fromJson(String string, Class<T> type) {
        return Objects.nonNull(string) ? objectMapper.readValue(string, type) : null;
    }

}
