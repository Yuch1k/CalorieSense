package ru.CalorieSense.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryMealRepositoryTest {

    private InMemoryMealRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryMealRepository();
    }

    @Test
    void testGenerateId_ReturnsUniqueIds() {
        Long id1 = repository.generateId();
        Long id2 = repository.generateId();
        Long id3 = repository.generateId();

        assertNotEquals(id1, id2);
        assertNotEquals(id2, id3);
        assertTrue(id2 > id1);
        assertTrue(id3 > id2);
    }

    @Test
    void testAddMeal_AndRetrieve() {
        Long mealId = repository.generateId();
        Meal meal = new Meal(mealId, 123L, LocalDateTime.now(), 500, 30, 15, 50, "Breakfast");

        repository.addMeal(meal);
        List<Meal> todayMeals = repository.getTodayMeals(123L);

        assertEquals(1, todayMeals.size());
        assertEquals(meal, todayMeals.get(0));
    }

    @Test
    void testGetTodayMeals_EmptyForNewUser() {
        List<Meal> meals = repository.getTodayMeals(999L);

        assertTrue(meals.isEmpty());
    }

    @Test
    void testGetTodayMeals_OnlyReturnsCurrentDate() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = today.minusDays(1);

        Meal todayMeal = new Meal(1L, 123L, today, 500, 30, 15, 50, "Today");
        Meal yesterdayMeal = new Meal(2L, 123L, yesterday, 600, 35, 20, 60, "Yesterday");

        repository.addMeal(todayMeal);
        repository.addMeal(yesterdayMeal);

        List<Meal> todayMeals = repository.getTodayMeals(123L);

        assertEquals(1, todayMeals.size());
        assertEquals("Today", todayMeals.get(0).getDescription());
    }

    @Test
    void testGetMealsByDate_SpecificDate() {
        LocalDate targetDate = LocalDate.of(2025, 10, 15);
        LocalDateTime targetDateTime = targetDate.atTime(12, 0);
        LocalDateTime otherDateTime = LocalDate.of(2025, 10, 16).atTime(12, 0);

        Meal targetMeal = new Meal(1L, 123L, targetDateTime, 500, 30, 15, 50, "Target");
        Meal otherMeal = new Meal(2L, 123L, otherDateTime, 600, 35, 20, 60, "Other");

        repository.addMeal(targetMeal);
        repository.addMeal(otherMeal);

        List<Meal> meals = repository.getMealsByDate(123L, targetDate);

        assertEquals(1, meals.size());
        assertEquals("Target", meals.get(0).getDescription());
    }

    @Test
    void testGetMealsByDate_MultipleUsersIsolated() {
        Meal user1Meal = new Meal(1L, 123L, LocalDateTime.now(), 500, 30, 15, 50, "User1");
        Meal user2Meal = new Meal(2L, 456L, LocalDateTime.now(), 600, 35, 20, 60, "User2");

        repository.addMeal(user1Meal);
        repository.addMeal(user2Meal);

        List<Meal> user1Meals = repository.getTodayMeals(123L);
        List<Meal> user2Meals = repository.getTodayMeals(456L);

        assertEquals(1, user1Meals.size());
        assertEquals(1, user2Meals.size());
        assertEquals("User1", user1Meals.get(0).getDescription());
        assertEquals("User2", user2Meals.get(0).getDescription());
    }

    @Test
    void testDeleteMeal() {
        Long mealId = repository.generateId();
        Meal meal = new Meal(mealId, 123L, LocalDateTime.now(), 500, 30, 15, 50, "Breakfast");
        repository.addMeal(meal);

        repository.deleteMeal(mealId);
        List<Meal> meals = repository.getTodayMeals(123L);

        assertTrue(meals.isEmpty());
    }

    @Test
    void testDeleteMeal_NonExistentId_DoesNotThrow() {
        assertDoesNotThrow(() -> repository.deleteMeal(999L));
    }

    @Test
    void testGetTodayMeals_MultipleMealsForSameUser() {
        Meal meal1 = new Meal(1L, 123L, LocalDateTime.now(), 500, 30, 15, 50, "Meal1");
        Meal meal2 = new Meal(2L, 123L, LocalDateTime.now(), 600, 35, 20, 60, "Meal2");
        Meal meal3 = new Meal(3L, 123L, LocalDateTime.now(), 700, 40, 25, 70, "Meal3");

        repository.addMeal(meal1);
        repository.addMeal(meal2);
        repository.addMeal(meal3);

        List<Meal> meals = repository.getTodayMeals(123L);

        assertEquals(3, meals.size());
    }
}
