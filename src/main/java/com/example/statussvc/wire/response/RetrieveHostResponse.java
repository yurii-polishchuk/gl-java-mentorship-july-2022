package com.example.statussvc.wire.response;

import com.example.statussvc.wire.Response;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public record RetrieveHostResponse(@JsonProperty String title,
                                   @JsonProperty String description,
                                   @JsonProperty String url,
                                   @JsonProperty int ConnectionTime,
                                   @JsonProperty Date lastCheck,
                                   @JsonProperty Status status) implements Response {
}
