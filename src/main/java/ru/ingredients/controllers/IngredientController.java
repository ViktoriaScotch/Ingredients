package ru.ingredients.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ingredients.models.Category;
import ru.ingredients.models.Function;
import ru.ingredients.models.Ingredient;
import ru.ingredients.models.Percent;
import ru.ingredients.services.IngredientService;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {
    @Autowired
    private IngredientService ingredientService;

    @ModelAttribute("allFunctions")
    public List<Function> allFunctions() {
        return ingredientService.getAllFunctions();
    }

    @ModelAttribute("allCategories")
    public List<Category> allCategories() {
        return ingredientService.getAllCategories();
    }

    @GetMapping("")
    public String ingredients(Model model, HttpServletRequest request,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(required = false) String search,
                              @RequestParam(required = false) List<Long> selFuncId,
                              @RequestParam(required = false) List<Long> selCatId) {
        if (selFuncId == null) selFuncId = Collections.emptyList();
        if (selCatId == null) selCatId = Collections.emptyList();
        Page<Ingredient> ingredientPages = ingredientService.getIngredients(search, page, 20, selFuncId, selCatId);

        model.addAttribute("isAdmin", request.isUserInRole("ROLE_ADMIN"));
        model.addAttribute("ingredientsNameId", ingredientService.getIngredientsForAutocomplete());
        model.addAttribute("search", search);
        model.addAttribute("ingredients", ingredientPages.getContent());
        model.addAttribute("currentPage", ingredientPages.getNumber());
        model.addAttribute("totalPages", ingredientPages.getTotalPages());
        model.addAttribute("selFunc", ingredientService.getFuncById(selFuncId));
        model.addAttribute("selCat", ingredientService.getCatById(selCatId));
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
        ingredientService.saveIngredient(ingredient);
        model.clear();
        return "redirect:/ingredients";
    }

    @GetMapping("/{id}")
    public String ingredientMore(@PathVariable(value = "id") long id, Model model, HttpServletRequest req) {
        Ingredient ing;
        try {
            ing = ingredientService.findIngredientById(id);
        } catch (NoSuchElementException e) {
            return "redirect:/ingredients";
        }
        model.addAttribute("ing", ing);
        model.addAttribute("isAdmin", req.isUserInRole("ROLE_ADMIN"));
        model.addAttribute("referer", req.getHeader("Referer"));
        return "ingredients/ingredients-about";
    }

    @DeleteMapping("/{id}")
    public String ingredientsDelete(@PathVariable(value = "id") long id) {
        ingredientService.deleteIngredient(id);
        return "redirect:/ingredients";
    }

    @GetMapping("/{id}/edit")
    public String ingredientEdit(@PathVariable(value = "id") long id, Model model, HttpServletRequest req) {
        Ingredient ing;
        try {
            ing = ingredientService.findIngredientById(id);
        } catch (NoSuchElementException e) {
            return "redirect:/ingredients";
        }
        model.addAttribute("ingredient", ing);
        return "ingredients/ingredients-edit";
    }

    @RequestMapping(value = "/{id}/edit", params = {"addPercent"})
    public String addPercentEdit(final Ingredient ing) {
        ing.getPercents().add(new Percent());
        return "ingredients/ingredients-edit";
    }

    @RequestMapping(value = "/{id}/edit", params = {"removePercent"})
    public String removePercentEdit(final Ingredient ing, final HttpServletRequest req) {
        final int percentIndex = Integer.parseInt(req.getParameter("removePercent"));
        ing.getPercents().remove(percentIndex);
        return "ingredients/ingredients-edit";
    }

    @PatchMapping(value = "/{id}/edit")
    public String ingredientsUpdate(final Ingredient ing, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "ingredients/ingredients-edit";
        }
        ingredientService.saveIngredient(ing);
        return "redirect:/ingredients/{id}";
    }
}
