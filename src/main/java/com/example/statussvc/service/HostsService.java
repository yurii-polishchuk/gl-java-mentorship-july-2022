package com.example.statussvc.service;

import com.example.statussvc.controller.handler.exceptions.ConflictException;
import com.example.statussvc.domain.Host;
import com.example.statussvc.mapper.HostMapper;
import com.example.statussvc.repository.HostsRepository;
import com.example.statussvc.wire.request.HostCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;

/**
 * CRUD operations for Hosts Management.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class HostsService {
    private final HostsRepository hostsRepository;

    public Long save(@Valid HostCreateDto hostCreateDto) {
        Host mappedHost = HostMapper.INSTANCE.HostCreateRequestToHost(hostCreateDto);
        if (mappedHost.getId() != null && hostsRepository.existsById(mappedHost.getId())) {
            throw new ConflictException(String.format("Host with id=%d already exists!", mappedHost.getId()));
        }
        return hostsRepository.save(mappedHost).getId();
    }

    public Object replace() {
        return null;
    }

    public Object modify() {
        return null;
    }

    public Object retrieve() {
        return null;
    }

    public Object retrieveAll() {
        return null;
    }

    public Object remove() {
        return null;
    }

    public Object removeAll() {
        return null;
    }

}
