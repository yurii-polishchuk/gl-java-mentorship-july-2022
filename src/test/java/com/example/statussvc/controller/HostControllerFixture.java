package com.example.statussvc.controller;

import com.example.statussvc.domain.type.Status;
import com.example.statussvc.wire.request.CreateHostRequest;
import com.example.statussvc.wire.response.RetrieveAllHostsResponse;
import com.example.statussvc.wire.response.RetrieveHostResponse;
import com.example.statussvc.wire.type.StatusRest;
import org.springframework.data.domain.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HostControllerFixture {

    static final Long HOST_ID_VALID = 1024L;
    static final Long HOST_ID_INVALID = 42L;
    static final String STORAGE_EXCEPTION_MESSAGE = "Connection Error";
    static final String NOT_FOUND_EXCEPTION_MESSAGE = "NOT_FOUND";
    static final LocalDateTime LAST_CHECK_DATE_TIME = LocalDateTime.parse("2022-08-02T10:15:30");
    static final PageRequest PAGE_REQUEST = PageRequest.of(0, 3, Sort.Direction.ASC, "id");
    static final CreateHostRequest CREATE_HOST_REQUEST = CreateHostRequest.builder()
            .title("Google")
            .description("Google Description")
            .url("https://google.com/")
            .build();
    static final RetrieveAllHostsResponse FIRST_RETRIEVE_HOST_RESPONSE = RetrieveAllHostsResponse.builder()
            .title("Google")
            .description("Google Description")
            .url("https://google.com/")
            .id(1L)
            .status(StatusRest.UNKNOWN)
            .connectionTime(Duration.ZERO)
            .lastCheck(LAST_CHECK_DATE_TIME)
            .build();
    static final RetrieveAllHostsResponse SECOND_RETRIEVE_HOST_RESPONSE = RetrieveAllHostsResponse.builder()
            .title("Bing")
            .description("Bing Description")
            .url("https://bing.com/")
            .id(2L)
            .status(StatusRest.UNKNOWN)
            .connectionTime(Duration.ZERO)
            .lastCheck(LocalDateTime.now())
            .build();
    static final RetrieveHostResponse RETRIEVE_HOST_RESPONSE_VALID = RetrieveHostResponse.builder()
            .title("Apple")
            .description("Apple Inc.")
            .url("https://apple.com/")
            .id(3L)
            .status(StatusRest.ACTIVE)
            .connectionTime(Duration.ofMillis(7L))
            .lastCheck(LAST_CHECK_DATE_TIME)
            .build();


    static final List<RetrieveAllHostsResponse> RETRIEVE_ALL_HOSTS_RESPONSE = List.of(FIRST_RETRIEVE_HOST_RESPONSE, SECOND_RETRIEVE_HOST_RESPONSE);
    static final PageResponse<RetrieveAllHostsResponse> GET_ALL_HOSTS_RESPONSE_PAGEABLE = new PageResponse<>(
            RETRIEVE_ALL_HOSTS_RESPONSE,
            PAGE_REQUEST
    );
    static final PageResponse<RetrieveAllHostsResponse> GET_ALL_HOSTS_EMPTY_RESPONSE_PAGEABLE = new PageResponse<>(List.of(), PAGE_REQUEST);

    static final String CREATE_HOST_REQUEST_INVALID = "{\"test\":\"test\"}";
    static final String CREATE_HOST_RESPONSE_BAD_REQUEST_MESSAGE = "description: must not be blank, title: must not be blank, url: must not be blank";
    private static final String API_V1 = "/api/v1";
    static final String HOSTS_URL_VALID = API_V1 + "/hosts";
    static final String HOST_URL_VALID = HOSTS_URL_VALID + "/{id}";
    private static final String URL_PATH_SEPARATOR = "/";
    static final String EXPECTED_CREATED_URL = HOSTS_URL_VALID + URL_PATH_SEPARATOR + HOST_ID_VALID;

}
