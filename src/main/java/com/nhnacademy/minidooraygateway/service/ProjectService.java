package com.nhnacademy.minidooraygateway.service;

import com.nhnacademy.minidooraygateway.domain.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference; // 1. ParameterizedTypeReference 임포트
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections; // Collections 임포트
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
        // GET 요청이므로 Content-Type 헤더는 필수가 아닙니다. Accept 헤더가 더 적절할 수 있습니다.
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", userUuid); // set 혹은 add 사용
        headers.setAccept(List.of(MediaType.APPLICATION_JSON)); // 서버로부터 JSON 응답을 기대한다고 명시

        // 2. GET 요청이므로 body가 없는 HttpEntity 생성
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            // 3. exchange 메소드 사용
            ResponseEntity<List<Project>> response = restTemplate.exchange(
                    url, // URL 수정 (아래 설명 참조)
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    } // 4. 응답 타입을 명확히 지정
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            log.error("Failed to fetch projects: {}", e.getMessage());
        }

        return Collections.emptyList(); // 에러 발생 또는 실패 시 null 대신 빈 리스트 반환
    }
}