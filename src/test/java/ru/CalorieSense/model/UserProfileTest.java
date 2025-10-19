package ru.CalorieSense.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    @Test
    void testDefaultConstructor() {
        UserProfile profile = new UserProfile(123L);

        assertEquals(123L, profile.getChatId());
        assertEquals(2200, profile.getCaloriesGoal());
        assertEquals("maintain", profile.getGoal());
        assertEquals("low", profile.getActivityLevel());
        assertEquals(0, profile.getHeight());
        assertEquals(0, profile.getWeight());
        assertEquals(0, profile.getAge());
        assertNull(profile.getSex());
    }

    @Test
    void testFullConstructor() {
        UserProfile profile = new UserProfile(123L, 75.5, 180, "М", 30);

        assertEquals(123L, profile.getChatId());
        assertEquals(75.5, profile.getWeight());
        assertEquals(180, profile.getHeight());
        assertEquals("М", profile.getSex());
        assertEquals(30, profile.getAge());
        assertEquals("maintain", profile.getGoal());
        assertEquals("low", profile.getActivityLevel());
    }

    @Test
    void testIsComplete_AllFieldsFilled() {
        UserProfile profile = new UserProfile(123L);
        profile.setWeight(75);
        profile.setHeight(180);
        profile.setAge(30);
        profile.setSex("М");

        assertTrue(profile.isComplete());
    }

    @Test
    void testIsComplete_MissingWeight() {
        UserProfile profile = new UserProfile(123L);
        profile.setHeight(180);
        profile.setAge(30);
        profile.setSex("М");

        assertFalse(profile.isComplete());
    }

    @Test
    void testIsComplete_MissingHeight() {
        UserProfile profile = new UserProfile(123L);
        profile.setWeight(75);
        profile.setAge(30);
        profile.setSex("М");

        assertFalse(profile.isComplete());
    }

    @Test
    void testIsComplete_MissingAge() {
        UserProfile profile = new UserProfile(123L);
        profile.setWeight(75);
        profile.setHeight(180);
        profile.setSex("М");

        assertFalse(profile.isComplete());
    }

    @Test
    void testIsComplete_MissingSex() {
        UserProfile profile = new UserProfile(123L);
        profile.setWeight(75);
        profile.setHeight(180);
        profile.setAge(30);

        assertFalse(profile.isComplete());
    }

    @Test
    void testIsComplete_BlankSex() {
        UserProfile profile = new UserProfile(123L);
        profile.setWeight(75);
        profile.setHeight(180);
        profile.setAge(30);
        profile.setSex("   ");

        assertFalse(profile.isComplete());
    }

    @Test
    void testSettersAndGetters() {
        UserProfile profile = new UserProfile(123L);

        profile.setWeight(80.5);
        profile.setHeight(185);
        profile.setAge(35);
        profile.setSex("Ж");
        profile.setGoal("lose");
        profile.setActivityLevel("high");
        profile.setCaloriesGoal(2500);

        assertEquals(80.5, profile.getWeight());
        assertEquals(185, profile.getHeight());
        assertEquals(35, profile.getAge());
        assertEquals("Ж", profile.getSex());
        assertEquals("lose", profile.getGoal());
        assertEquals("high", profile.getActivityLevel());
        assertEquals(2500, profile.getCaloriesGoal());
    }

    @Test
    void testToString() {
        UserProfile profile = new UserProfile(123L, 75.5, 180, "М", 30);
        profile.setGoal("maintain");
        profile.setActivityLevel("medium");

        String result = profile.toString();

        assertTrue(result.contains("123"));
        assertTrue(result.contains("75.5"));
        assertTrue(result.contains("180"));
        assertTrue(result.contains("30"));
        assertTrue(result.contains("М"));
    }
}
