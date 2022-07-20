package com.example.statussvc.mapper;

import com.example.statussvc.domain.Host;
import com.example.statussvc.wire.request.HostCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface HostMapper {

    HostMapper INSTANCE = Mappers.getMapper(HostMapper.class);
    @Mapping(target = "lastCheck",
            expression = "java( new java.util.Date() )"
    )
    Host HostCreateRequestToHost(HostCreateDto hostCreateDto);
    HostCreateDto hostToHostCreateResponse(Host host);
}
