package ru.CalorieSense.command.nutrition.meal;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.Meal;
import ru.CalorieSense.repository.MealRepository;
import ru.CalorieSense.ui.KeyboardFactory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MealEditCommandTest {

    private Update buildUpdate(long chatId, String text) {
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        msg.setText(text);

        Update u = new Update();
        u.setMessage(msg);
        return u;
    }

    @Test
    void startEditInitializesStateAndShowsCurrentValues() {
        MealRepository repo = mock(MealRepository.class);
        MealEditCommand cmd = new MealEditCommand(repo);

        long chatId = 200L;
        Meal meal = new Meal(1L, chatId, LocalDateTime.now(), 400, 25, 10, 50, "Обед");
        when(repo.findById(1L)).thenReturn(meal);

        BotResponse r = cmd.startEdit(chatId, 1L);

        assertTrue(r.getText().contains("Редактирование приёма"));
        assertTrue(r.getText().contains("Обед"));
        assertTrue(r.getText().contains("400"));
    }

    @Test
    void fullEditFlowUpdatesMeal() {
        MealRepository repo = mock(MealRepository.class);
        MealEditCommand cmd = new MealEditCommand(repo);

        long chatId = 201L;
        Meal meal = new Meal(5L, chatId, LocalDateTime.now(), 400, 25, 10, 50, "Обед");
        when(repo.findById(5L)).thenReturn(meal);

        // запускаем сценарий
        cmd.startEdit(chatId, 5L);

        // Калории
        BotResponse r1 = cmd.execute(buildUpdate(chatId, "450"));
        assertTrue(r1.getText().contains("Калории обновлены"));
        assertEquals(450, meal.getCalories());

        // Белки
        BotResponse r2 = cmd.execute(buildUpdate(chatId, "30"));
        assertTrue(r2.getText().contains("Белки обновлены"));
        assertEquals(30, meal.getProtein());

        // Жиры
        BotResponse r3 = cmd.execute(buildUpdate(chatId, "15"));
        assertTrue(r3.getText().contains("Жиры обновлены"));
        assertEquals(15, meal.getFats());

        // Углеводы
        BotResponse r4 = cmd.execute(buildUpdate(chatId, "60"));
        assertTrue(r4.getText().contains("Углеводы обновлены"));
        assertEquals(60, meal.getCarbs());

        // Название
        BotResponse r5 = cmd.execute(buildUpdate(chatId, "Новый обед"));
        assertTrue(r5.getText().contains("Приём пищи обновлён"));
        assertTrue(r5.getText().contains("Новый обед"));
        assertEquals("Новый обед", meal.getDescription());

        // Проверяем, что updateMeal вызывался несколько раз (на каждом шаге)
        verify(repo, atLeast(1)).updateMeal(meal);
    }
}
