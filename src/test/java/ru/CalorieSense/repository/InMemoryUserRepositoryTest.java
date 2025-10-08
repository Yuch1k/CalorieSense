package ru.CalorieSense.repository;

import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.UserProfile;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    @Test
    void testCreateAndRetrieveUser() {
        InMemoryUserRepository repo = new InMemoryUserRepository();
        UserProfile profile = repo.findByChatId(1L);

        assertNotNull(profile);
        assertEquals(1L, profile.getChatId());
    }

    @Test
    void testUpdateAndPersist() {
        InMemoryUserRepository repo = new InMemoryUserRepository();
        UserProfile p = repo.findByChatId(1L);

        repo.updateWeight(1L, 70);
        repo.updateHeight(1L, 180);
        repo.updateAge(1L, 30);
        repo.updateSex(1L, "М");

        UserProfile updated = repo.findByChatId(1L);
        assertEquals(70, updated.getWeight());
        assertEquals(180, updated.getHeight());
        assertEquals(30, updated.getAge());
        assertEquals("М", updated.getSex());
    }
}
