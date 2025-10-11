package com.nhnacademy.minidooraygateway.controller;

import com.nhnacademy.minidooraygateway.domain.Project;
import com.nhnacademy.minidooraygateway.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("")
    public String getProjects(Model model, HttpSession httpSession) {
        String uuid = (String) httpSession.getAttribute("uuid");
        String userName = (String) httpSession.getAttribute("userName");

         List<Project> projects = projectService.findProjectsByUser(uuid);
         model.addAttribute("userName", userName);
         model.addAttribute("projects", projects);

        return "projects"; // projects 뷰 반환
    }
}
