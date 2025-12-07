package ru.CalorieSense.command.nutrition;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.DailyProgress;
import ru.CalorieSense.model.MacronutrientResult;
import ru.CalorieSense.model.Meal;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.InMemoryMealRepository;
import ru.CalorieSense.repository.InMemoryUserRepository;
import ru.CalorieSense.repository.MealRepository;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;
import ru.CalorieSense.service.nutrition.MealTrackingService;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CaloriesCommandTest {

    private Update buildUpdate(long chatId) {
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        msg.setText("/calories");
        Update u = new Update();
        u.setMessage(msg);
        return u;
    }

    @Test
    void whenProfileIncomplete_showsRegistrationWarning() throws Exception {
        long chatId = 1L;

        UserRepository userRepo = new InMemoryUserRepository();
        MealRepository mealRepo = new InMemoryMealRepository();
        CalorieCalculatorService calc = new CalorieCalculatorService();
        MealTrackingService mealTracker = new MealTrackingService(mealRepo);

        // профиль пока пустой → isComplete() == false
        userRepo.findByChatId(chatId);

        CaloriesCommand cmd = new CaloriesCommand(userRepo, calc, mealTracker);

        BotResponse resp = cmd.execute(buildUpdate(chatId));

        assertEquals(chatId, resp.getChatId());
        String text = resp.getText();
        assertTrue(text.contains("пройдите регистрацию"));
        assertEquals("Markdown", resp.getParseMode());
        assertNotNull(resp.getReplyMarkup());
    }

    @Test
    void whenProfileComplete_showsProgressSummary() throws Exception {
        long chatId = 2L;

        UserRepository userRepo = new InMemoryUserRepository();
        MealRepository mealRepo = new InMemoryMealRepository();
        CalorieCalculatorService calc = new CalorieCalculatorService();
        MealTrackingService mealTracker = new MealTrackingService(mealRepo);

        // заполняем профиль, чтобы isComplete() вернул true
        UserProfile profile = userRepo.findByChatId(chatId);
        profile.setWeight(70);
        profile.setHeight(175);
        profile.setAge(28);
        profile.setSex("male");
        profile.setGoal("lose");
        profile.setActivityLevel("medium");
        userRepo.save(profile);

        // добавим простой приём пищи на сегодня, чтобы прогресс был не нулевой
        Long mealId = mealRepo.generateId();
        LocalDateTime now = LocalDate.now().atTime(10, 0);
        mealRepo.addMeal(new Meal(mealId, chatId, now,
                500, 30, 20, 50, "Тестовый обед"));

        CaloriesCommand cmd = new CaloriesCommand(userRepo, calc, mealTracker);

        BotResponse resp = cmd.execute(buildUpdate(chatId));

        String text = resp.getText();
        assertTrue(text.contains("Ваш прогресс за сегодня"));
        assertTrue(text.contains("Калории"));
        assertTrue(text.contains("Белки"));
        // проверяем, что цель и активность подставились
        assertTrue(text.contains("Цель: Похудение"));
        assertTrue(text.contains("Активность"));
        assertEquals("Markdown", resp.getParseMode());
        assertNotNull(resp.getReplyMarkup());
    }
}
