package com.example.statussvc.controller;

import com.example.statussvc.Constants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Entry point for Root endpoint APIs.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.URL_SEPARATOR)
public class RootController {

    /**
     * Handle root endpoint.
     *
     * @return ResponseEntity with proper HttpStatus
     */
    @GetMapping
    public ResponseEntity<String> rootEndpoint() {
        return new ResponseEntity<>(StringUtils.EMPTY, HttpStatus.OK);
    }

}
