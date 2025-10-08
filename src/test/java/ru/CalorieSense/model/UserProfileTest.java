package ru.CalorieSense.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    @Test
    void testInitializationAndGetters() {
        UserProfile p = new UserProfile(1L, 70.0, 180, "М", 25);
        assertEquals(1L, p.getChatId());
        assertEquals(70.0, p.getWeight());
        assertEquals(180, p.getHeight());
        assertEquals("М", p.getSex());
        assertEquals(25, p.getAge());
    }

    @Test
    void testUpdateFields() {
        UserProfile p = new UserProfile(1L);
        p.setHeight(180);
        p.setWeight(75);
        p.setAge(30);
        p.setSex("М");

        assertEquals(180, p.getHeight());
        assertEquals(75.0, p.getWeight());
        assertEquals(30, p.getAge());
        assertEquals("М", p.getSex());
    }
}
