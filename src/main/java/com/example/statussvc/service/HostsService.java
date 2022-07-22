package com.example.statussvc.service;

import com.example.statussvc.domain.Host;
import com.example.statussvc.domain.Status;
import com.example.statussvc.mapper.HostMapper;
import com.example.statussvc.repository.HostsRepository;
import com.example.statussvc.wire.request.HostCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Date;

/**
 * CRUD operations for Hosts Management.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class HostsService {
    private final HostsRepository hostsRepository;
    private final HostMapper hostMapper;

    public Long create(HostCreateRequest hostCreateRequest) {
        Host mappedHost = hostMapper.hostCreateRequestToHost(hostCreateRequest);
        mappedHost.setConnectionTime(0);
        mappedHost.setLastCheck(new Date());
        mappedHost.setStatus(Status.INACTIVE);
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
