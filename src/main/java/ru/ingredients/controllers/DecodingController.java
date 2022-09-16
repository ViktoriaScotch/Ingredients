package ru.ingredients.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ingredients.models.Function;
import ru.ingredients.models.Ingredient;
import ru.ingredients.repo.FunctionRepository;
import ru.ingredients.repo.IngredientRepository;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DecodingController {
    private String text;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private FunctionRepository functionRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        return "decoding/home";
    }

    @PostMapping("/")
    public String homePost(@RequestParam String text, Model model) {
        this.text = text;
        return "redirect:/decoding";
    }

    @PostMapping("/decoding")
    public String decodingPost(@RequestParam String text, Model model) {
        this.text = text;
        return "redirect:/decoding";
    }

    @GetMapping("/decoding")
    public String decoding(Model model) {
        List<String> ingText = new ArrayList<>();
        if (text != null) {
            ingText = Arrays.stream(text.split(", ")).map(String::toLowerCase).collect(Collectors.toList());
        }
        List<String> decoding = new ArrayList<>();
        Map<String, List<String>> ingByFunc = new HashMap<>();
        for (String ing : ingText) {
            Ingredient ingredient = ingredientRepository.findByInci(ing);
            if (ingredient == null) {
                ingredient = ingredientRepository.findByTradeName(ing);
            }
            if (ingredient == null) {
                ingredient = ingredientRepository.findByOtherNames(ing);
            }
            if (ingredient == null) {
                decoding.add(ing + " ???");
            } else {
                decoding.add(ingredient.getTradeName());
                for (String func : ingredient.getFunctions().stream().map(Function::getName).collect(Collectors.toList())) {
                    if (ingByFunc.containsKey(func)) {
                        List<String> elements = new ArrayList<>(ingByFunc.get(func));
                        elements.add(ingredient.getTradeName());
                        ingByFunc.put(func, elements);
                    } else {
                        ingByFunc.put(func, List.of(ingredient.getTradeName()));
                    }
                }
            }
        }
        model.addAttribute("text", text);
        model.addAttribute("decoding", decoding);
        model.addAttribute("ingByFunc", ingByFunc);
        return "decoding/decoding";
    }
}
