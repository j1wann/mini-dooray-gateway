package com.nhnacademy.minidooraygateway.service;

import com.nhnacademy.minidooraygateway.dto.LoginRequestDto;
import com.nhnacademy.minidooraygateway.dto.RegisterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final RestTemplate restTemplate;

    @Value("${api.account.url}")
    private String apiUrl;

    public String authenticate(LoginRequestDto loginRequest) {
        String loginUrl = apiUrl + "/api/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequestDto> requestEntity = new HttpEntity<>(loginRequest, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return null;
        }

        return null;
    }

    public void logout(String uuid) {
        String logoutUrl = apiUrl + "/api/logout";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", uuid);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            restTemplate.postForEntity(logoutUrl, requestEntity, Void.class);
            log.info("Successfully requested logout from Account API for uuid: {}", uuid);
        } catch (Exception e) {
            log.error("Failed to logout from Account API. Error: {}", e.getMessage());
        }
    }
    public boolean registerUser(RegisterDto registerDTO) {
        String registerUrl = apiUrl + "/api/register";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RegisterDto> requestEntity = new HttpEntity<>(registerDTO, headers);

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(registerUrl, requestEntity, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException e) {
            log.error("Registration failed: Status code {}, Body {}", e.getStatusCode(), e.getResponseBodyAsString());
            return false;
        } catch (Exception e) {
            log.error("An unexpected error occurred during registration: {}", e.getMessage());
            return false;
        }
    }
}