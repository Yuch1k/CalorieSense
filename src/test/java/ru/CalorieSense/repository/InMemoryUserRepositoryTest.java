package ru.CalorieSense.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.UserProfile;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
    }

    @Test
    void testFindByChatId_CreatesNewProfile() {
        UserProfile profile = repository.findByChatId(123L);

        assertNotNull(profile);
        assertEquals(123L, profile.getChatId());
        assertEquals(2200, profile.getCaloriesGoal());
        assertEquals("maintain", profile.getGoal());
        assertEquals("low", profile.getActivityLevel());
    }

    @Test
    void testFindByChatId_ReturnsSameProfile() {
        UserProfile profile1 = repository.findByChatId(123L);
        profile1.setWeight(75);

        UserProfile profile2 = repository.findByChatId(123L);

        assertSame(profile1, profile2);
        assertEquals(75, profile2.getWeight());
    }

    @Test
    void testSave() {
        UserProfile profile = new UserProfile(123L);
        profile.setHeight(180);
        profile.setWeight(75);

        repository.save(profile);
        UserProfile retrieved = repository.findByChatId(123L);

        assertEquals(180, retrieved.getHeight());
        assertEquals(75, retrieved.getWeight());
    }

    @Test
    void testUpdateWeight() {
        repository.findByChatId(123L); // Создаем профиль

        repository.updateWeight(123L, 80.5);
        UserProfile profile = repository.findByChatId(123L);

        assertEquals(80.5, profile.getWeight());
    }

    @Test
    void testUpdateHeight() {
        repository.findByChatId(123L);

        repository.updateHeight(123L, 175);
        UserProfile profile = repository.findByChatId(123L);

        assertEquals(175, profile.getHeight());
    }

    @Test
    void testUpdateAge() {
        repository.findByChatId(123L);

        repository.updateAge(123L, 30);
        UserProfile profile = repository.findByChatId(123L);

        assertEquals(30, profile.getAge());
    }

    @Test
    void testUpdateSex() {
        repository.findByChatId(123L);

        repository.updateSex(123L, "М");
        UserProfile profile = repository.findByChatId(123L);

        assertEquals("М", profile.getSex());
    }
}