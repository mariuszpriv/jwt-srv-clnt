package org.example.demojwtclient.service;

import lombok.extern.slf4j.Slf4j;
import org.example.demojwtclient.conf.TokenManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

@Service
@Slf4j
public class AuthService {

    @Value("${auth.url}")
    private String authUrl;

    @Value("${great.url}")
    private String greatUrl;

    private final RestTemplate restTemplate;

    private final TokenManager tokenManager;

    public AuthService(RestTemplate restTemplate, TokenManager tokenManager) {
        this.restTemplate = restTemplate;
        this.tokenManager = tokenManager;
    }

    @Scheduled(fixedRate = 60000)
    public void authenticateAndCallService() {
//        String token = retrieveToken();
        System.out.println("");
        String token = tokenManager.getToken();
        if (token != null) {
            callGreatService(token);
        }
    }

    @Scheduled(fixedRate = 5000)
    public void printTimer() {
        System.out.print(".");
    }

//    private String retrieveToken() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//        headers.setBasicAuth("c3RhdGljVXNlcjpkdW1teVBhc3N3b3Jk");
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        try {
//            ResponseEntity<String> response = restTemplate.exchange(authUrl, HttpMethod.POST, entity, String.class);
//            return response.getBody();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    private void callGreatService(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(greatUrl, HttpMethod.GET, entity, String.class);
            System.out.println("Great Service Response: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
