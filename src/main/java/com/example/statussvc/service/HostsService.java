package com.example.statussvc.service;

import com.example.statussvc.domain.Host;
import com.example.statussvc.mapper.HostMapper;
import com.example.statussvc.repository.HostsRepository;
import com.example.statussvc.wire.request.CreateHostRequest;
import com.example.statussvc.wire.response.RetrieveAllHostsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * CRUD operations for Hosts Management.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class HostsService {

    private final HostsRepository hostsRepository;
    private final HostMapper hostMapper;

    /**
     * Creates new Host in entry.
     *
     * @param createHostRequest - {@link CreateHostRequest} create request object
     * @return {@link Long} unique id of the Host in storage
     */
    public Long create(CreateHostRequest createHostRequest) {
        Host host = hostMapper.toHost(createHostRequest);
        Host save = hostsRepository.save(host);
        return save.getId();
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
    /**
     * Retrieves all Hosts
     *
     * @param paging - {@link Pageable} paging object
     * @return {@link Page} page of filtered and sorted {@link RetrieveAllHostsResponse} objects
     */
    public Page<RetrieveAllHostsResponse> retrieveAll(Pageable paging) {
        return hostsRepository.findAll(paging).map(hostMapper::toRetrieveAllHostsResponse);
    }

    public Object remove() {
        return null;
    }

    public Object removeAll() {
        return null;
    }

}
