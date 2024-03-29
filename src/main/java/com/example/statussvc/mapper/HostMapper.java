package com.example.statussvc.mapper;

import com.example.statussvc.domain.Host;
import com.example.statussvc.wire.request.CreateHostRequest;
import com.example.statussvc.wire.request.ModifyHostRequest;
import com.example.statussvc.wire.request.ReplaceHostRequest;
import com.example.statussvc.wire.response.RetrieveAllHostsResponse;
import com.example.statussvc.wire.response.RetrieveHostResponse;
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

    public abstract RetrieveHostResponse toRetrieveHostResponse(Host host);

    @Mapping(target = "connectionTime", expression = "java(java.time.Duration.ZERO)")
    @Mapping(target = "lastCheck", expression = "java(null)")
    @Mapping(target = "status", constant = "UNKNOWN")
    public abstract Host toHost(Long id, ReplaceHostRequest replaceHostRequest);

    @Mapping(target = "connectionTime", expression = "java(java.time.Duration.ZERO)")
    @Mapping(target = "lastCheck", expression = "java(null)")
    @Mapping(target = "status", constant = "UNKNOWN")
    @Mapping(target = "title", source = "modifyHostRequest.title", defaultExpression = "java(host.getTitle())")
    @Mapping(target = "description", source = "modifyHostRequest.description", defaultExpression = "java(host.getDescription())")
    @Mapping(target = "url", source = "modifyHostRequest.url", defaultExpression = "java(host.getUrl())")
    public abstract Host toHost(Host host, ModifyHostRequest modifyHostRequest);

}

