package ru.CalorieSense.service.nutrition;

import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.MacronutrientResult;
import ru.CalorieSense.model.UserProfile;

import static org.junit.jupiter.api.Assertions.*;

class CalorieCalculatorServiceTest {

    @Test
    void returnsDefaultForIncompleteProfile() {
        CalorieCalculatorService service = new CalorieCalculatorService();
        UserProfile profile = new UserProfile(1L);

        int calories = service.calculate(profile);

        assertEquals(2200, calories);
    }

    @Test
    void calculatesForMaleMediumActivity() {
        CalorieCalculatorService service = new CalorieCalculatorService();
        UserProfile profile = new UserProfile(2L);
        profile.setWeight(80);
        profile.setHeight(180);
        profile.setAge(30);
        profile.setSex("male");
        profile.setActivityLevel("medium");

        int calories = service.calculate(profile);

        assertTrue(calories > 0);
    }

    @Test
    void calculateWithMacrosReturnsPositiveValues() {
        CalorieCalculatorService service = new CalorieCalculatorService();
        UserProfile profile = new UserProfile(3L);
        profile.setWeight(70);
        profile.setHeight(175);
        profile.setAge(28);
        profile.setSex("female");
        profile.setActivityLevel("low");
        profile.setGoal("lose");

        MacronutrientResult result = service.calculateWithMacros(profile);

        assertTrue(result.getCalories() > 0);
        assertTrue(result.getProtein() > 0);
        assertTrue(result.getFats() > 0);
        assertTrue(result.getCarbs() > 0);
    }

    @Test
    void activityLevelTextMapping() {
        CalorieCalculatorService service = new CalorieCalculatorService();

        assertTrue(service.getActivityLevelText("low").toLowerCase().contains("без тренировок"));
        assertTrue(service.getActivityLevelText("medium").toLowerCase().contains("1-2"));
        assertTrue(service.getActivityLevelText("high").toLowerCase().contains("2"));
        assertEquals(service.getActivityLevelText("low"), service.getActivityLevelText("unknown"));
    }
}
