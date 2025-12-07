package ru.CalorieSense.service.calendar;

import ru.CalorieSense.model.CalendarState;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CalendarService {

    private final Map<Long, CalendarState> stateByChat = new HashMap<>();

    public CalendarState getState(Long chatId) {
        return stateByChat.computeIfAbsent(chatId, id -> new CalendarState());
    }

    public LocalDate getCurrentMonth(Long chatId) {
        return getState(chatId).getCurrentMonth();
    }

    public void moveNextMonth(Long chatId) {
        getState(chatId).nextMonth();
    }

    public void movePrevMonth(Long chatId) {
        getState(chatId).prevMonth();
    }

    public void setMonth(Long chatId, LocalDate anyDateInMonth) {
        getState(chatId).setCurrentMonth(anyDateInMonth);
    }

    public void reset(Long chatId) {
        stateByChat.remove(chatId);
    }
}
