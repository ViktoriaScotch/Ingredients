package ru.ingredients.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ingredients.models.Ingredient;
import ru.ingredients.services.DecodingService;

import java.util.List;
import java.util.Map;

@Controller
public class DecodingController {
    @Autowired
    private DecodingService decodingService;

    @RequestMapping(value = "/decoding", method = {RequestMethod.GET, RequestMethod.POST})
    public String decoding(@RequestParam(required = false) String text, Model model) {
        List<Ingredient> ingredients = decodingService.findIng(text);
        Map<String, List<Ingredient>> ingByFunc = decodingService.groupByFunc(ingredients);
        Map<String, List<Ingredient>> ingByCat = decodingService.groupByCat(ingredients);

        model.addAttribute("text", text);
        model.addAttribute("ingByCat", ingByCat);
        model.addAttribute("ingByFunc", ingByFunc);
        model.addAttribute("ingredients", ingredients);
        return "decoding/decoding";
    }
}
