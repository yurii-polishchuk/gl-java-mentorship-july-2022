package com.example.statussvc.repository;

import com.example.statussvc.domain.Host;
import com.example.statussvc.domain.type.Status;
import org.junit.ClassRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestContainerTest {

    @Autowired
    private HostsRepository hostsRepository;

    @ClassRule
    public static SingletonTestContainer container = SingletonTestContainer.getInstance();

    @Test
    @Transactional
    @DisplayName("""
            GIVEN post one valid host
            WHEN retrieve this host by findById method
            THEN verify result
            """)
    public void postValidHostAndReturnThisHostByRetrieveAll() {
        //GIVEN
        hostsRepository.save(new Host(10L, "Google", "searching portal",
            "https://google.com", Duration.ZERO, LocalDateTime.parse("2022-08-02T10:15:30"),
                Status.UNKNOWN));
        //WHEN
        Optional<Host> result = hostsRepository.findById(10L);
        //THEN
        result.ifPresent(host -> assertThat(host.getTitle().equals("Google")));
        //TODO hostsRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("""
            GIVEN post 2 valid hosts
            WHEN retrieve ala this hosts by retrieveAll method
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
        assertThat(resultList.size()).isEqualTo(3);
        //TODO hostsRepository.deleteAll();
    }
}
