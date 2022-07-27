package com.example.statussvc.wire.response;

import com.example.statussvc.domain.Status;
import com.example.statussvc.wire.Response;

import java.time.Duration;
import java.time.LocalDateTime;

public record HostRetrieveAllResponse(
        Long id,
        String title,
        String description,
        String url,
        Duration connectionTime,
        LocalDateTime lastCheck,
        Status status
) implements Response{
}
