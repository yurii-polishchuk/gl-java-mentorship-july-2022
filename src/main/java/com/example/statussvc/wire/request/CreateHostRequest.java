package com.example.statussvc.wire.request;

import com.example.statussvc.wire.Request;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Jacksonized
public record CreateHostRequest(
        @NotBlank @Size(min = 1, max = 128) String title,
        @NotBlank @Size(min = 1, max = 1024) String description,
        @NotBlank @Size(min = 1, max = 1024) @URL String url
) implements Request {
}
