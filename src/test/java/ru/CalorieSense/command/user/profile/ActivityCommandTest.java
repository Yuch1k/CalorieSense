package ru.CalorieSense.command.user.profile;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.InMemoryUserRepository;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;

import static org.junit.jupiter.api.Assertions.*;

class ActivityCommandTest {

    private Update buildUpdate(long chatId, String text) {
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        msg.setText(text);
        Update u = new Update();
        u.setMessage(msg);
        return u;
    }

    @Test
    void whenNoArgument_showsPromptWithCurrentActivity() throws Exception {
        long chatId = 1L;
        UserRepository repo = new InMemoryUserRepository();
        CalorieCalculatorService calc = new CalorieCalculatorService();

        // профиль с дефолтной активностью
        UserProfile profile = repo.findByChatId(chatId);
        profile.setActivityLevel("low");
        repo.save(profile);

        ActivityCommand cmd = new ActivityCommand(repo, calc);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "activity")); // без слеша, чтобы попасть в prompt

        assertEquals(chatId, resp.getChatId());
        String text = resp.getText();
        assertTrue(text.contains("Текущий уровень активности"));
        assertTrue(text.contains("/activity 1"));
        assertNotNull(resp.getReplyMarkup());
    }

    @Test
    void whenValidArgument_updatesActivityLevel() throws Exception {
        long chatId = 2L;
        UserRepository repo = new InMemoryUserRepository();
        CalorieCalculatorService calc = new CalorieCalculatorService();
        repo.save(new UserProfile(chatId));

        ActivityCommand cmd = new ActivityCommand(repo, calc);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/activity 2"));

        UserProfile updated = repo.findByChatId(chatId);
        assertEquals("medium", updated.getActivityLevel());
        String text = resp.getText();
        assertTrue(text.contains("Уровень активности обновлён"));
        assertTrue(text.contains("Средняя активность"));
        assertNotNull(resp.getReplyMarkup());
    }

    @Test
    void whenInvalidArgument_showsError() throws Exception {
        long chatId = 3L;
        UserRepository repo = new InMemoryUserRepository();
        CalorieCalculatorService calc = new CalorieCalculatorService();
        repo.save(new UserProfile(chatId));

        ActivityCommand cmd = new ActivityCommand(repo, calc);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/activity x"));

        String text = resp.getText();
        assertTrue(text.contains("Некорректный выбор"));
        assertTrue(text.contains("/activity 1"));
        assertNotNull(resp.getReplyMarkup());
    }
}
