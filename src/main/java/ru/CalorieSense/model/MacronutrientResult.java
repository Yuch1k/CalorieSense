package ru.CalorieSense.model;

public class MacronutrientResult {
    private final int calories;
    private final int protein;
    private final int fats;
    private final int carbs;

    public MacronutrientResult(int calories, int protein, int fats, int carbs) {
        this.calories = calories;
        this.protein = protein;
        this.fats = fats;
        this.carbs = carbs;
    }

    public int getCalories() {
        return calories;
    }

    public int getProtein() {
        return protein;
    }

    public int getFats() {
        return fats;
    }

    public int getCarbs() {
        return carbs;
    }
}
