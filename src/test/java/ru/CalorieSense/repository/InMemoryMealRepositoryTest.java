package ru.CalorieSense.repository;

import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryMealRepositoryTest {

    @Test
    void addAndGetMealsByDate() {
        InMemoryMealRepository repo = new InMemoryMealRepository();
        Long chatId = 10L;
        LocalDate today = LocalDate.now();

        Long id1 = repo.generateId();
        Long id2 = repo.generateId();

        repo.addMeal(new Meal(id1, chatId, today.atTime(9, 0), 300, 20, 10, 40, "Завтрак"));
        repo.addMeal(new Meal(id2, chatId, today.atTime(13, 0), 500, 30, 15, 60, "Обед"));

        List<Meal> meals = repo.getMealsByDate(chatId, today);
        assertEquals(2, meals.size());
    }

    @Test
    void getTodayMealsUsesCurrentDate() {
        InMemoryMealRepository repo = new InMemoryMealRepository();
        Long chatId = 11L;
        LocalDate today = LocalDate.now();

        Long id = repo.generateId();
        repo.addMeal(new Meal(id, chatId, today.atTime(12, 0), 400, 25, 10, 50, "Обед"));

        List<Meal> meals = repo.getTodayMeals(chatId);
        assertEquals(1, meals.size());
    }

    @Test
    void deleteMealRemovesIt() {
        InMemoryMealRepository repo = new InMemoryMealRepository();
        Long chatId = 12L;
        LocalDate today = LocalDate.now();

        Long id = repo.generateId();
        repo.addMeal(new Meal(id, chatId, today.atTime(8, 0), 200, 10, 5, 20, "Завтрак"));

        repo.deleteMeal(id);

        List<Meal> meals = repo.getMealsByDate(chatId, today);
        assertTrue(meals.isEmpty());
    }

    @Test
    void findByIdReturnsSavedMeal() {
        InMemoryMealRepository repo = new InMemoryMealRepository();
        Long chatId = 13L;
        LocalDateTime ts = LocalDateTime.now();
        Long id = repo.generateId();
        Meal meal = new Meal(id, chatId, ts, 250, 15, 7, 30, "Перекус");

        repo.addMeal(meal);

        Meal loaded = repo.findById(id);
        assertNotNull(loaded);
        assertEquals("Перекус", loaded.getDescription());
    }
}
