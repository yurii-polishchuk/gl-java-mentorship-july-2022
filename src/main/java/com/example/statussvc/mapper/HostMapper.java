package com.example.statussvc.mapper;

import com.example.statussvc.domain.Host;
import com.example.statussvc.wire.request.HostCreateRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public abstract class HostMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "connectionTime", ignore = true)
    @Mapping(target = "lastCheck", ignore = true)
    @Mapping(target = "status", ignore = true)
    public abstract Host hostCreateRequestToHost(HostCreateRequest hostCreateRequest);
}
