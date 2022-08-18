package com.example.statussvc.service;

import com.example.statussvc.domain.Host;
import com.example.statussvc.domain.type.Status;
import com.example.statussvc.repository.HostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.stream.IntStream;

/**
 * Generic Monitor Service
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class MonitorService {

    private final HostsRepository hostsRepository;
    private final HttpClient httpClient;

    /**
     * Checking hosts availability via HTTP.
     */
    public void checkHostsAvailability() {
        var elementsTotal = (float) hostsRepository.count();
        var elementsPerPage = 50;
        var pages = (int) Math.ceil(elementsTotal / elementsPerPage);
        log.info(pages);

        IntStream.range(0, pages)
                .parallel()
                .forEach(i -> hostsRepository.saveAll(
                                hostsRepository.findAll(PageRequest.of(i, elementsPerPage))
                                        .stream()
                                        .map(this::checkAndStoreConnectionTime)
                                        .toList()
                        )
                );
    }

    /**
     * Generic HEAD request.
     *
     * @param url - {@link String} representation of the URL.
     * @return {@link HttpRequest} request.
     */
    private HttpRequest buildHeadRequest(String url) {
        return HttpRequest
                .newBuilder()
                .method(HttpMethod.HEAD.name(), HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(url))
                .build();
    }

    /**
     * HTTP HEAD Request with connection time setter.
     *
     * @param host - host objects that is used to get URL for request and connection time to set.
     */
    private Host checkAndStoreConnectionTime(Host host) {
        log.info("CHECKING HOST: {}, by URL: {}", host.getTitle(), host.getUrl());
        var startTimeMillis = System.currentTimeMillis();
        var status = Status.INACTIVE;

        try {
            var statusCode = httpClient.send(buildHeadRequest(host.getUrl()), HttpResponse.BodyHandlers.discarding()).statusCode();
            if (statusCode == HttpStatus.OK.value()) {
                status = Status.ACTIVE;
            }
        } catch (Exception e) {
            log.error("HOST ERROR: {}", e.getMessage());
            status = Status.UNKNOWN;
        }

        host.setConnectionTime(Duration.ofMillis(System.currentTimeMillis() - startTimeMillis));
        host.setStatus(status);

        log.info(
                "STORING HOST: {}, URL: {}, STATUS: {}, TIME: {} ms",
                host.getTitle(),
                host.getUrl(),
                host.getStatus(),
                host.getConnectionTime().toMillis()
        );

        return host;
    }

}
