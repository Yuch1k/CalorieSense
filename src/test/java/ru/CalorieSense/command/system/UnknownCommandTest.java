package ru.CalorieSense.command.system;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.messaging.BotResponse;

import static org.junit.jupiter.api.Assertions.*;

class UnknownCommandTest {

    private Update buildUpdate(long chatId, String text) {
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        msg.setText(text);
        Update u = new Update();
        u.setMessage(msg);
        return u;
    }

    @Test
    void executeReturnsUnknownCommandMessage() throws Exception {
        UnknownCommand cmd = new UnknownCommand();
        long chatId = 99L;

        BotResponse resp = cmd.execute(buildUpdate(chatId, "/something"));

        assertEquals(chatId, resp.getChatId());
        String text = resp.getText();
        assertTrue(text.contains("Неизвестная команда"));
        assertTrue(text.contains("/help"));
        assertEquals("Markdown", resp.getParseMode());
        assertNotNull(resp.getReplyMarkup());
    }
}
