package ru.CalorieSense.ui;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

    /**
     * Главное меню бота
     */
    public static ReplyKeyboardMarkup getMainKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);
        markup.setSelective(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("📊 Мои калории");
        row1.add("📝 Регистрация");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("🍽️ Добавить приём пищи");
        row2.add("📅 Календарь питания");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("👤 Профиль");
        row3.add("❓ Помощь");

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        markup.setKeyboard(keyboard);
        return markup;
    }

    /**
     * Меню редактирования параметров профиля
     */
    public static ReplyKeyboardMarkup getEditParamsKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);
        markup.setSelective(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("⚖️ Вес");
        row1.add("📏 Рост");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("🎂 Возраст");
        row2.add("👤 Пол");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("🎯 Цель");
        row3.add("💪 Активность");

        KeyboardRow row4 = new KeyboardRow();
        row4.add("◀️ Назад в главное меню");

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);

        markup.setKeyboard(keyboard);
        return markup;
    }

    /**
     * Меню добавления приёма пищи
     */
    public static ReplyKeyboardMarkup getMealInputKeyboard() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);
        markup.setSelective(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("✍️ Ввести вручную");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("📸 Ввести по фото");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("◀️ Назад в главное меню");

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        markup.setKeyboard(keyboard);
        return markup;
    }
}
