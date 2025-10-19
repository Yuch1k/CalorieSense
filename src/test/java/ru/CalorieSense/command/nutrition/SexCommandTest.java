package ru.CalorieSense.command.nutrition;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.user.profile.SexCommand;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;

class SexCommandTest {

    @Test
    void testSetSexSuccessfully() throws Exception {
        BotApp bot = Mockito.mock(BotApp.class);
        UserRepository repo = Mockito.mock(UserRepository.class);

        UserProfile profile = new UserProfile(1L);
        Mockito.when(repo.findByChatId(1L)).thenReturn(profile);

        Message msg = Mockito.mock(Message.class);
        Mockito.when(msg.getChatId()).thenReturn(1L);
        Mockito.when(msg.getText()).thenReturn("/sex Ж");

        Update update = new Update();
        update.setMessage(msg);

        SexCommand cmd = new SexCommand(bot, repo);
        cmd.execute(update);

        assertEquals("Ж", profile.getSex());
    }
}
