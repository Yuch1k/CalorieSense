package ru.CalorieSense.command.user.registration;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.InMemoryUserRepository;
import ru.CalorieSense.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

class RegisterScenarioTest {

    private Update buildUpdate(long chatId, String text) {
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        msg.setText(text);
        Update u = new Update();
        u.setMessage(msg);
        return u;
    }

    @Test
    void registrationFlowCompletesAndFillsProfile() {
        long chatId = 1L;
        UserRepository repo = new InMemoryUserRepository();
        RegisterScenario scenario = new RegisterScenario(repo);

        // шаг NONE -> запрос роста
        BotResponse r1 = scenario.execute(buildUpdate(chatId, "/register"));
        assertTrue(r1.getText().contains("Регистрация профиля"));

        // HEIGHT
        BotResponse r2 = scenario.execute(buildUpdate(chatId, "180"));
        assertTrue(r2.getText().contains("Рост сохранён"));

        // WEIGHT
        BotResponse r3 = scenario.execute(buildUpdate(chatId, "75.5"));
        assertTrue(r3.getText().contains("Вес сохранён"));

        // AGE
        BotResponse r4 = scenario.execute(buildUpdate(chatId, "30"));
        assertTrue(r4.getText().contains("Возраст сохранён"));

        // GENDER
        BotResponse r5 = scenario.execute(buildUpdate(chatId, "мужской"));
        assertTrue(r5.getText().contains("Пол сохранён"));

        // GOAL
        BotResponse r6 = scenario.execute(buildUpdate(chatId, "1")); // похудение
        assertTrue(r6.getText().contains("Цель сохранена"));

        // ACTIVITY + финальное сообщение
        BotResponse r7 = scenario.execute(buildUpdate(chatId, "2")); // средняя активность
        String finalText = r7.getText();
        assertTrue(finalText.contains("Регистрация завершена") || finalText.contains("профиль"));

        // проверяем, что профиль в репозитории заполнен
        UserProfile profile = repo.findByChatId(chatId);
        assertEquals(180, profile.getHeight());
        assertEquals(75.5, profile.getWeight(), 0.001);
        assertEquals(30, profile.getAge());
        assertEquals("male", profile.getSex());
        assertEquals("lose", profile.getGoal());
        assertEquals("medium", profile.getActivityLevel());
    }
}
