package com.example.movieReco;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
//@SpringBootTest
public class logTest {
    Logger log = LoggerFactory.getLogger(logTest.class);

    @Test
    void logTest(){
        log.info("info log");
        log.debug("debug log");
        log.warn("warn log");
    }
}
