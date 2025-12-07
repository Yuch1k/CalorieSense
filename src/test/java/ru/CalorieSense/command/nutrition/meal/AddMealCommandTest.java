package ru.CalorieSense.command.nutrition.meal;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.messaging.BotResponse;

import static org.junit.jupiter.api.Assertions.*;

class AddMealCommandTest {

    private Update buildUpdate(long chatId) {
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        msg.setText("/addmeal");

        Update u = new Update();
        u.setMessage(msg);
        return u;
    }

    @Test
    void executeReturnsKeyboardAndText() throws Exception {
        AddMealCommand cmd = new AddMealCommand();
        long chatId = 42L;

        BotResponse resp = cmd.execute(buildUpdate(chatId));

        assertEquals(chatId, resp.getChatId());
        assertTrue(resp.getText().contains("Добавление приёма пищи"));
        assertTrue(resp.getText().contains("Выберите способ ввода"));
        assertEquals("Markdown", resp.getParseMode());
        assertNotNull(resp.getReplyMarkup());
    }
}
