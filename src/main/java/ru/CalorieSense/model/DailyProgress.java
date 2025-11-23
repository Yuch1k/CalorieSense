package ru.CalorieSense.model;

public class DailyProgress {

    private final int consumedCalories;
    private final int consumedProtein;
    private final int consumedFats;
    private final int consumedCarbs;

    private final int targetCalories;
    private final int targetProtein;
    private final int targetFats;
    private final int targetCarbs;

    public DailyProgress(int consumedCalories, int consumedProtein, int consumedFats, int consumedCarbs,
                         int targetCalories, int targetProtein, int targetFats, int targetCarbs) {
        this.consumedCalories = consumedCalories;
        this.consumedProtein = consumedProtein;
        this.consumedFats = consumedFats;
        this.consumedCarbs = consumedCarbs;
        this.targetCalories = targetCalories;
        this.targetProtein = targetProtein;
        this.targetFats = targetFats;
        this.targetCarbs = targetCarbs;
    }

    public int getConsumedCalories() { return consumedCalories; }
    public int getConsumedProtein() { return consumedProtein; }
    public int getConsumedFats() { return consumedFats; }
    public int getConsumedCarbs() { return consumedCarbs; }

    public int getTargetCalories() { return targetCalories; }
    public int getTargetProtein() { return targetProtein; }
    public int getTargetFats() { return targetFats; }
    public int getTargetCarbs() { return targetCarbs; }

    public int getRemainingCalories() {
        return targetCalories - consumedCalories;
    }
    public int getRemainingProtein() {
        return targetProtein - consumedProtein;
    }
    public int getRemainingFats() {
        return targetFats - consumedFats;
    }
    public int getRemainingCarbs() {
        return targetCarbs - consumedCarbs;
    }

    public double getCaloriesPercentage() {
        return targetCalories > 0 ? (consumedCalories * 100.0 / targetCalories) : 0;
    }
}
