package ru.ingredients.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ingredients.models.Function;
import ru.ingredients.models.Ingredient;
import ru.ingredients.repo.FunctionRepository;
import ru.ingredients.repo.IngredientRepository;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class IngredientController {
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private FunctionRepository functionRepository;

    @GetMapping("/ingredients")
    public String ingredients(Model model) {
        Iterable<Ingredient> ingredients = ingredientRepository.findAll();
        model.addAttribute("ingredients", ingredients);
        return "ingredients/ingredients";
    }

    @GetMapping("/ingredient/add")
    public String ingredientsAdd(Model model) {
        Iterable<Function> functions = functionRepository.findAll();
        model.addAttribute("functions", functions);
        return "ingredients/ingredient-add";
    }

    @PostMapping("/ingredient/add")
    public String ingredientsPost(@RequestParam String inci, @RequestParam String translation, @RequestParam String description, @RequestParam(value = "functionsId[]") Long[] functionsId, @RequestParam String percent, @RequestParam String contraindication, Model model) {
        Set<Function> ingFunctions = new HashSet<>();
        for (Long funcId : functionsId) {
            functionRepository.findById(funcId).ifPresent(ingFunctions::add);
        }
        Ingredient ing = new Ingredient(inci.toLowerCase(), translation, description, ingFunctions, percent, contraindication);
        ingredientRepository.save(ing);
        return "redirect:/ingredients";
    }

    @GetMapping("/ingredient/{id}")
    public String ingredientMore(@PathVariable(value = "id") long id, Model model) {
        if (!ingredientRepository.existsById(id)) {
            return "redirect:/ingredients";
        }
        Optional<Ingredient> optionalIng = ingredientRepository.findById(id);
        ArrayList<Ingredient> ing = new ArrayList<>();
        optionalIng.ifPresent(ing::add);
        model.addAttribute("ing", ing);

        List<String> ingFunctions = ing.get(0).getFunctions().stream().map(Function::getName).collect(Collectors.toList());
        model.addAttribute("ingFunctions", ingFunctions);
        return "ingredients/ingredient";
    }

    @GetMapping("/ingredient/{id}/edit")
    public String ingredientEdit(@PathVariable(value = "id") long id, Model model) {
        if (!ingredientRepository.existsById(id)) {
            return "redirect:/ingredients";
        }
        Optional<Ingredient> optionalIng = ingredientRepository.findById(id);
        ArrayList<Ingredient> ing = new ArrayList<>();
        optionalIng.ifPresent(ing::add);
        model.addAttribute("ing", ing);

        List<Long> ingFunctions = ing.get(0).getFunctions().stream().map(Function::getId).collect(Collectors.toList());
        model.addAttribute("ingFunctions", ingFunctions);

        Iterable<Function> functions = functionRepository.findAll();
        model.addAttribute("functions", functions);
        return "ingredients/ingredient-edit";
    }

    @PostMapping("/ingredient/{id}/edit")
    public String ingredientsUpdate(@PathVariable(value = "id") long id, @RequestParam String inci, @RequestParam String translation, @RequestParam String description, @RequestParam(value = "functionsId[]") Long[] functionsId, @RequestParam String percent, @RequestParam String contraindication, Model model) {
        Ingredient ing = ingredientRepository.findById(id).orElseThrow();
        Set<Function> ingFunctions = new HashSet<>();
        for (Long funcId : functionsId) {
            functionRepository.findById(funcId).ifPresent(ingFunctions::add);
        }
        ing.setInci(inci)
                .setTranslation(translation)
                .setDescription(description)
                .setFunctions(ingFunctions)
                .setPercent(percent)
                .setContraindication(contraindication);
        ingredientRepository.save(ing);
        return "redirect:/ingredient/{id}";
    }

    @PostMapping("/ingredient/{id}/remove")
    public String ingredientsDelete(@PathVariable(value = "id") long id, Model model) {
        Ingredient ing = ingredientRepository.findById(id).orElseThrow();
        ingredientRepository.delete(ing);
        return "redirect:/ingredients";
    }
}
