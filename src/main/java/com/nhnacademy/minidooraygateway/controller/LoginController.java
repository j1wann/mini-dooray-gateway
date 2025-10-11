package com.nhnacademy.minidooraygateway.controller;

import com.nhnacademy.minidooraygateway.dto.LoginRequestDto;
import com.nhnacademy.minidooraygateway.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm() { // 메서드명 변경 (login -> loginForm)
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(LoginRequestDto loginRequest, HttpSession session) {
        log.info("{}", loginRequest.getUserName());
        log.info("{}", loginRequest.getPassword());

        String uuid = loginService.authenticate(loginRequest);

        if (uuid != null) {
            session.setAttribute("uuid", uuid);
            session.setAttribute("userName", loginRequest.getUserName());

            return "redirect:/api/project";
        } else {
            return "redirect:/api/login?error=true";
        }
    }
}