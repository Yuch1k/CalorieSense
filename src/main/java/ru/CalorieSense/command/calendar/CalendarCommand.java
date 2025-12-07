package ru.CalorieSense.command.calendar;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.service.calendar.CalendarService;
import ru.CalorieSense.ui.CalendarKeyboardFactory;

import java.time.LocalDate;

public class CalendarCommand implements Command {

    private final CalendarService calendarService;

    public CalendarCommand(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @Override
    public BotResponse execute(Update update) {
        Long chatId = update.getMessage().getChatId();

        LocalDate month = calendarService.getCurrentMonth(chatId);
        ReplyKeyboard keyboard = CalendarKeyboardFactory.buildMonthKeyboard(month);

        String text = "📅 Выберите день для просмотра питания:";

        return new BotResponse(chatId, text, null, keyboard);
    }
}
