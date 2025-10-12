package com.nhnacademy.minidooraygateway.service;

import com.nhnacademy.minidooraygateway.domain.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {
    @Value("${api.project.url}")
    private String apiUrl;
    private final RestTemplate restTemplate;

    public List<Project> findProjectsByUser(String userUuid) {
        String url = apiUrl + "/api/project";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", userUuid);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<Project>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    }
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            log.error("Failed to fetch projects: {}", e.getMessage());
        }

        return Collections.emptyList();
    }
}