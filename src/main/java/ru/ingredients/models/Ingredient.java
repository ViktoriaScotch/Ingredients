package ru.ingredients.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    String inci;
    String translation;
    String description;
    String percent;
    String contraindication;
    @ManyToMany
    Set<Function> functions = new HashSet<>();

    public Set<Function> getFunctions() {
        return functions;
    }

    public Ingredient setFunctions(Set<Function> functions) {
        this.functions = functions;
        return this;
    }

    public Ingredient() {
    }

    public Ingredient(String inci, String translation, String description, Set<Function> functions, String percent, String contraindication) {
        this.inci = inci;
        this.translation = translation;
        this.description = description;
        this.percent = percent;
        this.contraindication = contraindication;
        this.functions = functions;
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

    public String getTranslation() {
        return translation;
    }

    public Ingredient setTranslation(String translation) {
        this.translation = translation;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Ingredient setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPercent() {
        return percent;
    }

    public Ingredient setPercent(String percent) {
        this.percent = percent;
        return this;
    }

    public String getContraindication() {
        return contraindication;
    }

    public Ingredient setContraindication(String contraindication) {
        this.contraindication = contraindication;
        return this;
    }

    public void addFunction(Function p) {
        this.functions.add(p);
        p.getIngredients().add(this);
    }

    public void removeFunction(Function p) {
        this.functions.remove(p);
        p.getIngredients().remove(this);
    }
}
