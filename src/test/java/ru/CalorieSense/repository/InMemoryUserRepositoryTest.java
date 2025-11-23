package ru.CalorieSense.repository;

import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.InMemoryUserRepository;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    @Test
    void testFindByChatIdCreatesProfile() {
        InMemoryUserRepository repo = new InMemoryUserRepository();
        UserProfile profile = repo.findByChatId(123L);
        assertNotNull(profile);
        assertEquals(123L, profile.getChatId());
    }

    @Test
    void testSaveAndRetrieveProfile() {
        InMemoryUserRepository repo = new InMemoryUserRepository();
        UserProfile profile = new UserProfile(555L);
        profile.setWeight(90.4);
        profile.setHeight(188);
        repo.save(profile);
        UserProfile found = repo.findByChatId(555L);
        assertEquals(90.4, found.getWeight());
        assertEquals(188, found.getHeight());
    }

    @Test
    void testUpdateWeight() {
        InMemoryUserRepository repo = new InMemoryUserRepository();
        UserProfile p = repo.findByChatId(77L);
        repo.updateWeight(77L, 72.4);
        UserProfile updated = repo.findByChatId(77L);
        assertEquals(72.4, updated.getWeight());
    }

    @Test
    void testUpdateGoalAndActivity() {
        InMemoryUserRepository repo = new InMemoryUserRepository();
        repo.updateGoal(22L, "gain");
        repo.updateActivityLevel(22L, "medium");
        UserProfile profile = repo.findByChatId(22L);
        assertEquals("gain", profile.getGoal());
        assertEquals("medium", profile.getActivityLevel());
    }
}