package ru.CalorieSense.repository;

import ru.CalorieSense.model.Meal;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository {

    Long generateId();

    void addMeal(Meal meal);

    List<Meal> getMealsByDate(Long chatId, LocalDate date);

    List<Meal> getTodayMeals(Long chatId);

    void deleteMeal(Long mealId);
}
