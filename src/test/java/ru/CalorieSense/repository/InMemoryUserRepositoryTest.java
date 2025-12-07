package ru.CalorieSense.repository;

import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.UserProfile;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    @Test
    void findByChatIdCreatesProfileIfMissing() {
        InMemoryUserRepository repo = new InMemoryUserRepository();

        UserProfile p1 = repo.findByChatId(1L);
        UserProfile p2 = repo.findByChatId(1L);

        assertNotNull(p1);
        assertSame(p1, p2);
        assertEquals(1L, p1.getChatId());
    }

    @Test
    void saveUserProfileOverridesExisting() {
        InMemoryUserRepository repo = new InMemoryUserRepository();
        UserProfile profile = new UserProfile(2L);
        profile.setWeight(80);
        repo.save(profile);

        UserProfile loaded = repo.findByChatId(2L);
        assertEquals(80, loaded.getWeight());
    }

    @Test
    void updateMethodsChangeFields() {
        InMemoryUserRepository repo = new InMemoryUserRepository();
        Long chatId = 3L;
        repo.updateWeight(chatId, 75.5);
        repo.updateHeight(chatId, 182);
        repo.updateAge(chatId, 29);
        repo.updateSex(chatId, "female");
        repo.updateGoal(chatId, "lose");
        repo.updateActivityLevel(chatId, "medium");

        UserProfile profile = repo.findByChatId(chatId);
        assertEquals(75.5, profile.getWeight());
        assertEquals(182, profile.getHeight());
        assertEquals(29, profile.getAge());
        assertEquals("female", profile.getSex());
        assertEquals("lose", profile.getGoal());
        assertEquals("medium", profile.getActivityLevel());
    }
}
