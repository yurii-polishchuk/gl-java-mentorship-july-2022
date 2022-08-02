package com.example.statussvc.repository;

import com.example.statussvc.domain.Host;
import org.junit.ClassRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestContainerTest {

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
        //create table like "CREATE TABLE HOST..."?
        hostsRepository.save(new Host(10L, "google", "searching portal", "https://google.com",
                null,null,null));
        //WHEN
        Optional<Host> result = hostsRepository.findById(10L);
        //THEN
        if (result.isPresent()){
            assertThat(result.get().getTitle().equals("google"));
        }
    }
    //TODO delete all date after each test

}
