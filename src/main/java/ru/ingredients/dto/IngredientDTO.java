package ru.ingredients.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IngredientDTO {
    private Long id;
    @NotBlank(message = "Название не может быть пустым")
    private String inci;
    @NotBlank(message = "Название не может быть пустым")
    private String tradeName;
    private String description;
    private Set<String> otherNames = new HashSet<>();
    private Set<FunctionDTO> functions = new HashSet<>();
    private Set<CategoryDTO> categories = new HashSet<>();
    @Valid
    private List<PercentDTO> percents = new ArrayList<>();

    public IngredientDTO() {
    }

    public IngredientDTO(
            Long id,
            String inci,
            String tradeName,
            String description,
            Set<String> otherNames,
            Set<FunctionDTO> functions,
            Set<CategoryDTO> categories,
            List<PercentDTO> percents) {
        this.id = id;
        this.inci = inci;
        this.tradeName = tradeName;
        this.description = description;
        this.otherNames = otherNames;
        this.functions = functions;
        this.categories = categories;
        this.percents = percents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInci() {
        return inci;
    }

    public void setInci(String inci) {
        this.inci = inci;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(Set<String> otherNames) {
        this.otherNames = otherNames;
    }

    public Set<FunctionDTO> getFunctions() {
        return functions;
    }

    public void setFunctions(Set<FunctionDTO> functions) {
        this.functions = functions;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }

    public List<PercentDTO> getPercents() {
        return percents;
    }

    public void setPercents(List<PercentDTO> percents) {
        this.percents = percents;
    }
}
