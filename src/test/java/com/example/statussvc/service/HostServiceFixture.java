package com.example.statussvc.service;

import com.example.statussvc.domain.Host;
import com.example.statussvc.domain.type.Status;
import com.example.statussvc.wire.request.CreateHostRequest;

import java.time.Duration;
import java.time.LocalDateTime;

public class HostServiceFixture {

    static final Long HOST_ID_VALID = 1024L;

    private static final String HOST_TITLE_VALID = "Google";
    private static final String HOST_DESCRIPTION_VALID = "Google Description";
    private static final String HOST_URL_VALID = "https://google.com/";

    static final CreateHostRequest CREATE_HOST_REQUEST_VALID = CreateHostRequest.builder()
            .title(HOST_TITLE_VALID)
            .description(HOST_DESCRIPTION_VALID)
            .url(HOST_URL_VALID)
            .build();

    static final Host HOST_NEW_SAVE = Host.builder()
            .title(HOST_TITLE_VALID)
            .description(HOST_DESCRIPTION_VALID)
            .url(HOST_URL_VALID)
            .connectionTime(Duration.ZERO)
            .status(Status.UNKNOWN)
            .build();

    static final Host HOST_NEW_SAVED = Host.builder()
            .id(HOST_ID_VALID)
            .title(HOST_TITLE_VALID)
            .description(HOST_DESCRIPTION_VALID)
            .url(HOST_URL_VALID)
            .connectionTime(Duration.ZERO)
            .status(Status.UNKNOWN)
            .lastCheck(LocalDateTime.MIN)
            .build();

}
