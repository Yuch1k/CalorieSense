package ru.CalorieSense.model;

import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.Meal;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MealTest {

    @Test
    void testConstructorAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        Meal meal = new Meal(10L, 777L, now, 520, 30, 16, 70, "Тестовое блюдо");

        assertEquals(10L, meal.getId());
        assertEquals(777L, meal.getChatId());
        assertEquals(now, meal.getDateTime());
        assertEquals(520, meal.getCalories());
        assertEquals(30, meal.getProtein());
        assertEquals(16, meal.getFats());
        assertEquals(70, meal.getCarbs());
        assertEquals("Тестовое блюдо", meal.getDescription());
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
