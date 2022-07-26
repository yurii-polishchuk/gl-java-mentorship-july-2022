package com.example.statussvc.mapper;

import com.example.statussvc.domain.Host;
import com.example.statussvc.domain.Status;
import com.example.statussvc.wire.request.HostCreateRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.Duration;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public abstract class HostMapper {


    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "connectionTime", ignore = true, expression = "java( java.time.Duration.ZERO )"),
            @Mapping(target = "lastCheck", ignore = true, expression = "java( java.time.LocalDateTime.now() )"),
            @Mapping(target = "status", ignore = true, expression = "java( com.example.statussvc.domain.Status.INACTIVE)")
    })
    public abstract Host hostCreateRequestToHost(HostCreateRequest hostCreateRequest);
}
