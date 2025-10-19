package ru.CalorieSense.service.nutrition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.MacronutrientResult;
import ru.CalorieSense.model.UserProfile;

import static org.junit.jupiter.api.Assertions.*;

class CalorieCalculatorServiceTest {

    private CalorieCalculatorService calculator;

    @BeforeEach
    void setUp() {
        calculator = new CalorieCalculatorService();
    }

    @Test
    void testCalculate_MaleWithLowActivity() {
        UserProfile profile = new UserProfile(1L);
        profile.setSex("М");
        profile.setHeight(180);
        profile.setWeight(80);
        profile.setAge(30);
        profile.setActivityLevel("low");

        int calories = calculator.calculate(profile);

        // BMR мужчины: 10*80 + 6.25*180 - 5*30 + 5 = 800 + 1125 - 150 + 5 = 1780
        // С активностью 1.2: 1780 * 1.2 = 2136
        assertEquals(2136, calories);
    }

    @Test
    void testCalculate_FemaleWithHighActivity() {
        UserProfile profile = new UserProfile(1L);
        profile.setSex("Ж");
        profile.setHeight(165);
        profile.setWeight(60);
        profile.setAge(25);
        profile.setActivityLevel("high");

        int calories = calculator.calculate(profile);

        // BMR женщины: 10*60 + 6.25*165 - 5*25 - 161 = 600 + 1031.25 - 125 - 161 = 1345.25
        // С активностью 1.55: 1345.25 * 1.55 = 2085
        assertEquals(2085, calories);
    }

    @Test
    void testCalculate_IncompleteProfile_ReturnsDefault() {
        UserProfile profile = new UserProfile(1L);

        int calories = calculator.calculate(profile);

        assertEquals(2200, calories);
    }

    @Test
    void testCalculateWithMacros_MaintainGoal() {
        UserProfile profile = new UserProfile(1L);
        profile.setSex("М");
        profile.setHeight(175);
        profile.setWeight(75);
        profile.setAge(28);
        profile.setActivityLevel("medium");
        profile.setGoal("maintain");

        MacronutrientResult result = calculator.calculateWithMacros(profile);

        assertNotNull(result);
        assertTrue(result.getCalories() > 0);
        assertTrue(result.getProtein() > 0);
        assertTrue(result.getFats() > 0);
        assertTrue(result.getCarbs() > 0);

        // Проверка соотношения БЖУ для поддержания веса (30/25/45)
        int expectedProtein = (int) Math.round((result.getCalories() * 0.30) / 4);
        assertEquals(expectedProtein, result.getProtein());
    }

    @Test
    void testCalculateWithMacros_LoseWeight() {
        UserProfile profile = new UserProfile(1L);
        profile.setSex("М");
        profile.setHeight(180);
        profile.setWeight(85);
        profile.setAge(30);
        profile.setActivityLevel("low");
        profile.setGoal("lose");

        MacronutrientResult result = calculator.calculateWithMacros(profile);
        int baseCalories = calculator.calculate(profile);

        // Калории для похудения должны быть на 15% меньше базовых
        int expectedCalories = (int) (baseCalories * 0.85);
        assertEquals(expectedCalories, result.getCalories());
    }

    @Test
    void testCalculateWithMacros_GainWeight() {
        UserProfile profile = new UserProfile(1L);
        profile.setSex("М");
        profile.setHeight(175);
        profile.setWeight(70);
        profile.setAge(25);
        profile.setActivityLevel("high");
        profile.setGoal("gain");

        MacronutrientResult result = calculator.calculateWithMacros(profile);
        int baseCalories = calculator.calculate(profile);

        // Калории для набора массы должны быть на 15% больше базовых
        int expectedCalories = (int) (baseCalories * 1.15);
        assertEquals(expectedCalories, result.getCalories());
    }

    @Test
    void testGetActivityLevelText() {
        assertEquals("Малая (без тренировок)", calculator.getActivityLevelText("low"));
        assertEquals("Средняя (1-2 тренировки в неделю)", calculator.getActivityLevelText("medium"));
        assertEquals("Большая (более 2 тренировок в неделю)", calculator.getActivityLevelText("high"));
        assertEquals("Малая (без тренировок)", calculator.getActivityLevelText("invalid"));
    }
}
