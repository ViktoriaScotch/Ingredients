package ru.ingredients.models;

import javax.persistence.*;

@Entity
public class Percent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private double min;

    private double max;

    private String comment;

    public Percent() {
    }

    public Percent(Long id, double min, double max, String comment) {
        this.id = id;
        this.min = min;
        this.max = max;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
