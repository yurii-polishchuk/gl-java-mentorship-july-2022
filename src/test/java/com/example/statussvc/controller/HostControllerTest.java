package com.example.statussvc.controller;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.example.statussvc.service.HostsService;
import com.example.statussvc.wire.response.RestContractExceptionResponse;
import com.example.statussvc.wire.response.RetrieveAllHostsResponse;
import com.example.statussvc.wire.response.RetrieveHostResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

import static com.example.statussvc.controller.HostControllerFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HostsController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@MockBean(
        classes = {
                HostsService.class,
                Tracer.class,
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
            GIVEN default page number and page size
            WHEN performing GET request
            THEN return response with code 200 and list of hosts
            """)
    void retrieveAllHostsValid() throws Exception {
        // GIVEN
        given(hostsService.retrieveAll(PAGE_REQUEST)).willReturn(GET_ALL_HOSTS_RESPONSE_PAGEABLE);

        // WHEN
        PageResponse<RetrieveAllHostsResponse> actualResponse = fromJson(mockMvc
                        .perform(get(HOSTS_URL_VALID)
                                .accept(MediaType.APPLICATION_JSON))
                        // THEN
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                new TypeReference<>() {
                }
        );

        // AND THEN
        assertThat(actualResponse.getTotalElements()).isEqualTo(2L);
        assertThat(actualResponse.getTotalPages()).isEqualTo(1);
        assertThat(actualResponse.getContent()).isEqualTo(RETRIEVE_ALL_HOSTS_RESPONSE);
    }

    @Test
    @DisplayName("""
            GIVEN default page number and page size
            WHEN performing GET request
            THEN return response with code 200 and empty list of hosts
            """)
    void retrieveAllHostsEmptyValid() throws Exception {
        // GIVEN
        given(hostsService.retrieveAll(PAGE_REQUEST)).willReturn(GET_ALL_HOSTS_EMPTY_RESPONSE_PAGEABLE);

        // WHEN
        PageResponse<RetrieveAllHostsResponse> actualResponse = fromJson(mockMvc
                        .perform(get(HOSTS_URL_VALID)
                                .accept(MediaType.APPLICATION_JSON))
                        // THEN
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                new TypeReference<>() {
                }
        );

        // AND THEN
        assertThat(actualResponse.getTotalElements()).isZero();
        assertThat(actualResponse.getTotalPages()).isZero();
        assertThat(actualResponse.getContent()).isEmpty();
    }

    @Test
    @DisplayName("""
            GIVEN default page number and page size
            WHEN performing GET request and Storage returns exception
            THEN return response with code 500 and error message
            """)
    void retrieveAllHostsInternalServiceError() throws Exception {
        // GIVEN
        given(hostsService.retrieveAll(PAGE_REQUEST))
                .willThrow(new JDBCConnectionException(STORAGE_EXCEPTION_MESSAGE, null));

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(get(HOSTS_URL_VALID)
                                .accept(MediaType.APPLICATION_JSON))
                        // THEN
                        .andExpect(status().isInternalServerError())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(STORAGE_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid host id
            WHEN performing GET request
            THEN return response with code 200 and host entry
            """)
    void retrieveOneHostByIdValid() throws Exception {
        // GIVEN
        given(hostsService.retrieve(HOST_ID_VALID)).willReturn(RETRIEVE_HOST_RESPONSE_VALID);

        // WHEN
        RetrieveHostResponse actualResponse = fromJson(mockMvc
                        .perform(get(HOST_URL_VALID, HOST_ID_VALID)
                                .accept(MediaType.APPLICATION_JSON))

                        // THEN
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RetrieveHostResponse.class);

        // AND THEN
        assertThat(actualResponse).isEqualTo(RETRIEVE_HOST_RESPONSE_VALID);
    }

    @Test
    @DisplayName("""
            GIVEN invalid host id
            WHEN performing GET request
            THEN return response with code 404 and no host entry
            """)
    void retrieveOneHostByIdInValid() throws Exception {
        // GIVEN
        given(hostsService.retrieve(HOST_ID_INVALID))
                .willThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(get(HOST_URL_VALID, HOST_ID_INVALID)
                                .accept(MediaType.APPLICATION_JSON))

                        // THEN
                        .andExpect(status().isNotFound())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN invalid host id
            WHEN performing GET request
            THEN return response with code 404 and no host entry
            """)
    void retrieveOneHostByIdInternalServiceError() throws Exception {
        // GIVEN
        given(hostsService.retrieve(HOST_ID_VALID))
                .willThrow(new JDBCConnectionException(STORAGE_EXCEPTION_MESSAGE, null));

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(get(HOST_URL_VALID, HOST_ID_VALID)
                                .accept(MediaType.APPLICATION_JSON))

                        // THEN
                        .andExpect(status().isInternalServerError())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(STORAGE_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid host id and valid host object
            WHEN performing PUT request
            THEN return response with code 200 and no host entry
            """)
    void replaceHostByValidId() throws Exception {
        // GIVEN
        doNothing().when(hostsService).replace(HOST_ID_VALID, REPLACE_HOST_REQUEST);

        // WHEN
        MockHttpServletResponse actualResponse = mockMvc
                .perform(put(HOST_URL_VALID, HOST_ID_VALID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(REPLACE_HOST_REQUEST))
                )
                // THEN
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();

        // AND THEN
        assertThat(actualResponse.getContentAsString()).isEmpty();
    }

    @Test
    @DisplayName("""
            GIVEN invalid host id and valid host object
            WHEN performing PUT request
            THEN return response with code 404 and no host entry
            """)
    void replaceHostByInvalidId() throws Exception {
        // GIVEN
        doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND))
                .when(hostsService).replace(HOST_ID_INVALID, REPLACE_HOST_REQUEST);

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(put(HOST_URL_VALID, HOST_ID_INVALID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(REPLACE_HOST_REQUEST))
                        )
                        // THEN
                        .andExpect(status().isNotFound())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid host id and invalid host object
            WHEN performing PUT request
            THEN return response with code 400 and message "Bad Request"
            """)
    void replaceHostByValidIdAndInvalidReplaceHostRequest() throws Exception {
        // GIVEN
        doNothing().when(hostsService).replace(HOST_ID_VALID, REPLACE_HOST_REQUEST_INVALID);

        // WHEN
        RestContractExceptionResponse actualResponse =
                fromJson(mockMvc
                                .perform(put(HOST_URL_VALID, HOST_ID_VALID)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(toJson(REPLACE_HOST_REQUEST_INVALID))
                                )
                                .andExpect(status().isBadRequest())
                                .andReturn()
                                .getResponse()
                                .getContentAsString(),
                        RestContractExceptionResponse.class);
        //AND THEN
        assertThat(actualResponse.message()).isEqualTo(REPLACE_HOST_RESPONSE_BAD_REQUEST_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid host id
            WHEN performing DELETE request
            THEN return response with code 204 and remove entry
            """)
    void removeHostByValidId() throws Exception {
        // GIVEN
        doNothing().when(hostsService).remove(HOST_ID_VALID);

        // WHEN
        MockHttpServletResponse actualResponse = mockMvc
                .perform(delete(HOST_URL_VALID, HOST_ID_VALID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                // THEN
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();

        // AND THEN
        assertThat(actualResponse.getContentAsString()).isBlank();
    }

    @Test
    @DisplayName("""
            GIVEN malformed host id
            WHEN performing DELETE request
            THEN return response with code 400 and remove entry
            """)
    void removeHostByMalformedId() throws Exception {
        // GIVEN

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(delete(HOST_URL_VALID, HOST_ID_MALFORMED)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        // THEN
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(DELETE_HOST_RESPONSE_BAD_REQUEST_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid host id
            WHEN performing DELETE request
            THEN return response with code 204 and remove entry
            """)
    void removeHostByInvalidId() throws Exception {
        // GIVEN
        doThrow(new EmptyResultDataAccessException(0))
                .when(hostsService).remove(HOST_ID_INVALID);

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(delete(HOST_URL_VALID, HOST_ID_INVALID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(REPLACE_HOST_REQUEST))
                        )
                        // THEN
                        .andExpect(status().isNotFound())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid removal all request
            WHEN performing DELETE all request
            THEN return response with code 204 and remove all entries
            """)
    void removeAllHostsValid() throws Exception {
        // GIVEN
        doNothing().when(hostsService).remove(HOST_ID_VALID);

        // WHEN
        MockHttpServletResponse actualResponse = mockMvc
                .perform(delete(HOST_URL_VALID, HOST_ID_VALID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                // THEN
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();

        // AND THEN
        assertThat(actualResponse.getContentAsString()).isBlank();
    }

    @Test
    @DisplayName("""
            GIVEN valid removal all request
            WHEN performing DELETE all request
            THEN return response with code 404 and remove all entry if not found
            """)
    void removeAllHostsException() throws Exception {
        // GIVEN
        doThrow(new EmptyResultDataAccessException(0))
                .when(hostsService).removeAll();

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(delete(HOSTS_URL_VALID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(REPLACE_HOST_REQUEST))
                        )
                        // THEN
                        .andExpect(status().isNotFound())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @SneakyThrows(JsonProcessingException.class)
    private String toJson(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    @SneakyThrows(JsonProcessingException.class)
    public <T> T fromJson(String string, Class<T> type) {
        return Objects.nonNull(string) ? objectMapper.readValue(string, type) : null;
    }

    @SneakyThrows(JsonProcessingException.class)
    public <T> T fromJson(String string, TypeReference<T> type) {
        return Objects.nonNull(string) ? objectMapper.readValue(string, type) : null;
    }

}
