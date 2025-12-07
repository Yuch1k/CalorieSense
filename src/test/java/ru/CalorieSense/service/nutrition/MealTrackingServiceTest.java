package ru.CalorieSense.service.nutrition;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.CalorieSense.model.DailyProgress;
import ru.CalorieSense.model.MacronutrientResult;
import ru.CalorieSense.model.Meal;
import ru.CalorieSense.repository.MealRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MealTrackingServiceTest {

    @Test
    void calculateDailyProgressSumsMealsForDate() {
        MealRepository repo = Mockito.mock(MealRepository.class);
        MealTrackingService service = new MealTrackingService(repo);

        Long chatId = 1L;
        LocalDate date = LocalDate.of(2025, 12, 7);

        Meal m1 = new Meal(1L, chatId, date.atTime(9, 0), 300, 20, 10, 40, "Завтрак");
        Meal m2 = new Meal(2L, chatId, date.atTime(13, 0), 500, 30, 15, 60, "Обед");

        Mockito.when(repo.getMealsByDate(chatId, date)).thenReturn(List.of(m1, m2));

        MacronutrientResult target = new MacronutrientResult(2000, 120, 70, 250);

        DailyProgress progress = service.calculateDailyProgress(chatId, date, target);

        assertEquals(800, progress.getConsumedCalories());
        assertEquals(50, progress.getConsumedProtein());
        assertEquals(25, progress.getConsumedFats());
        assertEquals(100, progress.getConsumedCarbs());
        assertEquals(2000, progress.getTargetCalories());
    }
}
