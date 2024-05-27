package org.example.demojwtclient.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Component
public class TokenManager {

    @Value("${auth.url}")
    private String authUrl;

//    @Value("${auth.username}")
//    private String username;
//
//    @Value("${auth.password}")
//    private String password;

    private String token;
    private long tokenExpirationTime;

    private final RestTemplate restTemplate;

    public TokenManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public synchronized String getToken() {
        if (token == null || isTokenExpired()) {
            log.info("refresh token");
            refreshToken();
        }
        return token;
    }

    private boolean isTokenExpired() {
        return System.currentTimeMillis() >= tokenExpirationTime;
    }

    private void refreshToken() {
//        Map<String, String> requestBody = new HashMap<>();
//        requestBody.put("username", username);
//        requestBody.put("password", password);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth("c3RhdGljVXNlcjpkdW1teVBhc3N3b3Jk");

        HttpEntity<String> entity = new HttpEntity<>(headers);

//        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(authUrl, HttpMethod.POST, entity, String.class);

        String responseBody = response.getBody();
        if (responseBody != null) {
            token = responseBody;
            tokenExpirationTime = System.currentTimeMillis() + 175000; // assuming token is valid for 1 hour
        }
    }
}
