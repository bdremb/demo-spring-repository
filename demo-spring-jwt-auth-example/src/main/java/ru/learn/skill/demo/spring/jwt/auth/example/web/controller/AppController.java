package ru.learn.skill.demo.spring.jwt.auth.example.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/app")
public class AppController {

    @GetMapping("/all")
    public String allAccess() {
        return "Public response data";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin response data";
    }

    @GetMapping("/manager")
    @PreAuthorize("hasRole('MANAGER')")
    public String moderatorAccess() {
        return "Manager response data";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('MANAGER') or hasRole('USER') or hasRole('ADMIN')")
    public String userAccess() {
        return "User response data";
    }

}
