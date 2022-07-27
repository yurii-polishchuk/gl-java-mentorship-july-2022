package com.example.statussvc.controller.handler;

import brave.Tracer;
import com.example.statussvc.Constants;
import com.example.statussvc.wire.response.RestContractExceptionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Global Rest Controllers exception handler (WebMVC).
 */
@Slf4j
@RequiredArgsConstructor
@ControllerAdvice(basePackages = {Constants.ROOT_PACKAGE + ".controller"})
public class GlobalExceptionHandler {

    private static final String REASON_DELIMITER = ": ";
    private static final String MULTIPLE_ERRORS_DELIMITER = ", ";

    private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_MAPPING = Map.of(
            HttpMediaTypeNotSupportedException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE
    );

    private final Tracer tracer;

    /**
     * Handler for HTTP status code exceptions.
     *
     * @param exception {@link HttpStatusCodeException} thrown by service
     * @return {@link ResponseEntity} with status, header and body
     */
    @ExceptionHandler
    @SuppressWarnings("unused")
    public ResponseEntity<RestContractExceptionResponse> handleBindException(HttpStatusCodeException exception) {
        return map(exception.getStatusCode(), exception.getStatusText(), exception);
    }

    /**
     * Binding results for Request Body exception handler.
     *
     * @param exception {@link MethodArgumentNotValidException} to catch and extract error messages from fields
     * @return {@link ResponseEntity} with status {@link HttpStatus#BAD_REQUEST},
     */
    @ExceptionHandler
    @SuppressWarnings("unused")
    public ResponseEntity<RestContractExceptionResponse> handleBindException(MethodArgumentNotValidException exception) {
        return map(
                HttpStatus.BAD_REQUEST,
                exception.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(fieldError ->
                                fieldError.getField() +
                                        Optional.ofNullable(fieldError.getDefaultMessage())
                                                .map(REASON_DELIMITER::concat)
                                                .orElse(StringUtils.EMPTY)
                        )
                        .distinct()
                        .sorted(),
                exception
        );
    }

    /**
     * Not readable request data exception handler.
     *
     * @param exception {@link HttpMessageNotReadableException} to catch and extract meaningful response
     * @return {@link ResponseEntity} with status {@link HttpStatus#BAD_REQUEST},
     */
    @ExceptionHandler
    @SuppressWarnings("unused")
    public ResponseEntity<RestContractExceptionResponse> handleBindException(HttpMessageNotReadableException exception) {
        return map(HttpStatus.BAD_REQUEST, "Invalid request body received", exception);
    }

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
     * @param messages   stream of messages
     * @param throwable  cause
     * @return {@link ResponseEntity} with status, header and body
     */
    private ResponseEntity<RestContractExceptionResponse> map(HttpStatus httpStatus,
                                                              Stream<String> messages,
                                                              Throwable throwable) {
        return map(httpStatus, messages.collect(Collectors.joining(MULTIPLE_ERRORS_DELIMITER)), throwable);
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
