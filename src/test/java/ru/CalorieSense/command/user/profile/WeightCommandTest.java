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

class WeightCommandTest {

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
        WeightCommand cmd = new WeightCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/weight"));

        assertEquals(chatId, resp.getChatId());
        String text = resp.getText();
        assertTrue(text.contains("Введите ваш вес"));
        assertTrue(text.contains("/weight 70"));
        assertNotNull(resp.getReplyMarkup());
    }

    @Test
    void whenValidWeight_updatesProfile() throws Exception {
        long chatId = 2L;
        UserRepository repo = new InMemoryUserRepository();
        repo.save(new UserProfile(chatId));

        WeightCommand cmd = new WeightCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/weight 82.5"));

        UserProfile profile = repo.findByChatId(chatId);
        assertEquals(82.5, profile.getWeight(), 0.001);

        String text = resp.getText();
        assertTrue(text.contains("обновлён"));
        assertTrue(text.toLowerCase().contains("вес"));
        assertNotNull(resp.getReplyMarkup());
    }

    @Test
    void whenInvalidWeight_returnsError() throws Exception {
        long chatId = 3L;
        UserRepository repo = new InMemoryUserRepository();
        repo.save(new UserProfile(chatId));

        WeightCommand cmd = new WeightCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/weight 10")); // меньше 30

        String text = resp.getText();
        assertTrue(text.contains("Ошибка"));
        assertTrue(text.contains("30"));
        UserProfile profile = repo.findByChatId(chatId);
        assertEquals(0.0, profile.getWeight(), 0.001);
    }
}
