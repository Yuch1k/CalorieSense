package ru.CalorieSense.command.calendar;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.service.calendar.CalendarService;

import static org.junit.jupiter.api.Assertions.*;

class CalendarCommandTest {

    private Update buildUpdate(long chatId) {
        Message msg = new Message();
        msg.setChat(new Chat(chatId, "private"));
        msg.setText("/calendar");

        Update u = new Update();
        u.setMessage(msg);
        return u;
    }

    @Test
    void executeReturnsCalendarWithKeyboard() {

        CalendarService calendarService = new CalendarService();
        long chatId = 123L;

        CalendarCommand cmd = new CalendarCommand(calendarService);

        BotResponse resp = cmd.execute(buildUpdate(chatId));

        assertEquals(chatId, resp.getChatId());
        assertTrue(resp.getText().contains("Выберите день"));
        assertNotNull(resp.getReplyMarkup());
    }
}
