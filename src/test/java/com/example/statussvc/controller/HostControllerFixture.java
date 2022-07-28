package com.example.statussvc.controller;

import com.example.statussvc.wire.request.CreateHostRequest;

public class HostControllerFixture {

    static final Long HOST_ID_VALID = 1024L;
    static final CreateHostRequest CREATE_HOST_REQUEST = CreateHostRequest.builder()
            .title("Google")
            .description("Google Description")
            .url("https://google.com/")
            .build();
    static final String CREATE_HOST_REQUEST_INVALID = "{\"test\":\"test\"}";
    static final String CREATE_HOST_RESPONSE_BAD_REQUEST_MESSAGE = "description: must not be blank, title: must not be blank, url: must not be blank";
    private static final String API_V1 = "/api/v1";
    static final String HOSTS_URL_VALID = API_V1 + "/hosts";
    static final String HOST_URL_VALID = HOSTS_URL_VALID + "/{id}";
    private static final String URL_PATH_SEPARATOR = "/";
    static final String EXPECTED_CREATED_URL = HOSTS_URL_VALID + URL_PATH_SEPARATOR + HOST_ID_VALID;

}
