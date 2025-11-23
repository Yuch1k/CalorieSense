package ru.CalorieSense.service.nutrition;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.CalorieSense.model.Meal;
import ru.CalorieSense.model.MacronutrientResult;
import ru.CalorieSense.model.DailyProgress;
import ru.CalorieSense.repository.MealRepository;
import ru.CalorieSense.service.nutrition.MealTrackingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MealTrackingServiceTest {

    @Test
    void testCalculateDailyProgress() {
        MealRepository repo = Mockito.mock(MealRepository.class);
        Long chatId = 5L;

        Meal breakfast = new Meal(1L, chatId, LocalDateTime.now(), 300, 20, 10, 40, "Завтрак");
        Meal lunch = new Meal(2L, chatId, LocalDateTime.now(), 500, 35, 15, 60, "Обед");
        Mockito.when(repo.getTodayMeals(Mockito.eq(chatId))).thenReturn(List.of(breakfast, lunch));

        MacronutrientResult target = new MacronutrientResult(1800, 120, 50, 180);
        MealTrackingService service = new MealTrackingService(repo);

        DailyProgress progress = service.calculateDailyProgress(chatId, target);
        assertEquals(800, progress.getConsumedCalories());
        assertEquals(55, progress.getConsumedProtein());
        assertEquals(25, progress.getConsumedFats());
        assertEquals(100, progress.getConsumedCarbs());
        assertEquals(1800, progress.getTargetCalories());
        assertEquals(120, progress.getTargetProtein());
    }

    @Test
    void testGetTodayMeals() {
        MealRepository repo = Mockito.mock(MealRepository.class);
        Long chatId = 6L;
        Meal breakfast = new Meal(11L, chatId, LocalDateTime.now(), 250, 18, 8, 30, "Завтрак");
        Mockito.when(repo.getTodayMeals(Mockito.anyLong())).thenReturn(List.of(breakfast));

        MealTrackingService service = new MealTrackingService(repo);
        List<Meal> meals = service.getTodayMeals(chatId);
        assertEquals(1, meals.size());
        assertEquals("Завтрак", meals.get(0).getDescription());
    }
}