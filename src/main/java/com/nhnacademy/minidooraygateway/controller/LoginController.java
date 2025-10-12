package com.nhnacademy.minidooraygateway.controller;

import com.nhnacademy.minidooraygateway.dto.LoginRequestDto;
import com.nhnacademy.minidooraygateway.dto.RegisterDto;
import com.nhnacademy.minidooraygateway.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm() {
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

    @GetMapping("/logout")
    public String doLogout(HttpSession session) {
        String uuid = (String) session.getAttribute("uuid");

        if (uuid != null) {
            loginService.logout(uuid);
        }

        session.invalidate();

        return "redirect:/api/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(RegisterDto registerDTO) {
        log.info("Register attempt for user: {}", registerDTO.getUserName());

        boolean isSuccess = loginService.registerUser(registerDTO);

        if (isSuccess) {
            return "redirect:/api/login?register=success";
        } else {
            return "redirect:/api/register?error=true";
        }
    }
}