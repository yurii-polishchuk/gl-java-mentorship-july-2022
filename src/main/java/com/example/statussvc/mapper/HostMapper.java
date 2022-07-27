package com.example.statussvc.mapper;

import com.example.statussvc.domain.Host;
import com.example.statussvc.wire.request.CreateHostRequest;
import com.example.statussvc.wire.response.RetrieveAllHostsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public abstract class HostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "connectionTime", expression = "java(java.time.Duration.ZERO)")
    @Mapping(target = "lastCheck", ignore = true)
    @Mapping(target = "status", constant = "UNKNOWN")
    public abstract Host toHost(CreateHostRequest createHostRequest);

    public abstract RetrieveAllHostsResponse toRetrieveAllHostsResponse(Host host);
}
