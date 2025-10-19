package ru.CalorieSense.command.feature;

import ru.CalorieSense.model.MacronutrientResult;
import ru.CalorieSense.model.UserProfile;

public class CalorieCalculatorService {

    private static final double PROTEIN_RATIO = 0.30;
    private static final double FAT_RATIO = 0.25;
    private static final double CARB_RATIO = 0.45;

    private static final int PROTEIN_CALORIES_PER_GRAM = 4;
    private static final int FAT_CALORIES_PER_GRAM = 9;
    private static final int CARB_CALORIES_PER_GRAM = 4;

    /**
     * Рассчитывает базовый метаболизм (BMR) по формуле Миффлина-Сан Жеора
     */
    public int calculate(UserProfile profile) {
        if (profile.getSex() == null || profile.getHeight() <= 0 ||
                profile.getWeight() <= 0 || profile.getAge() <= 0) {
            return 2200;
        }

        double bmr;
        if ("М".equalsIgnoreCase(profile.getSex())) {
            bmr = 10 * profile.getWeight() + 6.25 * profile.getHeight() -
                    5 * profile.getAge() + 5;
        } else {
            bmr = 10 * profile.getWeight() + 6.25 * profile.getHeight() -
                    5 * profile.getAge() - 161;
        }

        double activityMultiplier = getActivityMultiplier(profile.getActivityLevel());

        return (int) Math.round(bmr * activityMultiplier);
    }

    /**
     * Возвращает коэффициент активности
     */
    private double getActivityMultiplier(String activityLevel) {
        return switch (activityLevel) {
            case "low" -> 1.2;
            case "medium" -> 1.375;
            case "high" -> 1.55;
            default -> 1.2;
        };
    }

    /**
     * Рассчитывает калории и макронутриенты (БЖУ)
     */
    public MacronutrientResult calculateWithMacros(UserProfile profile) {
        int baseCalories = calculate(profile);
        int adjustedCalories = baseCalories;

        if ("lose".equals(profile.getGoal())) {
            adjustedCalories = (int) (baseCalories * 0.85); // -15% для похудения
        } else if ("gain".equals(profile.getGoal())) {
            adjustedCalories = (int) (baseCalories * 1.15); // +15% для набора массы
        }

        double proteinRatio, fatRatio, carbRatio;

        if ("lose".equals(profile.getGoal())) {
            proteinRatio = 0.35;
            fatRatio = 0.25;
            carbRatio = 0.40;
        } else if ("gain".equals(profile.getGoal())) {
            proteinRatio = 0.30;
            fatRatio = 0.20;
            carbRatio = 0.50;
        } else {
            proteinRatio = 0.30;
            fatRatio = 0.25;
            carbRatio = 0.45;
        }

        int protein = (int) Math.round((adjustedCalories * proteinRatio) / PROTEIN_CALORIES_PER_GRAM);
        int fats = (int) Math.round((adjustedCalories * fatRatio) / FAT_CALORIES_PER_GRAM);
        int carbs = (int) Math.round((adjustedCalories * carbRatio) / CARB_CALORIES_PER_GRAM);

        return new MacronutrientResult(adjustedCalories, protein, fats, carbs);
    }

    /**
     * Возвращает текстовое описание уровня активности
     */
    public String getActivityLevelText(String activityLevel) {
        return switch (activityLevel) {
            case "low" -> "Малая (без тренировок)";
            case "medium" -> "Средняя (1-2 тренировки в неделю)";
            case "high" -> "Большая (более 2 тренировок в неделю)";
            default -> "Малая (без тренировок)";
        };
    }
}
