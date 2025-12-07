package ru.CalorieSense.messaging;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static org.junit.jupiter.api.Assertions.*;

class BotResponseTest {

    @Test
    void constructorStoresAllFields() {
        Long chatId = 123L;
        String text = "hello";
        String parseMode = "Markdown";
        ReplyKeyboard keyboard = new ReplyKeyboard() {};

        BotResponse resp = new BotResponse(chatId, text, parseMode, keyboard);

        assertEquals(chatId, resp.getChatId());
        assertEquals(text, resp.getText());
        assertEquals(parseMode, resp.getParseMode());
        assertEquals(keyboard, resp.getReplyMarkup());
    }

    @Test
    void simpleFactorySetsDefaults() {
        Long chatId = 42L;
        String text = "simple";

        BotResponse resp = BotResponse.simple(chatId, text);

        assertEquals(chatId, resp.getChatId());
        assertEquals(text, resp.getText());
        assertNull(resp.getParseMode());
        assertNull(resp.getReplyMarkup());
    }
}
