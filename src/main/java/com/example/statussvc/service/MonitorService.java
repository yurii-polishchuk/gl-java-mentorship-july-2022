package com.example.statussvc.service;

import com.example.statussvc.domain.Host;
import com.example.statussvc.domain.type.Status;
import com.example.statussvc.repository.HostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Generic Monitor Service
 */
@Log4j2
@Service
@EnableScheduling
@RequiredArgsConstructor
public class MonitorService {

    private final HostsRepository hostsRepository;
    private final HttpClient httpClient;

    /**
     * Checking host availability
     */
    @Scheduled(cron = "${application.scheduler.host.availability.cron}")
    public void checkHostsAvailability() {
        Iterable<Host> hosts = hostsRepository.findAll();


        hosts.forEach((host) -> {
            try {
                HttpRequest request = HttpRequest
                        .newBuilder()
                        .GET()
                        .uri(URI.create(host.getUrl()))
                        .build();

                LocalDateTime start = LocalDateTime.now();
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                LocalDateTime finish = LocalDateTime.now();
                host.setStatus(Status.ACTIVE);
                host.setConnectionTime(Duration.between(start, finish));
            } catch (Exception e) {
                host.setStatus(Status.INACTIVE);
            }
            host.setLastCheck(LocalDateTime.now());
            hostsRepository.save(host);
        });
    }

}
