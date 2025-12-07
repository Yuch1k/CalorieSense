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

class ProfileCommandTest {

    private Update buildUpdate(long chatId, String text) {
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        msg.setText(text);
        Update u = new Update();
        u.setMessage(msg);
        return u;
    }

    @Test
    void executeReturnsFormattedProfileAndEditKeyboard() throws Exception {
        long chatId = 1L;
        UserRepository repo = new InMemoryUserRepository();
        CalorieCalculatorService calc = new CalorieCalculatorService();

        // заполним профиль частично, чтобы форматтеру было что показывать
        UserProfile profile = repo.findByChatId(chatId);
        profile.setWeight(75);
        profile.setHeight(180);
        profile.setAge(30);
        profile.setSex("male");
        profile.setGoal("maintain");
        profile.setActivityLevel("low");
        repo.save(profile);

        ProfileCommand cmd = new ProfileCommand(repo, calc);

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/profile"));

        assertEquals(chatId, resp.getChatId());
        String text = resp.getText();
        // проверяем, что в профиле есть ключевые поля
        assertTrue(text.contains("Вес") || text.contains("вес"));
        assertTrue(text.contains("Рост") || text.contains("рост"));
        assertTrue(text.contains("Возраст") || text.contains("возраст"));
        assertNotNull(resp.getReplyMarkup()); // клавиатура редактирования параметров
        assertEquals("Markdown", resp.getParseMode());
    }
}
