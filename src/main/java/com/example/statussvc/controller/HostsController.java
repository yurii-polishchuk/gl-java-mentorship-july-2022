package com.example.statussvc.controller;

import brave.Tracer;
import com.example.statussvc.domain.Host;
import com.example.statussvc.repository.HostsRepository;
import com.example.statussvc.wire.response.RestContractExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.MetaData;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static com.example.statussvc.Constants.*;

/**
 * Entry point for Hosts endpoint APIs.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1)
public class HostsController {
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

    @GetMapping("/hosts/{id}")
    public ResponseEntity<URL> retrieve(@PathVariable("id") Long id) throws URISyntaxException {
        URI uri = new URI(API_V1 + "/hosts" + URL_SEPARATOR + id);
        return ResponseEntity.created(uri).build();
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
