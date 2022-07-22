package com.example.statussvc.wire.request;

import com.example.statussvc.wire.Request;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Builder
public record HostCreateRequest (
    @NotBlank(message = "Empty title")
    String title,
    String description,
    @NotBlank(message = "Empty url")
    @URL
    String url
    ) implements Request {
}
