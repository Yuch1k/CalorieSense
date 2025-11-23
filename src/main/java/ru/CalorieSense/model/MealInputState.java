package ru.CalorieSense.model;

public class MealInputState {
    public enum Step {
        NONE, CALORIES, PROTEIN, FATS, CARBS
    }

    private Step step = Step.NONE;
    private Integer calories;
    private Integer protein;
    private Integer fats;
    private Integer carbs;

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public void next() {
        switch (step) {
            case NONE -> step = Step.CALORIES;
            case CALORIES -> step = Step.PROTEIN;
            case PROTEIN -> step = Step.FATS;
            case FATS -> step = Step.CARBS;
            case CARBS -> step = Step.NONE;
        }
    }

    public void reset() {
        step = Step.NONE;
        calories = null;
        protein = null;
        fats = null;
        carbs = null;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public Integer getFats() {
        return fats;
    }

    public void setFats(Integer fats) {
        this.fats = fats;
    }

    public Integer getCarbs() {
        return carbs;
    }

    public void setCarbs(Integer carbs) {
        this.carbs = carbs;
    }
}
