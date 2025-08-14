package ru.ingredients.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import ru.ingredients.validation.MinLessThanMax;

@MinLessThanMax
public class PercentDTO {
    @Min(value = 0, message = "Процент не может быть меньше 0")
    @Max(value = 100, message = "Процент не может быть больше 100")
    private double min;
    @Min(value = 0, message = "Процент не может быть меньше 0")
    @Max(value = 100, message = "Процент не может быть больше 100")
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
