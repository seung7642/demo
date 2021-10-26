package com.pangtrue.firebase_server;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class FirebaseCloudMessageService {
	
	private static final Logger logger = LoggerFactory.getLogger(FirebaseCloudMessageService.class);
    public final ObjectMapper objectMapper;
    private List<String> clientTokens = new ArrayList<>();

    // Firebase Console -> [프로젝트 설정] -> 일반 탭에서 프로젝트 ID 확인 가능하다.
    // ~/projects/{Firebase 프로젝트ID}/messages:send 
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/{프로젝트ID}/messages:send";
     
    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        // Google API를 사용하기 위해 OAuth2를 이용해 인증한 대상을 나타내는 객체
        GoogleCredentials googleCredentials = GoogleCredentials
                // 서버로부터 받은 service key 파일 활용
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                // 인증하는 서버에서 필요로 하는 권한 지정
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
        
        googleCredentials.refreshIfExpired();
        String token = googleCredentials.getAccessToken().getTokenValue();
        return token;
    }
    
    // targetToken에 해당하는 디바이스로 FCM에 푸시하여 해당 디바이스에 알림을 전송한다.
    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);
        logger.info("message: {}", message);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                // 전송 토큰 추가
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(Objects.requireNonNull(response.body()).string());
    }

    // FCM 알림 메시지를 생성한다.
    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        FcmMessage.Notification noti = new FcmMessage.Notification(title, body, null);
        FcmMessage.Message message = new FcmMessage.Message(noti, targetToken);
        FcmMessage fcmMessage = new FcmMessage(false, message);
        return objectMapper.writeValueAsString(fcmMessage);
    }

    public FirebaseCloudMessageService(ObjectMapper objectMapper){
    	this.objectMapper = objectMapper;
    }

    // 클라이언트 토큰 관리
    public void addToken(String token) {
        clientTokens.add(token);
    }
    
    // 등록된 모든 토큰을 이용해서 broadcasting
    public int broadCastMessage(String title, String body) throws IOException {
       for(String token: clientTokens) {
    	   logger.debug("Broadcast message: {}, {}, {}",token, title, body);
           sendMessageTo(token, title, body);
       }
       return clientTokens.size();
    }
}
