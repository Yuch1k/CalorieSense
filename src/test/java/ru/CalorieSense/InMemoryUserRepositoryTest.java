package ru.CalorieSense.repository;

import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.UserProfile;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserRepositoryTest {

    @Test
    void testSaveAndFind() {
        UserRepository repo = new InMemoryUserRepository();
        Long chatId = 123L;

        repo.updateWeight(chatId, 80);
        repo.updateHeight(chatId, 180);
        repo.updateSex(chatId, "М");

        UserProfile p = repo.findByChatId(chatId);

        assertNotNull(p);
        assertEquals(80, p.getWeight());
        assertEquals(180, p.getHeight());
        assertEquals("М", p.getSex());
    }
}
