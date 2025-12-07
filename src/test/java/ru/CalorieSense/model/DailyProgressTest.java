package ru.CalorieSense.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DailyProgressTest {

    @Test
    void constructorStoresAllFields() {
        DailyProgress dp = new DailyProgress(
                900, 50, 30, 100,
                1800, 100, 60, 200
        );

        assertEquals(900, dp.getConsumedCalories());
        assertEquals(50, dp.getConsumedProtein());
        assertEquals(30, dp.getConsumedFats());
        assertEquals(100, dp.getConsumedCarbs());

        assertEquals(1800, dp.getTargetCalories());
        assertEquals(100, dp.getTargetProtein());
        assertEquals(60, dp.getTargetFats());
        assertEquals(200, dp.getTargetCarbs());
    }

    @Test
    void remainingValuesCalculatedCorrectly() {
        DailyProgress dp = new DailyProgress(
                900, 50, 30, 100,
                1800, 100, 60, 200
        );

        assertEquals(900, dp.getRemainingCalories());
        assertEquals(50, dp.getRemainingProtein());
        assertEquals(30, dp.getRemainingFats());
        assertEquals(100, dp.getRemainingCarbs());
    }

    @Test
    void caloriesPercentageUsesTargets() {
        DailyProgress dp = new DailyProgress(
                900, 0, 0, 0,
                1800, 0, 0, 0
        );

        assertEquals(50.0, dp.getCaloriesPercentage(), 0.0001);
    }

    @Test
    void caloriesPercentageZeroWhenNoTarget() {
        DailyProgress dp = new DailyProgress(
                900, 0, 0, 0,
                0, 0, 0, 0
        );

        assertEquals(0.0, dp.getCaloriesPercentage(), 0.0001);
    }
}
