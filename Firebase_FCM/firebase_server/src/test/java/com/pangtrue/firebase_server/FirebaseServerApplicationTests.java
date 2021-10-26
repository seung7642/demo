package com.pangtrue.firebase_server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class FirebaseServerApplicationTests {

    @Autowired
    private FirebaseCloudMessageService service;

    @Test
    void contextLoads() throws IOException {
        String token = "";
        service.addToken(token);
        service.broadCastMessage("제목~~", "파이팅~~~");
    }
}
