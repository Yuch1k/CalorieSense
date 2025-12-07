package ru.CalorieSense.ui;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.CalorieSense.model.Meal;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarKeyboardFactory {

    public static InlineKeyboardMarkup buildMonthKeyboard(LocalDate month) {
        LocalDate firstDay = month.withDayOfMonth(1);
        LocalDate lastDay = month.withDayOfMonth(month.lengthOfMonth());

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // 1. Заголовок с месяцем
        List<InlineKeyboardButton> header = new ArrayList<>();
        header.add(button("«", "CAL_MONTH_PREV"));
        header.add(button(month.getMonthValue() + "." + month.getYear(), "CAL_IGNORE"));
        header.add(button("»", "CAL_MONTH_NEXT"));
        rows.add(header);

        // 2. Дни недели
        rows.add(weekDaysRow());

        // 3. Дни месяца
        List<InlineKeyboardButton> currentRow = new ArrayList<>();

        // Отступы перед первым днём
        DayOfWeek firstDow = firstDay.getDayOfWeek(); // MONDAY..SUNDAY
        int shift = firstDow.getValue(); // 1..7 (1 = Monday)

        for (int i = 1; i < shift; i++) {
            currentRow.add(button(" ", "CAL_IGNORE"));
        }

        LocalDate cursor = firstDay;
        while (!cursor.isAfter(lastDay)) {
            String text = String.valueOf(cursor.getDayOfMonth());
            String callback = "CAL_DAY:" + cursor;

            currentRow.add(button(text, callback));

            if (currentRow.size() == 7) {
                rows.add(currentRow);
                currentRow = new ArrayList<>();
            }
            cursor = cursor.plusDays(1);
        }

        if (!currentRow.isEmpty()) {
            while (currentRow.size() < 7) {
                currentRow.add(button(" ", "CAL_IGNORE"));
            }
            rows.add(currentRow);
        }



        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }

    private static List<InlineKeyboardButton> weekDaysRow() {
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button("Пн", "CAL_IGNORE"));
        row.add(button("Вт", "CAL_IGNORE"));
        row.add(button("Ср", "CAL_IGNORE"));
        row.add(button("Чт", "CAL_IGNORE"));
        row.add(button("Пт", "CAL_IGNORE"));
        row.add(button("Сб", "CAL_IGNORE"));
        row.add(button("Вс", "CAL_IGNORE"));
        return row;
    }

    private static InlineKeyboardButton button(String text, String callback) {
        InlineKeyboardButton btn = new InlineKeyboardButton();
        btn.setText(text);
        btn.setCallbackData(callback);
        return btn;
    }

    public static InlineKeyboardMarkup buildMealsKeyboard(LocalDate date, java.util.List<Meal> meals) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // Заголовок с датой
        List<InlineKeyboardButton> header = new ArrayList<>();
        header.add(button("« Календарь", "CAL_BACK_TO_MONTH:" + date.withDayOfMonth(1)));
        rows.add(header);

        for (Meal meal : meals) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            String title = meal.getDescription() != null && !meal.getDescription().isBlank()
                    ? meal.getDescription()
                    : "Приём пищи " + meal.getId();
            String text = title + " (" + meal.getCalories() + " ккал)";
            String callback = "MEAL_EDIT:" + meal.getId();
            row.add(button(text, callback));
            rows.add(row);
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }
}

