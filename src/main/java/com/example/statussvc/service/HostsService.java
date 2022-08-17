package com.example.statussvc.service;

import com.example.statussvc.mapper.HostMapper;
import com.example.statussvc.repository.HostsRepository;
import com.example.statussvc.wire.request.CreateHostRequest;
import com.example.statussvc.wire.request.ModifyHostRequest;
import com.example.statussvc.wire.request.ReplaceHostRequest;
import com.example.statussvc.wire.response.RetrieveAllHostsResponse;
import com.example.statussvc.wire.response.RetrieveHostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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
        return hostsRepository.save(hostMapper.toHost(createHostRequest)).getId();
    }

    /**
     * Replaces e.g. Updates Host entry.
     *
     * @param id                 - {@link Long} unique id of the Host in storage
     * @param replaceHostRequest - {@link ReplaceHostRequest} replace request object
     */
    public void replace(Long id, ReplaceHostRequest replaceHostRequest) {
        if (!hostsRepository.existsById(id)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        hostsRepository.save(hostMapper.toHost(id, replaceHostRequest));
    }

    /**
     * Modifies e.g. Partially Updates Host entry.
     *
     * @param id                - {@link Long} unique id of the Host in storage
     * @param modifyHostRequest - {@link ModifyHostRequest} modify request object
     */
    public void modify(Long id, ModifyHostRequest modifyHostRequest) {
        hostsRepository.save(hostMapper.toHost(
                hostsRepository.findById(id)
                        .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND)),
                modifyHostRequest)
        );
    }

    /**
     * Retrieves one Host by unique identifier
     *
     * @param id - {@link Long} unique entry identifier
     * @return {@link RetrieveHostResponse} mapped object
     */
    public RetrieveHostResponse retrieve(Long id) {
        return hostsRepository.findById(id)
                .map(hostMapper::toRetrieveHostResponse)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
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

    /**
     * Remove one Host by unique identifier
     *
     * @param id - {@link Long} unique entry identifier
     */
    public void remove(Long id) {
        hostsRepository.deleteById(id);
    }

    /**
     * Remove all Hosts
     */
    public void removeAll() {
        hostsRepository.deleteAll();
    }

}
