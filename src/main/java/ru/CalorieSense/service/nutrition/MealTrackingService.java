package ru.CalorieSense.service.nutrition;

import ru.CalorieSense.model.DailyProgress;
import ru.CalorieSense.model.MacronutrientResult;
import ru.CalorieSense.model.Meal;
import ru.CalorieSense.repository.MealRepository;

import java.util.List;

public class MealTrackingService {

    private final MealRepository mealRepository;

    public MealTrackingService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    /**
     * Рассчитывает прогресс пользователя за сегодня
     */
    public DailyProgress calculateDailyProgress(Long chatId, MacronutrientResult target) {
        List<Meal> todayMeals = mealRepository.getTodayMeals(chatId);

        int consumedCalories = todayMeals.stream().mapToInt(Meal::getCalories).sum();
        int consumedProtein = todayMeals.stream().mapToInt(Meal::getProtein).sum();
        int consumedFats = todayMeals.stream().mapToInt(Meal::getFats).sum();
        int consumedCarbs = todayMeals.stream().mapToInt(Meal::getCarbs).sum();

        return new DailyProgress(
                consumedCalories, consumedProtein, consumedFats, consumedCarbs,
                target.getCalories(), target.getProtein(), target.getFats(), target.getCarbs()
        );
    }

    /**
     * Возвращает все приёмы пищи за сегодня
     */
    public List<Meal> getTodayMeals(Long chatId) {
        return mealRepository.getTodayMeals(chatId);
    }
}
