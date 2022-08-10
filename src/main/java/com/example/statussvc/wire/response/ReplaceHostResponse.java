package com.example.statussvc.wire.response;

import com.example.statussvc.wire.Response;
import com.example.statussvc.wire.type.StatusRest;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder
@Jacksonized
public record ReplaceHostResponse(
        Long id,
        String title,
        String description,
        String url,
        Duration connectionTime,
        StatusRest status,
        LocalDateTime lastCheck
) implements Response {
}
