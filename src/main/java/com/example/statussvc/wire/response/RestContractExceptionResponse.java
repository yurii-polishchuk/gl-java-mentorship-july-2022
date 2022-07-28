package com.example.statussvc.wire.response;

import com.example.statussvc.wire.Response;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RestContractExceptionResponse(
        LocalDateTime timestamp,
        String error,
        String message,
        String tracingId
) implements Response {

}
