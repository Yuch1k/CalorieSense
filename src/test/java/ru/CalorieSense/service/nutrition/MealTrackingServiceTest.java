package ru.CalorieSense.service.nutrition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.CalorieSense.model.DailyProgress;
import ru.CalorieSense.model.MacronutrientResult;
import ru.CalorieSense.model.Meal;
import ru.CalorieSense.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MealTrackingServiceTest {

    @Mock
    private MealRepository mealRepository;

    private MealTrackingService mealTracker;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mealTracker = new MealTrackingService(mealRepository);
    }

    @Test
    void testCalculateDailyProgress_NoMeals() {
        Long chatId = 123L;
        MacronutrientResult target = new MacronutrientResult(2000, 150, 55, 225);

        when(mealRepository.getTodayMeals(chatId)).thenReturn(Collections.emptyList());

        DailyProgress progress = mealTracker.calculateDailyProgress(chatId, target);

        assertEquals(0, progress.getConsumedCalories());
        assertEquals(0, progress.getConsumedProtein());
        assertEquals(0, progress.getConsumedFats());
        assertEquals(0, progress.getConsumedCarbs());
        assertEquals(2000, progress.getRemainingCalories());
        verify(mealRepository).getTodayMeals(chatId);
    }

    @Test
    void testCalculateDailyProgress_WithMultipleMeals() {
        Long chatId = 123L;
        MacronutrientResult target = new MacronutrientResult(2000, 150, 55, 225);

        List<Meal> meals = Arrays.asList(
                new Meal(1L, chatId, LocalDateTime.now(), 500, 30, 15, 50, "Завтрак"),
                new Meal(2L, chatId, LocalDateTime.now(), 700, 40, 20, 80, "Обед"),
                new Meal(3L, chatId, LocalDateTime.now(), 400, 25, 10, 50, "Ужин")
        );

        when(mealRepository.getTodayMeals(chatId)).thenReturn(meals);

        DailyProgress progress = mealTracker.calculateDailyProgress(chatId, target);

        assertEquals(1600, progress.getConsumedCalories());
        assertEquals(95, progress.getConsumedProtein());
        assertEquals(45, progress.getConsumedFats());
        assertEquals(180, progress.getConsumedCarbs());
        assertEquals(400, progress.getRemainingCalories());
        assertEquals(80.0, progress.getCaloriesPercentage(), 0.1);
    }

    @Test
    void testGetTodayMeals() {
        Long chatId = 123L;
        List<Meal> expectedMeals = Arrays.asList(
                new Meal(1L, chatId, LocalDateTime.now(), 500, 30, 15, 50, "Meal 1")
        );

        when(mealRepository.getTodayMeals(chatId)).thenReturn(expectedMeals);

        List<Meal> actualMeals = mealTracker.getTodayMeals(chatId);

        assertEquals(expectedMeals, actualMeals);
        verify(mealRepository).getTodayMeals(chatId);
    }
}