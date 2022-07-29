package com.example.statussvc.wire.response;

import com.example.statussvc.domain.Host;
import com.example.statussvc.wire.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RetrieveHostResponse implements Response {
    public String getContentAsString(Host host) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(host);
    }
}
