package ru.CalorieSense.ui;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KeyboardFactoryTest {

    @Test
    void mainKeyboardHasExpectedLayout() {
        ReplyKeyboardMarkup markup = KeyboardFactory.getMainKeyboard();

        assertTrue(markup.getResizeKeyboard());
        assertFalse(markup.getOneTimeKeyboard());
        assertFalse(markup.getSelective());

        List<KeyboardRow> rows = markup.getKeyboard();
        assertEquals(3, rows.size());

        assertEquals("📊 Мои калории", ((KeyboardButton) rows.get(0).get(0)).getText());
        assertEquals("📝 Регистрация", ((KeyboardButton) rows.get(0).get(1)).getText());

        assertEquals("🍽️ Добавить приём пищи", ((KeyboardButton) rows.get(1).get(0)).getText());
        assertEquals("📅 Календарь питания", ((KeyboardButton) rows.get(1).get(1)).getText());

        assertEquals("👤 Профиль", ((KeyboardButton) rows.get(2).get(0)).getText());
        assertEquals("❓ Помощь", ((KeyboardButton) rows.get(2).get(1)).getText());
    }

    @Test
    void editParamsKeyboardHasExpectedLayout() {
        ReplyKeyboardMarkup markup = KeyboardFactory.getEditParamsKeyboard();

        List<KeyboardRow> rows = markup.getKeyboard();
        assertEquals(4, rows.size());

        assertEquals("⚖️ Вес", ((KeyboardButton) rows.get(0).get(0)).getText());
        assertEquals("📏 Рост", ((KeyboardButton) rows.get(0).get(1)).getText());

        assertEquals("🎂 Возраст", ((KeyboardButton) rows.get(1).get(0)).getText());
        assertEquals("👤 Пол", ((KeyboardButton) rows.get(1).get(1)).getText());

        assertEquals("🎯 Цель", ((KeyboardButton) rows.get(2).get(0)).getText());
        assertEquals("💪 Активность", ((KeyboardButton) rows.get(2).get(1)).getText());

        assertEquals("◀️ Назад в главное меню", ((KeyboardButton) rows.get(3).get(0)).getText());
    }

    @Test
    void mealInputKeyboardHasExpectedLayout() {
        ReplyKeyboardMarkup markup = KeyboardFactory.getMealInputKeyboard();

        List<KeyboardRow> rows = markup.getKeyboard();
        assertEquals(3, rows.size());

        assertEquals("✍️ Ввести вручную", ((KeyboardButton) rows.get(0).get(0)).getText());
        assertEquals("📸 Ввести по фото", ((KeyboardButton) rows.get(1).get(0)).getText());
        assertEquals("◀️ Назад в главное меню", ((KeyboardButton) rows.get(2).get(0)).getText());
    }
}
