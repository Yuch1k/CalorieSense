package ru.CalorieSense.service.nutrition;

import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.model.MacronutrientResult;
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;

import static org.junit.jupiter.api.Assertions.*;

class CalorieCalculatorServiceTest {

    @Test
    void testDefaultCaloriesIfProfileIncomplete() {
        CalorieCalculatorService service = new CalorieCalculatorService();
        UserProfile profile = new UserProfile(1L);
        assertEquals(2200, service.calculate(profile));
    }

    @Test
    void testCalculateMale() {
        CalorieCalculatorService service = new CalorieCalculatorService();
        UserProfile profile = new UserProfile(2L);
        profile.setWeight(80);
        profile.setHeight(180);
        profile.setAge(25);
        profile.setSex("male");
        profile.setActivityLevel("medium");
        int calories = service.calculate(profile);
        assertTrue(calories > 0);
    }

    @Test
    void testCalculateFemale() {
        CalorieCalculatorService service = new CalorieCalculatorService();
        UserProfile profile = new UserProfile(3L);
        profile.setWeight(65);
        profile.setHeight(165);
        profile.setAge(30);
        profile.setSex("female");
        profile.setActivityLevel("medium");
        int calories = service.calculate(profile);
        assertTrue(calories > 0);
    }

    @Test
    void testCalculateWithMacros_Lose() {
        CalorieCalculatorService service = new CalorieCalculatorService();
        UserProfile profile = new UserProfile(4L);
        profile.setWeight(70);
        profile.setHeight(170);
        profile.setAge(28);
        profile.setSex("male");
        profile.setActivityLevel("low");
        profile.setGoal("lose");
        MacronutrientResult result = service.calculateWithMacros(profile);
        assertEquals("lose", profile.getGoal());
        assertTrue(result.getCalories() > 0);
        assertTrue(result.getProtein() > 0);
        assertTrue(result.getFats() > 0);
        assertTrue(result.getCarbs() > 0);
    }

    @Test
    void testActivityLevelText() {
        CalorieCalculatorService service = new CalorieCalculatorService();
        assertEquals("Малая (без тренировок)", service.getActivityLevelText("low"));
        assertEquals("Средняя (1-2 тренировки в неделю)", service.getActivityLevelText("medium"));
        assertEquals("Большая (более 2 тренировок в неделю)", service.getActivityLevelText("high"));
        assertEquals("Малая (без тренировок)", service.getActivityLevelText("unknown"));
    }
}
