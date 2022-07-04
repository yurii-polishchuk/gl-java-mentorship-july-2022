package com.example.statussvc.repository;

import com.example.statussvc.domain.Host;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * CRUD DB operations for Hosts Management.
 */
@Repository
public interface HostsRepository extends PagingAndSortingRepository<Host, Long> {

}

