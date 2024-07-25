package ru.kata.spring.boot_security.REST.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class LoginController {
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

}
