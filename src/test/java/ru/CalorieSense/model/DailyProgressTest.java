package ru.CalorieSense.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DailyProgressTest {

    @Test
    void testConstructor() {
        DailyProgress progress = new DailyProgress(1500, 100, 40, 150, 2000, 150, 55, 225);

        assertEquals(1500, progress.getConsumedCalories());
        assertEquals(100, progress.getConsumedProtein());
        assertEquals(40, progress.getConsumedFats());
        assertEquals(150, progress.getConsumedCarbs());
        assertEquals(2000, progress.getTargetCalories());
        assertEquals(150, progress.getTargetProtein());
        assertEquals(55, progress.getTargetFats());
        assertEquals(225, progress.getTargetCarbs());
    }

    @Test
    void testGetRemainingCalories() {
        DailyProgress progress = new DailyProgress(1500, 100, 40, 150, 2000, 150, 55, 225);

        assertEquals(500, progress.getRemainingCalories());
    }

    @Test
    void testGetRemainingProtein() {
        DailyProgress progress = new DailyProgress(1500, 100, 40, 150, 2000, 150, 55, 225);

        assertEquals(50, progress.getRemainingProtein());
    }

    @Test
    void testGetRemainingFats() {
        DailyProgress progress = new DailyProgress(1500, 100, 40, 150, 2000, 150, 55, 225);

        assertEquals(15, progress.getRemainingFats());
    }

    @Test
    void testGetRemainingCarbs() {
        DailyProgress progress = new DailyProgress(1500, 100, 40, 150, 2000, 150, 55, 225);

        assertEquals(75, progress.getRemainingCarbs());
    }

    @Test
    void testGetCaloriesPercentage() {
        DailyProgress progress = new DailyProgress(1500, 100, 40, 150, 2000, 150, 55, 225);

        assertEquals(75.0, progress.getCaloriesPercentage(), 0.01);
    }

    @Test
    void testGetCaloriesPercentage_ZeroTarget() {
        DailyProgress progress = new DailyProgress(1500, 100, 40, 150, 0, 150, 55, 225);

        assertEquals(0.0, progress.getCaloriesPercentage(), 0.01);
    }

    @Test
    void testGetCaloriesPercentage_OverTarget() {
        DailyProgress progress = new DailyProgress(2500, 180, 70, 300, 2000, 150, 55, 225);

        assertEquals(125.0, progress.getCaloriesPercentage(), 0.01);
    }

    @Test
    void testGetRemainingCalories_NegativeWhenOverTarget() {
        DailyProgress progress = new DailyProgress(2500, 180, 70, 300, 2000, 150, 55, 225);

        assertEquals(-500, progress.getRemainingCalories());
    }
}
