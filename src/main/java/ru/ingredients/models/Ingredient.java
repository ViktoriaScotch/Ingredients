package ru.ingredients.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id", nullable = false)
    private Long id;

    String inci;
    String translation;
    String description;
    String percent;
    String contraindication;

    public Ingredient() {
    }

    public Ingredient(String inci, String translation, List<Function> functions) {
        this.inci = inci;
        this.translation = translation;
    }

    public Ingredient(String inci, String translation, String description, String percent, String contraindication) {
        this.inci = inci;
        this.translation = translation;
        this.description = description;
        this.percent = percent;
        this.contraindication = contraindication;
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
}
