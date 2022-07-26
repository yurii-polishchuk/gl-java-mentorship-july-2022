package com.example.statussvc.service;

import com.example.statussvc.repository.HostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import com.example.statussvc.controller.handler.HttpClientErrorNotFoundException;

/**
 * CRUD operations for Hosts Management.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class HostsService {

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
        try {
            return hostsRepository.findById(id)
                    .orElseThrow(() -> new HttpClientErrorNotFoundException("Host not found"));
        } catch (HttpClientErrorNotFoundException e) {
            throw new RuntimeException(e);
        }
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
