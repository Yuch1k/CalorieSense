package ru.CalorieSense.model;

/**
 * Результат расчёта макронутриентов (БЖУ)
 */
public class MacronutrientResult {
    private final int calories;
    private final int protein;  // белки в граммах
    private final int fats;     // жиры в граммах
    private final int carbs;    // углеводы в граммах

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

    @Override
    public String toString() {
        return String.format(
                "MacronutrientResult{calories=%d, protein=%d g, fats=%d g, carbs=%d g}",
                calories, protein, fats, carbs
        );
    }
}