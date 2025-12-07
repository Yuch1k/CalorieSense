package ru.CalorieSense.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MealTest {

    @Test
    void constructorAndGettersWork() {
        LocalDateTime now = LocalDateTime.now();
        Meal meal = new Meal(10L, 100L, now, 500, 30, 20, 60, "Обед");

        assertEquals(10L, meal.getId());
        assertEquals(100L, meal.getChatId());
        assertEquals(now, meal.getDateTime());
        assertEquals(500, meal.getCalories());
        assertEquals(30, meal.getProtein());
        assertEquals(20, meal.getFats());
        assertEquals(60, meal.getCarbs());
        assertEquals("Обед", meal.getDescription());
    }

    @Test
    void settersChangeEditableFields() {
        LocalDateTime now = LocalDateTime.now();
        Meal meal = new Meal(11L, 101L, now, 300, 10, 5, 40, "Перекус");

        meal.setCalories(350);
        meal.setProtein(15);
        meal.setFats(8);
        meal.setCarbs(45);
        meal.setDescription("Новый перекус");

        assertEquals(350, meal.getCalories());
        assertEquals(15, meal.getProtein());
        assertEquals(8, meal.getFats());
        assertEquals(45, meal.getCarbs());
        assertEquals("Новый перекус", meal.getDescription());
    }
}
