package ru.CalorieSense.command.system;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.messaging.BotResponse;

import static org.junit.jupiter.api.Assertions.*;

class BackToMainCommandTest {

    private Update buildUpdate(long chatId) {
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        msg.setText("/backtomain");
        Update u = new Update();
        u.setMessage(msg);
        return u;
    }

    @Test
    void executeReturnsMainMenuMessage() throws Exception {
        BackToMainCommand cmd = new BackToMainCommand();
        long chatId = 10L;

        BotResponse resp = cmd.execute(buildUpdate(chatId));

        assertEquals(chatId, resp.getChatId());
        assertTrue(resp.getText().contains("главное меню"));
        assertEquals("Markdown", resp.getParseMode());
        assertNotNull(resp.getReplyMarkup());
    }
}
