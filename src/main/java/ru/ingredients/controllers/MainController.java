package ru.ingredients.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "Страница про нас");
        return "about";
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("title", "Здесь будет страница с продуктами");
        return "products/products";
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        model.addAttribute("isAuthorized", request.isUserInRole("ROLE_ADMIN"));
        return "login/login";
    }
}
