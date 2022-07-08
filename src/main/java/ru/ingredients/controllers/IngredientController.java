package ru.ingredients.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.ingredients.models.Ingredient;
import ru.ingredients.repo.IngredientRepository;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class IngredientController {

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping("/ingredients")
    public String ingredients(Model model) {
        Iterable<Ingredient> ingredients = ingredientRepository.findAll();
        model.addAttribute("ingredients", ingredients);
        return "ingredients";
    }

    @GetMapping("/ingredient/add")
    public String ingredientsAdd(Model model) {
        return "ingredient-add";
    }

    @PostMapping("/ingredient/add")
    public String ingredientsPost(@RequestParam String inci, @RequestParam String translation, @RequestParam String description, @RequestParam String percent, @RequestParam String contraindication, Model model) {
        Ingredient ing = new Ingredient(inci, translation, description, percent, contraindication);
        ingredientRepository.save(ing);
        return "redirect:/ingredients";
    }

    @GetMapping("/ingredient/{id}")
    public String ingredientMore(@PathVariable(value = "id") long id, Model model) {
        if (!ingredientRepository.existsById(id)) {
            return "redirect:/ingredients";
        }
        Optional<Ingredient> ing = ingredientRepository.findById(id);
        ArrayList<Ingredient> result = new ArrayList<>();
        ing.ifPresent(result::add);
        model.addAttribute("ing", result);
        return "ingredient";
    }

    @GetMapping("/ingredient/{id}/edit")
    public String ingredientEdit(@PathVariable(value = "id") long id, Model model) {
        if (!ingredientRepository.existsById(id)) {
            return "redirect:/ingredients";
        }
        Optional<Ingredient> ing = ingredientRepository.findById(id);
        ArrayList<Ingredient> result = new ArrayList<>();
        ing.ifPresent(result::add);
        model.addAttribute("ing", result);
        return "ingredient-edit";
    }

    @PostMapping("/ingredient/{id}/edit")
    public String ingredientsUpdate(@PathVariable(value = "id") long id, @RequestParam String inci, @RequestParam String translation, @RequestParam String description, @RequestParam String percent, @RequestParam String contraindication, Model model) {
        Ingredient ing = ingredientRepository.findById(id).orElseThrow();
        ing.setInci(inci)
                .setTranslation(translation)
                .setDescription(description)
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
