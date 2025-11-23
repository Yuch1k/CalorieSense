package ru.CalorieSense.model;
import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.UserProfile;

import static org.junit.jupiter.api.Assertions.*;

public class UserProfileTest {

    @Test
    void testConstructorAndDefaultValues() {
        UserProfile profile = new UserProfile(100L);
        assertEquals(100L, profile.getChatId());
        assertEquals(2200, profile.getCaloriesGoal());
        assertEquals("maintain", profile.getGoal());
        assertEquals("low", profile.getActivityLevel());
    }

    @Test
    void testSettersAndGetters() {
        UserProfile profile = new UserProfile(200L);
        profile.setWeight(85.6);
        profile.setHeight(185);
        profile.setAge(40);
        profile.setSex("female");
        profile.setCaloriesGoal(1700);
        profile.setGoal("lose");
        profile.setActivityLevel("medium");

        assertEquals(85.6, profile.getWeight());
        assertEquals(185, profile.getHeight());
        assertEquals(40, profile.getAge());
        assertEquals("female", profile.getSex());
        assertEquals(1700, profile.getCaloriesGoal());
        assertEquals("lose", profile.getGoal());
        assertEquals("medium", profile.getActivityLevel());
    }

    @Test
    void testIsCompleteTrue() {
        UserProfile profile = new UserProfile(300L);
        profile.setWeight(55.9);
        profile.setHeight(168);
        profile.setAge(23);
        profile.setSex("male");
        assertTrue(profile.isComplete());
    }

    @Test
    void testIsCompleteFalse() {
        UserProfile profile = new UserProfile(400L);
        profile.setHeight(175);
        profile.setSex("male");
        // weight = 0, age = 0
        assertFalse(profile.isComplete());
    }
}
