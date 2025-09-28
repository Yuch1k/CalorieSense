package ru.CalorieSense.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserProfileTest {

    @Test
    void testCaloriesCalculationMale() {
        UserProfile u = new UserProfile();
        u.setWeight(97);
        u.setHeight(186);
        u.setGender("М");
        u.setAge(19);

        assertTrue(u.getCaloriesGoal() > 1500);
        assertTrue(u.getCaloriesGoal() < 3000);
    }

    @Test
    void testCaloriesCalculationFemale() {
        UserProfile u = new UserProfile();
        u.setWeight(60);
        u.setHeight(165);
        u.setGender("Ж");
        u.setAge(30);

        assertTrue(u.getCaloriesGoal() > 1200);
        assertTrue(u.getCaloriesGoal() < 2500);
    }
}
