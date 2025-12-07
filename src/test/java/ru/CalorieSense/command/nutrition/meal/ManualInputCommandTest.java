package ru.CalorieSense.command.nutrition.meal;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.Meal;
import ru.CalorieSense.repository.MealRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManualInputCommandTest {

    private Update buildUpdate(long chatId, String text) {
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        msg.setText(text);

        Update u = new Update();
        u.setMessage(msg);
        return u;
    }

    @Test
    void fullHappyPathCreatesMealAndResetsState() {
        MealRepository repo = mock(MealRepository.class);
        when(repo.generateId()).thenReturn(1L);

        ManualInputCommand cmd = new ManualInputCommand(repo);
        long chatId = 100L;

        // Старт
        BotResponse r1 = cmd.execute(buildUpdate(chatId, "/manualinput"));
        assertTrue(r1.getText().contains("Ручной ввод"));

        // Калории
        BotResponse r2 = cmd.execute(buildUpdate(chatId, "500"));
        assertTrue(r2.getText().contains("Калории"));

        // Белки
        BotResponse r3 = cmd.execute(buildUpdate(chatId, "30"));
        assertTrue(r3.getText().contains("Белки"));

        // Жиры
        BotResponse r4 = cmd.execute(buildUpdate(chatId, "20"));
        assertTrue(r4.getText().contains("Жиры"));

        // Углеводы
        BotResponse r5 = cmd.execute(buildUpdate(chatId, "60"));
        assertTrue(r5.getText().contains("название приёма"));

        // Название
        BotResponse r6 = cmd.execute(buildUpdate(chatId, "Обед"));
        assertTrue(r6.getText().contains("Приём пищи добавлен"));
        assertTrue(r6.getText().contains("Обед"));
        assertTrue(r6.getText().contains("500"));

        // Проверяем, что Meal был создан и сохранён
        verify(repo, times(1)).addMeal(any(Meal.class));
    }

    @Test
    void invalidCaloriesShowsErrorAndDoesNotAdvance() {
        MealRepository repo = mock(MealRepository.class);
        ManualInputCommand cmd = new ManualInputCommand(repo);
        long chatId = 101L;

        cmd.execute(buildUpdate(chatId, "/manualinput")); // старт

        BotResponse r = cmd.execute(buildUpdate(chatId, "abc"));
        assertTrue(r.getText().contains("Введите число"));
        verify(repo, never()).addMeal(any());
    }
}
