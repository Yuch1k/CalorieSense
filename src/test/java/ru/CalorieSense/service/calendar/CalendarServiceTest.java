package ru.CalorieSense.service.calendar;

import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.CalendarState;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CalendarServiceTest {

    @Test
    void stateIsPerChatId() {
        CalendarService service = new CalendarService();

        LocalDate m1 = service.getCurrentMonth(1L);
        LocalDate m2 = service.getCurrentMonth(2L);

        assertNotNull(m1);
        assertNotNull(m2);
        assertNotSame(service.getState(1L), service.getState(2L));
    }

    @Test
    void moveNextAndPrevAffectOnlyOneChat() {
        CalendarService service = new CalendarService();

        LocalDate base1 = service.getCurrentMonth(1L);
        LocalDate base2 = service.getCurrentMonth(2L);

        service.moveNextMonth(1L);

        assertEquals(base1.plusMonths(1).withDayOfMonth(1), service.getCurrentMonth(1L));
        assertEquals(base2.withDayOfMonth(1), service.getCurrentMonth(2L));
    }

    @Test
    void resetRemovesState() {
        CalendarService service = new CalendarService();
        service.getCurrentMonth(1L);

        service.reset(1L);

        CalendarState newState = service.getState(1L);
        assertNotNull(newState);
    }
}
