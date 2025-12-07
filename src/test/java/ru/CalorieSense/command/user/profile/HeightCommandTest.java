package ru.CalorieSense.command.user.profile;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.InMemoryUserRepository;
import ru.CalorieSense.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

class HeightCommandTest {

    private Update buildUpdate(long chatId, String text) {
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        msg.setText(text);
        Update u = new Update();
        u.setMessage(msg);
        return u;
    }

    @Test
    void whenNoArgument_showsPrompt() throws Exception {
        long chatId = 1L;
        UserRepository repo = new InMemoryUserRepository();
        HeightCommand cmd = new HeightCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/height"));

        assertEquals(chatId, resp.getChatId());
        String text = resp.getText();
        assertTrue(text.contains("Введите ваш рост"));
        assertTrue(text.contains("/height 170"));
        assertNotNull(resp.getReplyMarkup());
    }

    @Test
    void whenValidHeight_updatesProfile() throws Exception {
        long chatId = 2L;
        UserRepository repo = new InMemoryUserRepository();
        repo.save(new UserProfile(chatId));

        HeightCommand cmd = new HeightCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/height 180"));

        UserProfile profile = repo.findByChatId(chatId);
        assertEquals(180, profile.getHeight());

        String text = resp.getText();
        assertTrue(text.contains("обновлён"));
        assertTrue(text.toLowerCase().contains("рост"));
        assertNotNull(resp.getReplyMarkup());
    }

    @Test
    void whenInvalidHeight_returnsError() throws Exception {
        long chatId = 3L;
        UserRepository repo = new InMemoryUserRepository();
        repo.save(new UserProfile(chatId));

        HeightCommand cmd = new HeightCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/height 50")); // вне диапазона

        String text = resp.getText();
        assertTrue(text.contains("Ошибка"));
        assertTrue(text.contains("100-250") || text.contains("100—250"));
        UserProfile profile = repo.findByChatId(chatId);
        assertEquals(0, profile.getHeight());
    }
}
