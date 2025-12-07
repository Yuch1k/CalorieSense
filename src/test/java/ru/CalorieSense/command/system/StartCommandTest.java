package ru.CalorieSense.command.system;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.messaging.BotResponse;

import static org.junit.jupiter.api.Assertions.*;

class StartCommandTest {

    private Update buildUpdate(long chatId) {
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        msg.setText("/start");
        Update u = new Update();
        u.setMessage(msg);
        return u;
    }

    @Test
    void executeReturnsWelcomeMessageAndMainKeyboard() {
        StartCommand cmd = new StartCommand();
        long chatId = 5L;

        BotResponse resp = cmd.execute(buildUpdate(chatId));

        assertEquals(chatId, resp.getChatId());
        String text = resp.getText();
        assertTrue(text.contains("Добро пожаловать"));
        assertTrue(text.contains("CalorieSense"));
        assertEquals("Markdown", resp.getParseMode());
        assertNotNull(resp.getReplyMarkup());
    }
}
