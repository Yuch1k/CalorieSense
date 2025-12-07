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

class AgeCommandTest {

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
        AgeCommand cmd = new AgeCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/age"));

        assertEquals(chatId, resp.getChatId());
        String text = resp.getText();
        assertTrue(text.contains("Введите ваш возраст"));
        assertTrue(text.contains("/age 25"));
        assertNotNull(resp.getReplyMarkup());
    }

    @Test
    void whenValidAge_updatesProfile() throws Exception {
        long chatId = 2L;
        UserRepository repo = new InMemoryUserRepository();
        repo.save(new UserProfile(chatId));

        AgeCommand cmd = new AgeCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/age 30"));

        UserProfile profile = repo.findByChatId(chatId);
        assertEquals(30, profile.getAge());

        String text = resp.getText();
        // AbstractParameterCommand формирует общий текст вида "✅ Возраст обновлён!"
        assertTrue(text.contains("обновлён"));
        assertTrue(text.toLowerCase().contains("возраст"));
        assertNotNull(resp.getReplyMarkup());
    }

    @Test
    void whenInvalidAge_returnsError() throws Exception {
        long chatId = 3L;
        UserRepository repo = new InMemoryUserRepository();
        repo.save(new UserProfile(chatId));

        AgeCommand cmd = new AgeCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/age 5")); // вне диапазона 10-120

        String text = resp.getText();
        assertTrue(text.contains("Ошибка"));
        assertTrue(text.contains("10-120"));
        UserProfile profile = repo.findByChatId(chatId);
        assertEquals(0, profile.getAge());
    }
}
