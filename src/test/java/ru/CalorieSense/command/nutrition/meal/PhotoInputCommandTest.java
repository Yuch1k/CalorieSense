package ru.CalorieSense.command.nutrition.meal;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.messaging.BotResponse;

import static org.junit.jupiter.api.Assertions.*;

class PhotoInputCommandTest {

    private Update buildUpdate(long chatId) {
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        msg.setText("/photoinput");

        Update u = new Update();
        u.setMessage(msg);
        return u;
    }

    @Test
    void executeReturnsStubMessageAndKeyboard() throws Exception {
        PhotoInputCommand cmd = new PhotoInputCommand();
        long chatId = 77L;

        BotResponse resp = cmd.execute(buildUpdate(chatId));

        assertEquals(chatId, resp.getChatId());
        assertTrue(resp.getText().contains("Ввод по фото"));
        assertTrue(resp.getText().contains("в разработке"));
        assertEquals("Markdown", resp.getParseMode());
        assertNotNull(resp.getReplyMarkup());
    }
}
