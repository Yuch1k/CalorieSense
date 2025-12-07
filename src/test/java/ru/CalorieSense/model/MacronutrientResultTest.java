package ru.CalorieSense.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MacronutrientResultTest {

    @Test
    void constructorStoresAllFields() {
        MacronutrientResult result = new MacronutrientResult(2000, 120, 70, 250);

        assertEquals(2000, result.getCalories());
        assertEquals(120, result.getProtein());
        assertEquals(70, result.getFats());
        assertEquals(250, result.getCarbs());
    }

    @Test
    void constructorStoresAllZeros() {
        MacronutrientResult result = new MacronutrientResult(0,0,0,0);

        assertEquals(0, result.getCalories());
        assertEquals(0, result.getProtein());
        assertEquals(0, result.getFats());
        assertEquals(0, result.getCarbs());
    }
}
