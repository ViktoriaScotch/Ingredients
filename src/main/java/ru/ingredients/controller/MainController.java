package ru.ingredients.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "main/home";
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        model.addAttribute("isAuthorized", request.isUserInRole("ROLE_ADMIN"));
        return "main/login";
    }
}
