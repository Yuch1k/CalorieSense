package ru.CalorieSense.messaging;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.CalorieSense.messaging.BotResponse;

import static org.junit.jupiter.api.Assertions.*;
public class BotResponseTest {
    @Test
    void testConstructorAndGetters() {
        ReplyKeyboard keyboard = org.mockito.Mockito.mock(ReplyKeyboard.class);
        BotResponse response = new BotResponse(9L, "hi", "Markdown", keyboard);

        assertEquals(9L, response.getChatId());
        assertEquals("hi", response.getText());
        assertEquals("Markdown", response.getParseMode());
        assertEquals(keyboard, response.getReplyMarkup());
    }

    @Test
    void testSimpleFactory() {
        BotResponse response = BotResponse.simple(88L, "Hello");
        assertEquals(88L, response.getChatId());
        assertEquals("Hello", response.getText());
        assertNull(response.getParseMode());
        assertNull(response.getReplyMarkup());
    }
}
