package com.example.statussvc.controller;

import brave.Tracer;
import com.example.statussvc.controller.handler.exceptions.ConflictException;
import com.example.statussvc.service.HostsService;
import com.example.statussvc.wire.request.HostCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static com.example.statussvc.Constants.*;

/**
 * Entry point for Hosts endpoint APIs.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + URL_SEPARATOR + HOSTS)
public class HostsController {

    private final Tracer tracer;
    private final HostsService hostsService;

    @PostMapping
    public ResponseEntity<URI> create(@Valid @RequestBody HostCreateDto hostCreateDto) throws Exception {
        Long id = hostsService.save(hostCreateDto);
        URI uri = new URI(API_V1 + URL_SEPARATOR + HOSTS + URL_SEPARATOR + id);
        return ResponseEntity.created(uri).build();
    }

    public Object replace() {
        return null;
    }

    public Object modify() {
        return null;
    }

    public Object retrieve() {
        return null;
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
