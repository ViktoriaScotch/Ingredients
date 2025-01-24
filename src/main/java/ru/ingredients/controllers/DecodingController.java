package ru.ingredients.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ingredients.models.Ingredient;
import ru.ingredients.services.DecodingService;

import java.util.List;
import java.util.Map;

@Controller
public class DecodingController {
    private String text;
    @Autowired
    private DecodingService decodingService;

    @GetMapping("/")
    public String home() {
        return "decoding/home";
    }

    @PostMapping("/")
    public String homePost(@RequestParam String text) {
        this.text = text;
        return "redirect:/decoding";
    }

    @PostMapping("/decoding")
    public String decodingPost(@RequestParam String text) {
        this.text = text;
        return "redirect:/decoding";
    }

    @GetMapping("/decoding")
    public String decoding(Model model) {
        List<Ingredient> ingredients = decodingService.findIng(text);
        Map<String, List<Ingredient>> ingByFunc = decodingService.groupByFunc(ingredients);
        Map<String, List<Ingredient>> ingByCat = decodingService.groupByCat(ingredients);

        model.addAttribute("text", text);
        model.addAttribute("ingByCat", ingByCat);
        model.addAttribute("ingByFunc", ingByFunc);
        model.addAttribute("ingredients", ingredients);
        return "decoding/decoding";
    }

//    @GetMapping("/decoding")
//    public String decoding(Model model) {
//        List<String> ingText = new ArrayList<>();
//        if (text != null) {
//            ingText = Arrays.stream(text.split(", ")).collect(Collectors.toList());
//        }
//
//        Map<String, List<Ingredient>> ingByFunc = new HashMap<>();
//        Map<String, List<Ingredient>> ingByCat = new HashMap<>();
//        List<Ingredient> ingredients = new ArrayList<>();
//
//        for (String ing : ingText) {
//            Ingredient ingredient;
//            try {
//                ingredient = ingredientRepository.findByAllNames(ing.toLowerCase()
//                        .replaceAll("\\(.+?\\)", "").replaceAll("[^a-zA-Zа-яА-Я0-9]+", ""));
//            }
//            catch (IncorrectResultSizeDataAccessException e) {
//                ingredient =ingredientRepository.findByAllNames(
//                        StringUtils.substringBetween(ing.toLowerCase(), "(", ")").replaceAll("[^a-zA-Zа-яА-Я0-9]+", ""));
//            }
//            if (ingredient == null) {
//                ingredients.add(new Ingredient().setTradeName(ing));
//            } else {
//                ingredients.add(ingredient);
//
//                for (String func : ingredient.getFunctions().stream().map(Function::getName).collect(Collectors.toList())) {
//                    if (ingByFunc.containsKey(func)) {
//                        List<Ingredient> elements = new ArrayList<>(ingByFunc.get(func));
//                        elements.add(ingredient);
//                        ingByFunc.put(func, elements);
//                    } else {
//                        ingByFunc.put(func, List.of(ingredient));
//                    }
//                }
//
//                for (String cat : ingredient.getCategories().stream().map(Category::getName).collect(Collectors.toList())) {
//                    if (ingByCat.containsKey(cat)) {
//                        List<Ingredient> elements = new ArrayList<>(ingByCat.get(cat));
//                        elements.add(ingredient);
//                        ingByCat.put(cat, elements);
//                    } else {
//                        ingByCat.put(cat, List.of(ingredient));
//                    }
//                }
//            }
//        }
//        model.addAttribute("text", text);
//        model.addAttribute("ingByCat", ingByCat);
//        model.addAttribute("ingByFunc", ingByFunc);
//        model.addAttribute("ingredients", ingredients);
//        return "decoding/decoding";
//    }
}
