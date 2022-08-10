package com.example.statussvc.service;

import com.example.statussvc.domain.Host;
import com.example.statussvc.domain.type.Status;
import com.example.statussvc.repository.HostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * Generic Monitor Service
 */
@Log4j2
@Service
@EnableScheduling
@RequiredArgsConstructor
public class MonitorService {

    private final HostsRepository hostsRepository;

    /**
     * Add docs
     */
    @Scheduled(cron = "${application.scheduler.host.availability.cron}")
    public void checkHostsAvailability() {
        log.info("checking...");
        Iterable<Host> hosts = hostsRepository.findAll();
        HttpClient httpClient = HttpClient
                .newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.of(30L, SECONDS))
                .build();

        hosts.forEach((host) -> {
            try {
                HttpRequest request = HttpRequest
                        .newBuilder()
                        .GET()
                        .uri(URI.create(host.getUrl()))
                        .build();

                LocalDateTime start = LocalDateTime.now();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                LocalDateTime finish = LocalDateTime.now();
                if (response.statusCode() == HttpStatus.OK_200) {
                    host.setStatus(Status.ACTIVE);
                    host.setConnectionTime(Duration.between(start, finish));
                } else {
                    host.setStatus(Status.INACTIVE);
                }
                host.setLastCheck(LocalDateTime.now());
                log.info("host: " + host);
                hostsRepository.save(host);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}
