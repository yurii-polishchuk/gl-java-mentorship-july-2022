package com.example.statussvc.controller;

import brave.Tracer;
import com.example.statussvc.service.HostsService;
import com.example.statussvc.wire.request.CreateHostRequest;
import com.example.statussvc.wire.request.ReplaceHostRequest;
import com.example.statussvc.wire.response.RetrieveAllHostsResponse;
import com.example.statussvc.wire.response.RetrieveHostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

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
    public static final int DEFAULT_PAGE_SIZE = 3;

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

    /**
     * PUT to replace One Host entry.
     *
     * @param id                 {@link Long} unique identifier
     * @param replaceHostRequest {@link ReplaceHostRequest} with body
     * @return {@link ResponseEntity} of {@link Void}
     */
    @PutMapping(path = HOST_ENDPOINT)
    public ResponseEntity<Void> replace(
            @PathVariable Long id,
            @Valid @RequestBody ReplaceHostRequest replaceHostRequest
    ) {
        hostsService.replace(id, replaceHostRequest);
        return ResponseEntity.noContent().build();
    }

    public Object modify() {
        return null;
    }

    /**
     * GET to retrieve One Host entry.
     *
     * @param id {@link Long} unique identifier
     * @return {@link ResponseEntity} of {@link RetrieveHostResponse} objects
     */
    @GetMapping(path = HOST_ENDPOINT)
    public ResponseEntity<RetrieveHostResponse> retrieve(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(hostsService.retrieve(id));
    }

    /**
     * GET to retrieve All Host entries.
     *
     * @param pageable {@link Pageable}
     * @return {@link ResponseEntity} with {@link Page} of {@link RetrieveAllHostsResponse} objects
     */
    @GetMapping(path = HOSTS_ENDPOINT)
    public ResponseEntity<Page<RetrieveAllHostsResponse>> retrieveAll(
            @PageableDefault(size = DEFAULT_PAGE_SIZE)
            @SortDefault.SortDefaults(
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(hostsService.retrieveAll(pageable));
    }

    /**
     * DELETE to delete One Host entry.
     *
     * @param id {@link Long} unique identifier
     * @return {@link ResponseEntity} without body
     */
    @DeleteMapping(path = HOST_ENDPOINT)
    public ResponseEntity delete(@PathVariable @Positive Long id) {
        hostsService.remove(id);
        return ResponseEntity.ok().build();

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
