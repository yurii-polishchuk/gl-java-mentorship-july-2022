package com.example.statussvc.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public HttpClient httpClient() {
        return HttpClient
                .newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .connectTimeout(Duration.of(30L, SECONDS))
                .build();
    }
}

