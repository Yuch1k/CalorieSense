package ru.CalorieSense.repository;

import ru.CalorieSense.model.Meal;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository {

     // Добавить приём пищи
    void addMeal(Meal meal);

     // Получить все приёмы пищи пользователя за определённую дату
    List<Meal> getMealsByDate(Long chatId, LocalDate date);

     // Получить все приёмы пищи пользователя за сегодня
    List<Meal> getTodayMeals(Long chatId);

     // Удалить приём пищи по ID
    void deleteMeal(Long mealId);
}
