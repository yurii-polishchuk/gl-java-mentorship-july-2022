package com.example.statussvc.wire.response;

import com.example.statussvc.wire.Response;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public record RetrieveHostResponse(String title,
                                   String description,
                                   String url,
                                   int ConnectionTime,
                                   Date lastCheck,
                                   Status status) implements Response {
}
