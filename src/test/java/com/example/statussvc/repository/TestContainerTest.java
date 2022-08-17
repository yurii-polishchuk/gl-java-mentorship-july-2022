package com.example.statussvc.repository;

import com.example.statussvc.domain.Host;
import com.example.statussvc.domain.type.Status;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestContainerTest {

    @ClassRule
    public static SingletonTestContainer container = SingletonTestContainer.getInstance();
    @Autowired
    private HostsRepository hostsRepository;

    @BeforeEach
    void setup() {
        hostsRepository.deleteAll();
    }

    @Test
    @DisplayName("""
            GIVEN post one valid host
            WHEN retrieve this host by findById method
            THEN verify result
            """)
    public void postValidHostAndReturnThisHostByFindById() {
        //GIVEN
        hostsRepository.save(new Host(10L, "Google", "searching portal",
                "https://google.com", Duration.ZERO, LocalDateTime.parse("2022-08-02T10:15:30"),
                Status.UNKNOWN));
        //WHEN
        List<Host> resultList = (List<Host>) hostsRepository.findAll();
        Optional<Host> result = hostsRepository.findById(resultList.get(0).getId());
        //THEN
        assertThat(result.get().getTitle().equals("Google"));
    }

    @Test
    @DisplayName("""
            GIVEN post 2 valid hosts
            WHEN retrieve this hosts by retrieveAll method
            THEN verify result
            """)
    public void post2ValidHostsAndReturnThisHostSize() {
        //GIVEN
        hostsRepository.save(new Host(10L, "Google", "searching portal",
                "https://google.com", Duration.ZERO, LocalDateTime.parse("2022-08-02T10:15:30"),
                Status.UNKNOWN));
        hostsRepository.save(new Host(20L, "Apple", "Apple portal",
                "https://apple.com", Duration.ZERO, LocalDateTime.parse("2022-07-02T10:12:30"),
                Status.UNKNOWN));
        //WHEN
        List<Host> resultList = (List<Host>) hostsRepository.findAll();
        //THEN
        assertThat(resultList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("""
            GIVEN negative scenario: post valid host
            WHEN retrieve this hosts by findById method method with other id
            THEN verify the output
            """)
    public void postValidHostAndTryToRetrieveHostWithInvalidId() {
        //GIVEN
        hostsRepository.save(new Host(10L, "Google", "searching portal",
                "https://google.com", Duration.ZERO, LocalDateTime.parse("2022-08-02T10:15:30"),
                Status.UNKNOWN));
        //THEN
        Optional<Host> result = hostsRepository.findById(1L);
        //THEN
        assertThat(!result.isPresent());
    }

    @Test
    @DisplayName("""
            GIVEN valid host
            WHEN update host (description field)
            THEN verify the output
            """)
    public void updateHostBySaveMethod() {
        //GIVEN
        hostsRepository.save(new Host(10L, "Google", "searching portal",
                "https://google.com", Duration.ZERO, LocalDateTime.parse("2022-08-02T10:15:30"),
                Status.UNKNOWN));
        //WHEN
        Optional<Host> result = hostsRepository.findById(10L);
        hostsRepository.save(new Host(10L, "Google", "searching engine",
                "https://google.com", Duration.ZERO, LocalDateTime.parse("2022-08-02T10:15:30"),
                Status.UNKNOWN));
        Optional<Host> updatedResult = hostsRepository.findById(10L);
        //THEN
        result.ifPresent(host -> assertThat(host.getDescription().equals("searching portal")));
        updatedResult.ifPresent(host -> assertThat(host.getDescription().equals("searching engine")));
    }

    @Test
    @DisplayName("""
            GIVEN post 2 valid hosts
            WHEN delete hosts by deleteAll method
            THEN verify result
            """)
    public void post2ValidHostsAndDeleteThisHosts() {
        //GIVEN
        hostsRepository.save(new Host(10L, "Google", "searching portal",
                "https://google.com", Duration.ZERO, LocalDateTime.parse("2022-08-02T10:15:30"),
                Status.UNKNOWN));
        hostsRepository.save(new Host(20L, "Apple", "Apple portal",
                "https://apple.com", Duration.ZERO, LocalDateTime.parse("2022-07-02T10:12:30"),
                Status.UNKNOWN));
        //WHEN
        List<Host> resultList = (List<Host>) hostsRepository.findAll();
        hostsRepository.deleteAll();
        List<Host> resultListAfterDelete = (List<Host>) hostsRepository.findAll();
        //THEN
        assertThat(resultList.size()).isEqualTo(2);
        assertThat(resultListAfterDelete.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("""
            GIVEN post valid host
            WHEN delete host by deleteById method
            THEN verify result
            """)
    public void post2ValidHostsAndDeleteByIdOneHost() {
        //GIVEN
        hostsRepository.save(new Host(10L, "Google", "searching portal",
                "https://google.com", Duration.ZERO, LocalDateTime.parse("2022-08-02T10:15:30"),
                Status.UNKNOWN));
        //WHEN
        List<Host> result = (List<Host>) hostsRepository.findAll();
        hostsRepository.deleteById(result.get(0).getId());
        List<Host> resultListAfterDelete = (List<Host>) hostsRepository.findAll();
        //THEN
        assertThat(resultListAfterDelete.size()).isEqualTo(0);
    }

}
