package ru.CalorieSense.command.feature;

import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.UserProfile;

import static org.junit.jupiter.api.Assertions.*;

class CalorieCalculatorServiceTest {

    private final CalorieCalculatorService service = new CalorieCalculatorService();

    @Test
    void testMaleCalculation() {
        UserProfile user = new UserProfile(1L, 70.0, 175, "М", 25);
        assertEquals(2009, service.calculate(user));
    }

    @Test
    void testFemaleCalculation() {
        UserProfile user = new UserProfile(2L, 60.0, 165, "Ж", 30);
        assertEquals(1584, service.calculate(user));
    }

    @Test
    void testDefaultValueWhenDataMissing() {
        UserProfile user = new UserProfile(3L);
        assertEquals(2200, service.calculate(user));
    }
}
