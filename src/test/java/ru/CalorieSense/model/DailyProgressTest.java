package ru.CalorieSense.model;

import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.DailyProgress;

import static org.junit.jupiter.api.Assertions.*;

class DailyProgressTest {

    @Test
    void testGetters() {
        DailyProgress progress = new DailyProgress(
                1200, 80, 30, 150,
                1800, 120, 50, 200
        );
        assertEquals(1200, progress.getConsumedCalories());
        assertEquals(80, progress.getConsumedProtein());
        assertEquals(30, progress.getConsumedFats());
        assertEquals(150, progress.getConsumedCarbs());
        assertEquals(1800, progress.getTargetCalories());
        assertEquals(120, progress.getTargetProtein());
        assertEquals(50, progress.getTargetFats());
        assertEquals(200, progress.getTargetCarbs());
    }

    @Test
    void testRemainingValues() {
        DailyProgress progress = new DailyProgress(
                500, 20, 10, 60,
                2000, 80, 40, 180
        );
        assertEquals(1500, progress.getRemainingCalories());
        assertEquals(60, progress.getRemainingProtein());
        assertEquals(30, progress.getRemainingFats());
        assertEquals(120, progress.getRemainingCarbs());
    }

    @Test
    void testPercentage() {
        DailyProgress progress = new DailyProgress(
                900, 60, 20, 110,
                1800, 120, 40, 200
        );
        assertEquals(50.0, progress.getCaloriesPercentage());
    }
}
