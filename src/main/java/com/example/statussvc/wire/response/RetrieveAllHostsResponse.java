package com.example.statussvc.wire.response;

import com.example.statussvc.domain.type.Status;
import com.example.statussvc.wire.Response;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder
@Jacksonized
public record RetrieveAllHostsResponse(
        Long id,
        String title,
        String description,
        String url,
        Duration connectionTime,
        Status status,
        LocalDateTime lastCheck
) implements Response{
}
