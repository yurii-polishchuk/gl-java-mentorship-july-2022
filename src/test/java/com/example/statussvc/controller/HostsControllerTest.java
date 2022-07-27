package com.example.statussvc.controller;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.example.statussvc.domain.Status;
import com.example.statussvc.service.HostsService;
import com.example.statussvc.wire.request.HostCreateRequest;
import com.example.statussvc.wire.response.HostRetrieveAllResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class HostsControllerTest {
    private final MockMvc mockMvc;
    private final Tracer tracer;
    private final HostsService hostsService;

    private final String URL = "/api/v1/hosts";

    @Autowired
    private final ObjectMapper objectMapper;

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
                .perform(post(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hostCreateRequest))
                )
                // THEN
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        // AND THEN
        assertThat(actualResponse.getHeader(HttpHeaders.LOCATION))
                .isNotEmpty()
                .isEqualTo(EXPECTED_RESPONSE_URL);
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
                .perform(post(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hostCreateRequest))
                )
                // THEN
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("""
            GIVEN default page number and page size
            WHEN performing GET request
            THEN return response with code 200 and list of hosts
            """)
    void getAllCorrectData() throws Exception {
        // GIVEN
        final String EXPECTED_RESPONSE = "{\"totalItems\":2,\"hosts\":[{\"id\":1,\"title\":\"Yahoo\",\"description\":null,\"url\":\"http://yahoo.com/\",\"connectionTime\":null,\"lastCheck\":null,\"status\":\"UNKNOWN\"},{\"id\":2,\"title\":\"Google\",\"description\":\"Google Engine\",\"url\":\"https://google.com/\",\"connectionTime\":null,\"lastCheck\":null,\"status\":\"UNKNOWN\"}],\"totalPages\":1,\"currentPage\":0}";
        Page<HostRetrieveAllResponse> hostsPage = new PageImpl<>(List.of(
                new HostRetrieveAllResponse(1L, "Yahoo", null, "http://yahoo.com/", null, null, Status.UNKNOWN),
                new HostRetrieveAllResponse(2L, "Google", "Google Engine", "https://google.com/", null, null,Status.UNKNOWN)
        ));
        Pageable paging = PageRequest.of(0, 3);

        given(hostsService.retrieveAll(paging)).willReturn(hostsPage);
        // WHEN
        MockHttpServletResponse actualResponse = mockMvc
                .perform(get(URL)
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        // AND THEN
        assertThat(actualResponse.getContentAsString()).isEqualTo(EXPECTED_RESPONSE);
    }

    @Test
    @DisplayName("""
            GIVEN invalid page number and page size
            WHEN performing GET request
            THEN return response with code 400 BAD REQUEST
            """)
    void getAllDataWithInvalidParams() throws Exception {
        // GIVEN
        final String GIVEN_URL = "http://localhost:8080/api/v1/hosts?page=-1&size=-4";
        Page<HostRetrieveAllResponse> hostsPage = new PageImpl<>(List.of(
                new HostRetrieveAllResponse(1L, "Yahoo", null, "http://yahoo.com/", null, null, Status.UNKNOWN),
                new HostRetrieveAllResponse(2L, "Google", "Google Engine", "https://google.com/", null, null,Status.UNKNOWN)
        ));

        given(hostsService.retrieveAll(Pageable.unpaged())).willReturn(hostsPage);
        // WHEN
        mockMvc
                .perform(get(GIVEN_URL)
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isBadRequest());
    }
}
