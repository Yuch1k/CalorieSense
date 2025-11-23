package ru.CalorieSense.model;

import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.MacronutrientResult;

import static org.junit.jupiter.api.Assertions.*;

class MacronutrientResultTest {

    @Test
    void testConstructor() {
        MacronutrientResult result = new MacronutrientResult(2000, 150, 55, 225);

        assertEquals(2000, result.getCalories());
        assertEquals(150, result.getProtein());
        assertEquals(55, result.getFats());
        assertEquals(225, result.getCarbs());
    }

    @Test
    void testGetters() {
        MacronutrientResult result = new MacronutrientResult(2500, 180, 70, 280);

        assertEquals(2500, result.getCalories());
        assertEquals(180, result.getProtein());
        assertEquals(70, result.getFats());
        assertEquals(280, result.getCarbs());
    }

    @Test
    void testToString() {
        MacronutrientResult result = new MacronutrientResult(2000, 150, 55, 225);

        String str = result.toString();

        assertTrue(str.contains("2000"));
        assertTrue(str.contains("150"));
        assertTrue(str.contains("55"));
        assertTrue(str.contains("225"));
    }

    @Test
    void testWithZeroValues() {
        MacronutrientResult result = new MacronutrientResult(0, 0, 0, 0);

        assertEquals(0, result.getCalories());
        assertEquals(0, result.getProtein());
        assertEquals(0, result.getFats());
        assertEquals(0, result.getCarbs());
    }
}
