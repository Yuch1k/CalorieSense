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

class SexCommandTest {

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
        SexCommand cmd = new SexCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/sex"));

        assertEquals(chatId, resp.getChatId());
        String text = resp.getText();
        assertTrue(text.contains("Введите ваш пол"));
        assertTrue(text.contains("/sex мужской"));
        assertNotNull(resp.getReplyMarkup());
    }

    @Test
    void whenValidSex_updatesProfile() throws Exception {
        long chatId = 2L;
        UserRepository repo = new InMemoryUserRepository();
        repo.save(new UserProfile(chatId));

        SexCommand cmd = new SexCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/sex женский"));

        UserProfile profile = repo.findByChatId(chatId);
        assertEquals("female", profile.getSex());

        String text = resp.getText();
        assertTrue(text.contains("обновлён"));
        assertTrue(text.toLowerCase().contains("пол"));
        assertNotNull(resp.getReplyMarkup());
    }

    @Test
    void whenInvalidSex_returnsError() throws Exception {
        long chatId = 3L;
        UserRepository repo = new InMemoryUserRepository();
        repo.save(new UserProfile(chatId));

        SexCommand cmd = new SexCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/sex другое"));

        String text = resp.getText();
        assertTrue(text.contains("Ошибка"));
        assertTrue(text.contains("мужской или женский"));
        UserProfile profile = repo.findByChatId(chatId);
        assertNull(profile.getSex());
    }
}
