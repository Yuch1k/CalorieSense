package ru.CalorieSense.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CalendarStateTest {

    @Test
    void constructorSetsCurrentMonthToFirstDayOfCurrentMonth() {
        CalendarState state = new CalendarState();
        LocalDate now = LocalDate.now();

        assertEquals(1, state.getCurrentMonth().getDayOfMonth());
        assertEquals(now.getYear(), state.getCurrentMonth().getYear());
        assertEquals(now.getMonth(), state.getCurrentMonth().getMonth());
    }

    @Test
    void setCurrentMonthNormalizesToFirstDay() {
        CalendarState state = new CalendarState();
        LocalDate custom = LocalDate.of(2025, 12, 15);

        state.setCurrentMonth(custom);

        assertEquals(LocalDate.of(2025, 12, 1), state.getCurrentMonth());
    }

    @Test
    void nextMonthMovesToNextFirstDay() {
        CalendarState state = new CalendarState();
        LocalDate initial = LocalDate.of(2025, 1, 1);
        state.setCurrentMonth(initial);

        state.nextMonth();

        assertEquals(LocalDate.of(2025, 2, 1), state.getCurrentMonth());
    }

    @Test
    void prevMonthMovesToPreviousFirstDay() {
        CalendarState state = new CalendarState();
        LocalDate initial = LocalDate.of(2025, 3, 1);
        state.setCurrentMonth(initial);

        state.prevMonth();

        assertEquals(LocalDate.of(2025, 2, 1), state.getCurrentMonth());
    }
}
