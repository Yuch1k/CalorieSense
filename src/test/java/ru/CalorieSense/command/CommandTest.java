package ru.CalorieSense.command;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.messaging.BotResponse;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    private Update buildUpdate(long chatId, String text) {
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        msg.setText(text);
        Update u = new Update();
        u.setMessage(msg);
        return u;
    }

    @Test
    void simpleImplementationReturnsResponse() throws Exception {
        Command cmd = update ->
                new BotResponse(update.getMessage().getChatId(), "ok", null, null);

        long chatId = 123L;
        BotResponse resp = cmd.execute(buildUpdate(chatId, "/test"));

        assertNotNull(resp);
        assertEquals(chatId, resp.getChatId());
        assertEquals("ok", resp.getText());
    }
}
