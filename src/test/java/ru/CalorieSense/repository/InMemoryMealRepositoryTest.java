package ru.CalorieSense.repository;

import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.Meal;
import ru.CalorieSense.repository.InMemoryMealRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryMealRepositoryTest {

    @Test
    void testAddAndGetMealsByDate() {
        InMemoryMealRepository repo = new InMemoryMealRepository();
        Long chatId = 100L;
        Long id = repo.generateId();
        Meal meal = new Meal(id, chatId, LocalDateTime.now(), 450, 30, 12, 78, "Обед");
        repo.addMeal(meal);

        List<Meal> todayMeals = repo.getTodayMeals(chatId);
        assertFalse(todayMeals.isEmpty());
        assertEquals(450, todayMeals.get(0).getCalories());
        assertEquals("Обед", todayMeals.get(0).getDescription());
    }

    @Test
    void testDeleteMeal() {
        InMemoryMealRepository repo = new InMemoryMealRepository();
        Long chatId = 101L;
        Long id = repo.generateId();
        Meal meal = new Meal(id, chatId, LocalDateTime.now(), 200, 10, 5, 18, "Перекус");
        repo.addMeal(meal);

        repo.deleteMeal(id);
        List<Meal> todayMeals = repo.getTodayMeals(chatId);
        assertTrue(todayMeals.isEmpty());
    }

    @Test
    void testGenerateUniqueIds() {
        InMemoryMealRepository repo = new InMemoryMealRepository();
        Long id1 = repo.generateId();
        Long id2 = repo.generateId();
        assertNotEquals(id1, id2);
    }
}
