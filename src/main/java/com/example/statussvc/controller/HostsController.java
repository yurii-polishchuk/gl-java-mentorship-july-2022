package com.example.statussvc.controller;

import brave.Tracer;
import com.example.statussvc.service.HostsService;
<<<<<<< HEAD
import com.example.statussvc.wire.request.CreateHostRequest;
=======
import com.example.statussvc.wire.request.HostCreateRequest;
import com.example.statussvc.wire.response.HostRetrieveAllResponse;
>>>>>>> 469af82 (First Commit!)
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.example.statussvc.Constants.API_V1;
import static com.example.statussvc.Constants.URL_SEPARATOR;

/**
 * Entry point for Hosts endpoint APIs.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1)
public class HostsController {

    private static final String HOSTS_ENDPOINT = "/hosts";
    private static final String HOST_ENDPOINT = HOSTS_ENDPOINT + "/{id}";

    private final Tracer tracer;
    private final HostsService hostsService;

    /**
     * POST to create Host entry.
     *
     * @param createHostRequest  {@link CreateHostRequest} with body
     * @param httpServletRequest {@link HttpServletRequest} with full request data
     * @return {@link ResponseEntity} with trace and location headers
     */
    @PostMapping(path = HOSTS_ENDPOINT)
    public ResponseEntity<URI> create(
            @Valid @RequestBody CreateHostRequest createHostRequest,
            HttpServletRequest httpServletRequest
    ) {
        return ResponseEntity.created(
                URI.create(httpServletRequest.getRequestURI() + URL_SEPARATOR + hostsService.create(createHostRequest))
        ).build();
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

    @GetMapping
    public ResponseEntity<Map<String, Object>> retrieveAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Pageable paging = PageRequest.of(page, size);
        Page<HostRetrieveAllResponse> pageRetrieveAllHostResponses = hostsService.retrieveAll(paging);
        Map<String, Object> response = new HashMap<>();

        response.put("hosts", pageRetrieveAllHostResponses.getContent());
        response.put("currentPage", pageRetrieveAllHostResponses.getNumber());
        response.put("totalItems", pageRetrieveAllHostResponses.getTotalElements());
        response.put("totalPages", pageRetrieveAllHostResponses.getTotalPages());

        return ResponseEntity.ok(response);
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
