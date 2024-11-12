package ru.ingredients.controllers;

import org.javatuples.LabelValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ingredients.models.Category;
import ru.ingredients.models.Function;
import ru.ingredients.models.Ingredient;
import ru.ingredients.models.Percent;
import ru.ingredients.repo.CategoryRepository;
import ru.ingredients.repo.FunctionRepository;
import ru.ingredients.repo.IngredientRepository;
import ru.ingredients.repo.PercentRepository;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PercentRepository percentRepository;

    @ModelAttribute("allFunctions")
    public List<Function> allFunctions() {
        return (List<Function>) this.functionRepository.findAll();
    }

    @ModelAttribute("allCategories")
    public List<Category> allCategories() {
        return (List<Category>) this.categoryRepository.findAll();
    }

    @GetMapping("")
    public String ingredients(Model model, @RequestParam(required = false) String q, HttpServletRequest request) {
        Iterable<Ingredient> ingredients = q == null || q.isEmpty() ? ingredientRepository.findAll() :
                ingredientRepository.searchIngredients(q.toLowerCase().replaceAll("[^a-zA-Zа-яА-Я0-9]+", ""));
        List<LabelValue<String, String>> ingredientsNameId = ingredientRepository.getAllNamesId().stream().map(LabelValue::fromArray).collect(Collectors.toList());
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("isAdmin", request.isUserInRole("ROLE_ADMIN"));
        model.addAttribute("ingredientsNameId", ingredientsNameId);
        return "ingredients/ingredients";
    }

    @GetMapping("/new")
    public String ingredientsAdd(@ModelAttribute("ingredient") Ingredient ingredient) {
        return "ingredients/ingredients-new";
    }

    @RequestMapping(value = "/new", params = {"addPercent"})
    public String addPercent(@ModelAttribute("ingredient") Ingredient ingredient) {
        ingredient.getPercents().add(new Percent());
        return "ingredients/ingredients-new";
    }

    @RequestMapping(value = "/new", params = {"removePercent"})
    public String removePercent(@ModelAttribute("ingredient") Ingredient ingredient, final HttpServletRequest req) {
        final int percentIndex = Integer.parseInt(req.getParameter("removePercent"));
        ingredient.getPercents().remove(percentIndex);
        return "ingredients/ingredients-new";
    }

    @PostMapping("/new")
    public String saveIngredient(@ModelAttribute("ingredient") Ingredient ingredient, BindingResult bindingResult, final ModelMap model) {
        if (bindingResult.hasErrors()) {
            return "ingredients/ingredients-new";
        }
        ingredientRepository.save(ingredient);
        model.clear();
        return "redirect:/ingredients";
    }

    @GetMapping("/{id}")
    public String ingredientMore(@PathVariable(value = "id") long id, Model model, HttpServletRequest req) {
        if (ingredientRepository.findById(id).isEmpty()) {
            return "redirect:/ingredients";
        }
        Ingredient ing = ingredientRepository.findById(id).get();
        model.addAttribute("ing", ing);

        model.addAttribute("isAdmin", req.isUserInRole("ROLE_ADMIN"));
        model.addAttribute("referer", req.getHeader("Referer"));
        return "ingredients/ingredients-about";
    }

    @DeleteMapping("/{id}")
    public String ingredientsDelete(@PathVariable(value = "id") long id, Model model) {
        Ingredient ing = ingredientRepository.findById(id).orElseThrow();
        ingredientRepository.delete(ing);
        return "redirect:/ingredients";
    }

    @GetMapping("/{id}/edit")
    public String ingredientEdit(@PathVariable(value = "id") long id, Ingredient ing, final BindingResult bindingResult, Model model) {
        if (ingredientRepository.findById(id).isEmpty()) {
            return "redirect:/ingredients";
        }
        ing = ingredientRepository.findById(id).get();
        model.addAttribute("ingredient", ing);
        return "ingredients/ingredients-edit";
    }

    @RequestMapping(value = "/{id}/edit", params = {"addPercent"})
    public String addPercentEdit(@PathVariable(value = "id") long id, final Ingredient ing, final BindingResult bindingResult, Model model) {
        ing.getPercents().add(new Percent());
        return "ingredients/ingredients-edit";
    }

    @RequestMapping(value = "/{id}/edit", params = {"removePercent"})
    public String removePercentEdit(@PathVariable(value = "id") long id, final Ingredient ing, final BindingResult bindingResult, Model model, final HttpServletRequest req) {
        final int percentIndex = Integer.parseInt(req.getParameter("removePercent"));
        ing.getPercents().remove(percentIndex);
        return "ingredients/ingredients-edit";
    }

    @PatchMapping(value = "/{id}/edit", params = {"save"})
    public String ingredientsUpdate(@PathVariable(value = "id") long id, final Ingredient ing, final BindingResult bindingResult, final ModelMap model) {
        if (bindingResult.hasErrors()) {
            return "ingredients/ingredients-edit";
        }
        ingredientRepository.save(ing);
        return "redirect:/ingredients/{id}";
    }
}
