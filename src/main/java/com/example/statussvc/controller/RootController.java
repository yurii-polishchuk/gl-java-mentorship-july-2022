package com.example.statussvc.controller;

import com.example.statussvc.Constants;
import com.example.statussvc.configuration.ApplicationProperties;
import com.example.statussvc.wire.response.TestResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Entry point for Root endpoint APIs.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.URL_SEPARATOR)
public class RootController {

    private final ApplicationProperties applicationProperties;

    /**
     * Handle root endpoint.
     *
     * @return ResponseEntity with proper HttpStatus
     */
    @GetMapping
    public ResponseEntity<TestResponse> rootEndpoint() {
        TestResponse build = TestResponse.builder().test2("test").testMap(Map.of("test1", "test2")).listStrings(List.of("test")).build();
        return new ResponseEntity<>(build, HttpStatus.OK);
    }

}
