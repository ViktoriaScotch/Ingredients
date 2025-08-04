package ru.ingredients.dto;

import java.util.Objects;

public class PercentDTO {
    private double min;
    private double max;
    private String comment;

    public PercentDTO() {
    }

    public PercentDTO(double min, double max, String comment) {
        this.min = min;
        this.max = max;
        this.comment = comment;
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
