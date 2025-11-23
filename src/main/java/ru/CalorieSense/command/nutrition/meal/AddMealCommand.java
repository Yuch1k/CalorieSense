package ru.CalorieSense.command.nutrition.meal;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.ui.KeyboardFactory;

public class AddMealCommand implements Command {
    @Override
    public BotResponse execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        ReplyKeyboard keyboard = KeyboardFactory.getMealInputKeyboard();
        String message = "🍽️ *Добавление приёма пищи*\n\nВыберите способ ввода:";
        return new BotResponse(chatId, message, "Markdown", keyboard);
    }
}
