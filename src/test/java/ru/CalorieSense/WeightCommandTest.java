package ru.CalorieSense.command.feature;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.repository.InMemoryUserRepository;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.model.UserProfile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.junit.jupiter.api.Assertions.*;

public class WeightCommandTest {
    @Test
    void testSetWeight() throws Exception {
        BotApp bot = Mockito.mock(BotApp.class);
        UserRepository repo = new InMemoryUserRepository();
        WeightCommand cmd = new WeightCommand(bot, repo);

        Update update = new Update();
        Message msg = Mockito.mock(Message.class);
        Mockito.when(msg.getChatId()).thenReturn(123L);
        Mockito.when(msg.getText()).thenReturn("/weight 70");
        update.setMessage(msg);

        cmd.execute(update);

        UserProfile profile = repo.findByChatId(123L);
        assertNotNull(profile);
        assertEquals(180, profile.getWeight());
    }
}
