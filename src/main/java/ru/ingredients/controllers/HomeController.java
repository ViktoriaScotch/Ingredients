package ru.ingredients.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

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
}
