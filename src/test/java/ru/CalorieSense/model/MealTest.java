package ru.CalorieSense.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MealTest {

    @Test
    void testConstructor() {
        LocalDateTime now = LocalDateTime.now();

        Meal meal = new Meal(1L, 123L, now, 500, 30, 15, 50, "Breakfast");

        assertEquals(1L, meal.getId());
        assertEquals(123L, meal.getChatId());
        assertEquals(now, meal.getTimestamp());
        assertEquals(500, meal.getCalories());
        assertEquals(30, meal.getProtein());
        assertEquals(15, meal.getFats());
        assertEquals(50, meal.getCarbs());
        assertEquals("Breakfast", meal.getDescription());
    }

    @Test
    void testGetters() {
        LocalDateTime timestamp = LocalDateTime.of(2025, 10, 19, 12, 30);
        Meal meal = new Meal(5L, 456L, timestamp, 700, 40, 25, 80, "Lunch");

        assertEquals(5L, meal.getId());
        assertEquals(456L, meal.getChatId());
        assertEquals(timestamp, meal.getTimestamp());
        assertEquals(700, meal.getCalories());
        assertEquals(40, meal.getProtein());
        assertEquals(25, meal.getFats());
        assertEquals(80, meal.getCarbs());
        assertEquals("Lunch", meal.getDescription());
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();
        Meal meal = new Meal(1L, 123L, now, 500, 30, 15, 50, "Test");

        String str = meal.toString();

        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("chatId=123"));
        assertTrue(str.contains("cal=500"));
        assertTrue(str.contains("P=30г"));
        assertTrue(str.contains("F=15г"));
        assertTrue(str.contains("C=50г"));
    }

    @Test
    void testWithNullDescription() {
        LocalDateTime now = LocalDateTime.now();

        Meal meal = new Meal(1L, 123L, now, 500, 30, 15, 50, null);

        assertNull(meal.getDescription());
        assertEquals(500, meal.getCalories());
    }
}
