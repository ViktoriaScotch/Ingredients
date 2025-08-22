package ru.ingredients.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.ingredients.dto.IngredientDTO;
import ru.ingredients.service.DecodingService;

import java.util.List;
import java.util.Map;

@Controller
public class DecodingController {
    @Autowired
    private DecodingService decodingService;

    @PostMapping("/decoding")
    public String decodingPost(@RequestParam String text, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("text", text);
        return "redirect:/decoding";
    }

    @GetMapping(value = "/decoding")
    public String decoding(@ModelAttribute("text") String text, Model model) {
        List<IngredientDTO> ingredients = decodingService.findIng(text);
        Map<String, List<IngredientDTO>> ingByFunc = decodingService.groupByFunc(ingredients);
        Map<String, List<IngredientDTO>> ingByCat = decodingService.groupByCat(ingredients);

        model.addAttribute("text", text);
        model.addAttribute("ingByCat", ingByCat);
        model.addAttribute("ingByFunc", ingByFunc);
        model.addAttribute("ingredients", ingredients);
        return "decoding/decoding";
    }
}
