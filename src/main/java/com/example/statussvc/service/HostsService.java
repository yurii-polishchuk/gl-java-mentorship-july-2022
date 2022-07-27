package com.example.statussvc.service;

import com.example.statussvc.domain.type.Host;
import com.example.statussvc.repository.HostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CRUD operations for Hosts Management.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class HostsService {

    @Autowired
    private final HostsRepository hostsRepository;

    public Object create() {
        return null;
    }

    public Object replace() {
        return null;
    }

    public Object modify() {
        return null;
    }

    public Host retrieve(Long id) {
        return hostsRepository.findById(id).orElseThrow();
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
