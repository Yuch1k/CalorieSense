package ru.CalorieSense.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    @Test
    void testMaleCaloriesCalculation() {
        UserProfile profile = new UserProfile(1L,70.0, 175, "М", 25);
        int calories = profile.getCaloriesGoal();

        // BMR = 10*70 + 6.25*175 - 5*25 + 5 = 1673.75
        // TDEE = BMR * 1.2 ≈ 2009
        assertEquals(2009, calories);
    }

    @Test
    void testFemaleCaloriesCalculation() {
        UserProfile profile = new UserProfile(2L,60.0, 165, "Ж", 30);
        int calories = profile.getCaloriesGoal();

        // BMR = 10*60 + 6.25*165 - 5*30 - 161 = 1320.25
        // TDEE = 1320.25 * 1.2 ≈ 1584
        assertEquals(1584, calories);
    }

    @Test
    void testUpdateWeightRecalculatesCalories() {
        UserProfile profile = new UserProfile(3L,60.0, 165, "Ж", 30);
        int oldCalories = profile.getCaloriesGoal();

        profile.setWeight(65.0);
        int newCalories = profile.getCaloriesGoal();

        assertTrue(newCalories > oldCalories);
    }

    @Test
    void testUpdateAgeRecalculatesCalories() {
        UserProfile profile = new UserProfile(4L,80.0, 180, "Ж", 25);
        int calories25 = profile.getCaloriesGoal();

        profile.setAge(35);
        int calories35 = profile.getCaloriesGoal();

        assertTrue(calories35 < calories25);
    }


}
