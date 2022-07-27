package com.example.statussvc.controller;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.example.statussvc.domain.Status;
import com.example.statussvc.service.HostsService;
<<<<<<< HEAD:src/test/java/com/example/statussvc/controller/HostControllerTest.java
import com.example.statussvc.wire.response.RestContractExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
=======
import com.example.statussvc.wire.request.HostCreateRequest;
import com.example.statussvc.wire.response.HostRetrieveAllResponse;
>>>>>>> 469af82 (First Commit!):src/test/java/com/example/statussvc/controller/HostsControllerTest.java
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

<<<<<<< HEAD:src/test/java/com/example/statussvc/controller/HostControllerTest.java
import java.util.Objects;

import static com.example.statussvc.controller.HostControllerFixture.*;
=======
import java.util.List;

>>>>>>> 469af82 (First Commit!):src/test/java/com/example/statussvc/controller/HostsControllerTest.java
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
class HostControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private final Tracer tracer;
    private final HostsService hostsService;

<<<<<<< HEAD:src/test/java/com/example/statussvc/controller/HostControllerTest.java
=======
    private final String URL = "/api/v1/hosts";

    @Autowired
    private final ObjectMapper objectMapper;

>>>>>>> 469af82 (First Commit!):src/test/java/com/example/statussvc/controller/HostsControllerTest.java
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
<<<<<<< HEAD:src/test/java/com/example/statussvc/controller/HostControllerTest.java
                .perform(post(HOSTS_URL_VALID)
=======
                .perform(post(URL)
>>>>>>> 469af82 (First Commit!):src/test/java/com/example/statussvc/controller/HostsControllerTest.java
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
<<<<<<< HEAD:src/test/java/com/example/statussvc/controller/HostControllerTest.java
                .isEqualTo(EXPECTED_CREATED_URL);
=======
                .isEqualTo(EXPECTED_RESPONSE_URL);
>>>>>>> 469af82 (First Commit!):src/test/java/com/example/statussvc/controller/HostsControllerTest.java
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
<<<<<<< HEAD:src/test/java/com/example/statussvc/controller/HostControllerTest.java
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

    @SneakyThrows(JsonProcessingException.class)
    private String toJson(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    @SneakyThrows(JsonProcessingException.class)
    public <T> T fromJson(String string, Class<T> type) {
        return Objects.nonNull(string) ? objectMapper.readValue(string, type) : null;
    }

=======
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
>>>>>>> 469af82 (First Commit!):src/test/java/com/example/statussvc/controller/HostsControllerTest.java
}
