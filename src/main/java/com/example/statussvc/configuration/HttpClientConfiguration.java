package com.example.statussvc.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * Different Http Clients configuration.
 */
@Configuration
public class HttpClientConfiguration {

    /**
     * Custom bean configuration for a HTTP Client.
     *
     * @return pre-built instance of {@link HttpClient}
     */
    @Bean
    public HttpClient httpClient() {
        return HttpClient
                .newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.of(10L, SECONDS))
                .build();
    }

}

