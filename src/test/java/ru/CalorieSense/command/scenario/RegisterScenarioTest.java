package ru.CalorieSense.command.scenario;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.scenario.RegisterScenario;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;

class RegisterScenarioTest {

    @Test
    void testScenarioFlow() throws Exception {
        BotApp bot = Mockito.mock(BotApp.class);
        UserRepository repo = Mockito.mock(UserRepository.class);
        UserProfile profile = new UserProfile(1L);
        Mockito.when(repo.findByChatId(1L)).thenReturn(profile);

        RegisterScenario scenario = new RegisterScenario(bot, repo);

        // Шаг 1: начало сценария
        Update start = createUpdate(1L, "/register");
        scenario.execute(start);

        // Шаг 2: ввод роста
        Update height = createUpdate(1L, "180");
        scenario.execute(height);

        // Шаг 3: ввод веса
        Update weight = createUpdate(1L, "75");
        scenario.execute(weight);

        // Шаг 4: ввод возраста
        Update age = createUpdate(1L, "28");
        scenario.execute(age);

        // Шаг 5: ввод пола
        Update sex = createUpdate(1L, "М");
        scenario.execute(sex);

        assertEquals(180, profile.getHeight());
        assertEquals(75.0, profile.getWeight());
        assertEquals(28, profile.getAge());
        assertEquals("М", profile.getSex());
    }

    private Update createUpdate(Long chatId, String text) {
        Message msg = Mockito.mock(Message.class);
        Mockito.when(msg.getChatId()).thenReturn(chatId);
        Mockito.when(msg.getText()).thenReturn(text);

        Update update = new Update();
        update.setMessage(msg);
        return update;
    }
}

