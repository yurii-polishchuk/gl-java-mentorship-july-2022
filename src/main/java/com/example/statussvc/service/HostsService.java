package com.example.statussvc.service;

import com.example.statussvc.domain.type.Host;
import com.example.statussvc.repository.HostsRepository;
import com.example.statussvc.wire.response.RetrieveHostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/host/{id}")
    public ResponseEntity<RetrieveHostResponse> retrieve(@PathVariable Long id) {
        return hostsRepostory.getById(id);
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
