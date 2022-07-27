package com.example.statussvc.mapper;

import com.example.statussvc.domain.Host;
<<<<<<< HEAD
import com.example.statussvc.wire.request.CreateHostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
=======
import com.example.statussvc.wire.request.HostCreateRequest;
import com.example.statussvc.wire.response.HostRetrieveAllResponse;
import org.mapstruct.*;
>>>>>>> 469af82 (First Commit!)

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

<<<<<<< HEAD
=======
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "connectionTime", ignore = true),
            @Mapping(target = "lastCheck", ignore = true),
            @Mapping(target = "status", expression = "java( com.example.statussvc.domain.Status.UNKNOWN)")
    })
    public abstract Host hostCreateRequestToHost(HostCreateRequest hostCreateRequest);

    public abstract HostRetrieveAllResponse hostToHostRetrieveAllResponse(Host host);
>>>>>>> 469af82 (First Commit!)
}
