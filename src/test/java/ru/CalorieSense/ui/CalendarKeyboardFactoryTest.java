package ru.CalorieSense.ui;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.CalorieSense.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalendarKeyboardFactoryTest {

    @Test
    void buildMonthKeyboardHasHeaderAndWeekDays() {
        LocalDate month = LocalDate.of(2025, 12, 1);
        InlineKeyboardMarkup markup = CalendarKeyboardFactory.buildMonthKeyboard(month);

        var rows = markup.getKeyboard();
        assertTrue(rows.size() >= 3); // заголовок + дни недели + хотя бы одна неделя

        List<InlineKeyboardButton> header = rows.get(0);
        assertEquals(3, header.size());
        assertEquals("CAL_MONTH_PREV", header.get(0).getCallbackData());
        assertEquals("CAL_MONTH_NEXT", header.get(2).getCallbackData());

        List<InlineKeyboardButton> weekDays = rows.get(1);
        assertEquals(7, weekDays.size());
    }

    @Test
    void buildMonthKeyboardUsesCalDayPrefix() {
        LocalDate month = LocalDate.of(2025, 12, 1);
        InlineKeyboardMarkup markup = CalendarKeyboardFactory.buildMonthKeyboard(month);

        boolean foundDayCallback = markup.getKeyboard().stream()
                .flatMap(List::stream)
                .map(InlineKeyboardButton::getCallbackData)
                .filter(data -> data != null && data.startsWith("CAL_DAY:"))
                .findAny()
                .isPresent();

        assertTrue(foundDayCallback);
    }

    @Test
    void buildMealsKeyboardCreatesButtonsPerMeal() {
        LocalDate date = LocalDate.of(2025, 12, 7);
        Meal m1 = new Meal(1L, 1L, date.atTime(9, 0), 300, 20, 10, 40, "Завтрак");
        Meal m2 = new Meal(2L, 1L, date.atTime(13, 0), 500, 30, 15, 60, "Обед");

        InlineKeyboardMarkup markup = CalendarKeyboardFactory.buildMealsKeyboard(date, List.of(m1, m2));

        var rows = markup.getKeyboard();
        // 1 строка — заголовок, дальше по приёму на строку
        assertEquals(1 + 2, rows.size());

        InlineKeyboardButton b1 = rows.get(1).get(0);
        InlineKeyboardButton b2 = rows.get(2).get(0);

        assertTrue(b1.getText().contains("Завтрак"));
        assertEquals("MEAL_EDIT:1", b1.getCallbackData());
        assertTrue(b2.getText().contains("Обед"));
        assertEquals("MEAL_EDIT:2", b2.getCallbackData());

        // кнопка возврата к календарю
        InlineKeyboardButton back = rows.get(0).get(0);
        assertTrue(back.getCallbackData().startsWith("CAL_BACK_TO_MONTH:"));
    }
}
