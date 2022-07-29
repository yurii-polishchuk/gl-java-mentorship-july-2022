package com.example.statussvc.controller;

import brave.Tracer;
import com.example.statussvc.service.HostsService;
import com.example.statussvc.wire.request.CreateHostRequest;
import com.example.statussvc.wire.response.RetrieveHostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

import static com.example.statussvc.Constants.API_V1;

/**
 * Entry point for Hosts endpoint APIs.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1)
public class HostsController {

    private final HostsService hostsService;
    private static final String HOSTS_ENDPOINT = "/hosts";
    private static final String HOST_ENDPOINT = HOSTS_ENDPOINT + "/{id}";

    private final Tracer tracer;

    public Object create() {
        return null;
    }

    public Object replace() {
        return null;
    }

    public Object modify() {
        return null;
    }

    @GetMapping("/host/{id}")
    public ResponseEntity<RetrieveHostResponse> retrieve(@PathVariable Long id) throws Exception {
        return hostsService.retrieve(id);
    }

    public Object retrieveAll() {
        return null;
    }

    public Object remove() {
        return null;
    }

    public Object removeAll() {
        return null;
    }

    /**
     * Returns the hex representation of the span's trace ID.
     *
     * @return trace ID in {@link String} representation
     */
    private String getTraceId() {
        return tracer.currentSpan().context().traceIdString();
    }

}
