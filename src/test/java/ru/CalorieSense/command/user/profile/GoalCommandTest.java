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

class GoalCommandTest {

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
        GoalCommand cmd = new GoalCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/goal"));

        assertEquals(chatId, resp.getChatId());
        String text = resp.getText();
        assertTrue(text.contains("Введите вашу цель"));
        assertTrue(text.contains("/goal похудение"));
        assertNotNull(resp.getReplyMarkup());
    }

    @Test
    void whenValidGoal_updatesProfile() throws Exception {
        long chatId = 2L;
        UserRepository repo = new InMemoryUserRepository();
        repo.save(new UserProfile(chatId));

        GoalCommand cmd = new GoalCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/goal похудение"));

        UserProfile profile = repo.findByChatId(chatId);
        assertEquals("lose", profile.getGoal());  // маппинг \"похудение\" → lose

        String text = resp.getText();
        assertTrue(text.contains("обновлён"));      // общий текст из AbstractParameterCommand
        assertTrue(text.toLowerCase().contains("цель"));
        assertNotNull(resp.getReplyMarkup());
    }

    @Test
    void whenInvalidGoal_returnsError() throws Exception {
        long chatId = 3L;
        UserRepository repo = new InMemoryUserRepository();
        repo.save(new UserProfile(chatId));

        GoalCommand cmd = new GoalCommand(repo);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/goal что-то"));

        String text = resp.getText();
        assertTrue(text.contains("Ошибка"));
        assertTrue(text.contains("поддержание, похудение или масса"));
        UserProfile profile = repo.findByChatId(chatId);
        // дефолтная цель профиля — maintain
        assertEquals("maintain", profile.getGoal());
    }
}
