package ru.ingredients.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String inci;

    private String tradeName;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    private String description;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "ingredient_id")
    private List<Percent> percents = new ArrayList<>();

    @ElementCollection
    private Set<String> otherNames = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "ingredient_function",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "function_id")
    )
    private Set<Function> functions = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "ingredient_category",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public Ingredient() {
    }

    public Ingredient(String inci, String tradeName, String description, Set<String> otherNames, Set<Function> functions, Set<Category> categories, List<Percent> percents) {
        this.inci = inci;
        this.tradeName = tradeName;
        this.description = description;
        this.percents = percents;
        this.otherNames = otherNames;
        this.functions = functions;
        this.categories = categories;
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

    public Ingredient setInci(String inci) {
        this.inci = inci;
        return this;
    }

    public String getTradeName() {
        return tradeName;
    }

    public Ingredient setTradeName(String tradeName) {
        this.tradeName = tradeName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Ingredient setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<Percent> getPercents() {
        return percents;
    }

    public Ingredient setPercents(List<Percent> percents) {
        this.percents = percents;
        return this;
    }

    public Set<String> getOtherNames() {
        return otherNames;
    }

    public Ingredient setOtherNames(Set<String> otherNames) {
        this.otherNames = otherNames;
        return this;
    }

    public Set<Function> getFunctions() {
        return functions;
    }

    public Ingredient setFunctions(Set<Function> functions) {
        this.functions = functions;
        return this;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Ingredient setCategories(Set<Category> categories) {
        this.categories = categories;
        return this;
    }
}
