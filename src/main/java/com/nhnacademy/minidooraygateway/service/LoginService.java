package com.nhnacademy.minidooraygateway.service;

import com.nhnacademy.minidooraygateway.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final RestTemplate restTemplate;

    // application.properties 등에서 API 서버의 주소를 주입받아 유연하게 관리합니다.
    @Value("${api.account.url}")
    private String apiUrl;

    public String authenticate(LoginRequestDto loginRequest) {
        // 1. 요청할 API 서버의 전체 URL을 만듭니다.
        String loginUrl = apiUrl + "/api/login";

        // 2. HTTP 요청 헤더를 설정합니다. (JSON 타입으로 보낼 것임을 명시)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. 요청 바디와 헤더를 포함하는 HttpEntity 객체를 생성합니다.
        HttpEntity<LoginRequestDto> requestEntity = new HttpEntity<>(loginRequest, headers);

        try {
            // 4. restTemplate.postForEntity()를 사용해 POST 요청을 보냅니다.
            //    - 파라미터: (요청 URL, 요청 데이터, 응답 타입)
            //    - 응답으로 ResponseEntity<String>을 받습니다. 여기에는 상태 코드, 헤더, 바디가 포함됩니다.
            ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, requestEntity, String.class);

            // 5. 응답 상태 코드가 200 OK 이면, 응답 바디(UUID)를 반환합니다.
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody(); // 성공 시 UUID 문자열 반환
            }
        } catch (Exception e) {
            // API 서버 호출 중 예외 발생 시 (e.g., 401 Unauthorized, 서버 다운 등)
            // 로그를 남기고 null 또는 예외를 던져서 처리할 수 있습니다.
            log.error("Login failed: {}", e.getMessage());
            return null;
        }

        return null; // 그 외의 경우 null 반환
    }
}