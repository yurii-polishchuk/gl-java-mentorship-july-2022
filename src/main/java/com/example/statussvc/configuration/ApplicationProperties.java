package com.example.statussvc.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "application")
public record ApplicationProperties(@NotNull Service service) {

    public record Service(@NotEmpty String version) {
    }

}
