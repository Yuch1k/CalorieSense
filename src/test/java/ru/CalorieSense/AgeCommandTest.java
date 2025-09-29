package ru.CalorieSense.command.feature;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.InMemoryUserRepository;
import ru.CalorieSense.repository.UserRepository;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.junit.jupiter.api.Assertions.*;

public class AgeCommandTest {

    @Test
    void testSetAge() throws Exception {
        BotApp bot = Mockito.mock(BotApp.class);
        UserRepository repo = new InMemoryUserRepository();
        AgeCommand cmd = new AgeCommand(bot, repo);

        Update update = new Update();
        Message msg = Mockito.mock(Message.class);
        Mockito.when(msg.getChatId()).thenReturn(123L);
        Mockito.when(msg.getText()).thenReturn("/age 30");
        update.setMessage(msg);

        cmd.execute(update);

        UserProfile profile = repo.findByChatId(123L);
        assertNotNull(profile);
        assertEquals(40, profile.getAge());
    }
}
