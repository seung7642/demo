package com.pangtrue.firebase_server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin("*")
public class TokenController {
	
	private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    private FirebaseCloudMessageService service;

    // Android Device로부터 토큰을 받아서 저장해둔다.
    @PostMapping("/token")
    public String registToken(String token) {
    	logger.info("registToken: token:{}", token);
        service.addToken(token);
        return token;
    }
    
    // 등록되어 있는 모든 사람들에게 알림을 보낸다.
    @PostMapping("/broadcast")
    public Integer broadCast(String title, String body) throws IOException {
    	logger.info("BroadCast: title:{}, body:{}", title, body);
    	return service.broadCastMessage(title, body);
    }

    @PostMapping("/sendMessageTo")
    public void sendMessageTo(String token, String title, String body) throws IOException {
    	logger.info("sendMessageTo: token:{}, title:{}, body:{}", token, title, body);
        service.sendMessageTo(token, title, body);
    }
}

