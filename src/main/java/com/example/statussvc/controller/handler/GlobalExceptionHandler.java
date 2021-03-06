package com.example.statussvc.controller.handler;

import brave.Tracer;
import com.example.statussvc.Constants;
import com.example.statussvc.wire.response.RestContractExceptionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

/**
 * Global Rest Controllers exception handler (WebMVC).
 */
@Slf4j
@RequiredArgsConstructor
@ControllerAdvice(basePackages = {Constants.ROOT_PACKAGE + ".controller"})
public class GlobalExceptionHandler {

    private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_MAPPING = Map.of(
            HttpMediaTypeNotSupportedException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE
    );

    private final Tracer tracer;

    /**
     * Global exception Handler.
     *
     * @return {@link ResponseEntity} with status and body
     */
    @ExceptionHandler
    @SuppressWarnings("unused")
    public ResponseEntity<RestContractExceptionResponse> handleThrowable(Throwable exception) {
        return map(EXCEPTION_MAPPING.getOrDefault(exception.getClass(), HttpStatus.INTERNAL_SERVER_ERROR),
                exception.getMessage(),
                exception
        );
    }

    /**
     * Converts specific exceptions to meaningful response.
     *
     * @param httpStatus generic error code
     * @param message    descriptive message of an error
     * @param throwable  cause
     * @return {@link ResponseEntity} with status, header and body
     */
    private ResponseEntity<RestContractExceptionResponse> map(
            HttpStatus httpStatus,
            String message,
            Throwable throwable) {
        var traceId = tracer.currentSpan().context().traceIdString();
        var reasonPhrase = httpStatus.getReasonPhrase();
        log.error("Error: {}, Message: {}, Trace: {}, Cause: {}", reasonPhrase, message, traceId, throwable.toString());
        return ResponseEntity.status(httpStatus)
                .body(RestContractExceptionResponse.builder()
                        .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                        .error(reasonPhrase)
                        .message(message)
                        .tracingId(traceId)
                        .build());
    }

}
