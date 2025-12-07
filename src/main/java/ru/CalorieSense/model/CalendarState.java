package ru.CalorieSense.model;

import java.time.LocalDate;

public class CalendarState {

    private LocalDate currentMonth;

    public CalendarState() {
        this.currentMonth = LocalDate.now().withDayOfMonth(1);
    }

    public LocalDate getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(LocalDate currentMonth) {
        this.currentMonth = currentMonth.withDayOfMonth(1);
    }

    public void nextMonth() {
        currentMonth = currentMonth.plusMonths(1).withDayOfMonth(1);
    }

    public void prevMonth() {
        currentMonth = currentMonth.minusMonths(1).withDayOfMonth(1);
    }
}
