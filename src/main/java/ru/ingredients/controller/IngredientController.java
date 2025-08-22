package ru.ingredients.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.ingredients.dto.CategoryDTO;
import ru.ingredients.dto.FunctionDTO;
import ru.ingredients.dto.IngredientDTO;
import ru.ingredients.dto.PercentDTO;
import ru.ingredients.service.IngredientService;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {
    @Autowired
    private IngredientService ingredientService;

    @ModelAttribute("allFunctions")
    public List<FunctionDTO> allFunctions() {
        return ingredientService.getAllFunctions();
    }

    @ModelAttribute("allCategories")
    public List<CategoryDTO> allCategories() {
        return ingredientService.getAllCategories();
    }

    @GetMapping("")
    public String ingredients(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(required = false) String search,
                              @RequestParam(required = false) List<Long> selFuncId,
                              @RequestParam(required = false) List<Long> selCatId) {
        if (selFuncId == null) selFuncId = Collections.emptyList();
        if (selCatId == null) selCatId = Collections.emptyList();
        Page<IngredientDTO> ingredientPages = ingredientService.getIngredients(search, page, 20, selFuncId, selCatId);

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
    public String ingredientsAdd(@ModelAttribute("ingredient") IngredientDTO ingredient) {
        return "ingredients/ingredients-new";
    }

    @RequestMapping(value = "/new", params = {"addPercent"})
    public String addPercent(@ModelAttribute("ingredient") IngredientDTO ingredient) {
        ingredient.getPercents().add(new PercentDTO());
        return "ingredients/ingredients-new";
    }

    @RequestMapping(value = "/new", params = {"removePercent"})
    public String removePercent(@ModelAttribute("ingredient") IngredientDTO ingredient, int removePercent) {
        ingredient.getPercents().remove(removePercent);
        return "ingredients/ingredients-new";
    }

    @PostMapping("/new")
    public String saveIngredient(@Valid @ModelAttribute("ingredient") IngredientDTO ingredient, BindingResult bindingResult) {
        try {
            ingredientService.saveIngredient(ingredient);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("duplicate", e.getMessage());
        }
        if (bindingResult.hasErrors()) {
            return "ingredients/ingredients-new";
        }
        return "redirect:/ingredients";
    }

    @GetMapping("/{id}")
    public String ingredientMore(@PathVariable(value = "id") long id, Model model, RedirectAttributes redirectAttributes) {
        IngredientDTO ing;
        try {
            ing = ingredientService.getIngredientById(id);
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ингредиент не найден");
            return "redirect:/ingredients";
        }
        model.addAttribute("ing", ing);
        return "ingredients/ingredients-about";
    }

    @DeleteMapping("/{id}")
    public String ingredientsDelete(@PathVariable(value = "id") long id) {
        ingredientService.deleteIngredient(id);
        return "redirect:/ingredients";
    }

    @GetMapping("/{id}/edit")
    public String ingredientEdit(@PathVariable(value = "id") long id, Model model, RedirectAttributes redirectAttributes) {
        IngredientDTO ing;
        try {
            ing = ingredientService.getIngredientById(id);
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ингредиент не найден");
            return "redirect:/ingredients";
        }
        model.addAttribute("ingredient", ing);
        return "ingredients/ingredients-edit";
    }

    @RequestMapping(value = "/{id}/edit", params = {"addPercent"})
    public String addPercentEdit(@ModelAttribute("ingredient") IngredientDTO ing) {
        ing.getPercents().add(new PercentDTO());
        return "ingredients/ingredients-edit";
    }

    @RequestMapping(value = "/{id}/edit", params = {"removePercent"})
    public String removePercentEdit(@ModelAttribute("ingredient") IngredientDTO ing, int removePercent) {
        ing.getPercents().remove(removePercent);
        return "ingredients/ingredients-edit";
    }

    @PatchMapping(value = "/{id}/edit")
    public String ingredientsUpdate(@Valid @ModelAttribute("ingredient") IngredientDTO ing, final BindingResult bindingResult) {
        try {
            ingredientService.saveIngredient(ing);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("duplicate", e.getMessage());
        }
        if (bindingResult.hasErrors()) {
            return "ingredients/ingredients-edit";
        }
        return "redirect:/ingredients/{id}";
    }
}
