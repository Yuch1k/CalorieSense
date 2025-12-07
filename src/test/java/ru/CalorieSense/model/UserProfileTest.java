package ru.CalorieSense.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    @Test
    void isCompleteWhenAllFieldsFilled() {
        UserProfile profile = new UserProfile(1L);
        profile.setWeight(80);
        profile.setHeight(180);
        profile.setAge(30);
        profile.setSex("male");

        assertTrue(profile.isComplete());
    }

    @Test
    void isCompleteWhenMissingRequiredFields() {
        UserProfile profile = new UserProfile(2L);
        profile.setHeight(180);
        profile.setSex("male");

        assertFalse(profile.isComplete());
    }

    @Test
    void defaultValuesAfterConstruction() {
        UserProfile profile = new UserProfile(3L);

        assertEquals(3L, profile.getChatId());
        assertEquals("maintain", profile.getGoal());
        assertEquals("low", profile.getActivityLevel());
        assertEquals(2200, profile.getCaloriesGoal());
    }
}
