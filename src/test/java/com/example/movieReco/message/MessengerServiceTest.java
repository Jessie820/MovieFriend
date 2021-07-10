package com.example.movieReco.message;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MessengerServiceTest {

    @Autowired
    MessengerService messengerService;
    private String message;

    @BeforeEach
    public void setUp() {
        message = "잘 할 수 있어";
    }
    @Test
    public void publish_test_message() {
        messengerService.publishTextMessage(message);
    }

}