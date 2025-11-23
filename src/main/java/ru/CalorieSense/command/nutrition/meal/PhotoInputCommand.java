package ru.CalorieSense.command.nutrition.meal;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.ui.KeyboardFactory;

public class PhotoInputCommand implements Command {
    @Override
    public BotResponse execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        String msg = ("📸 *Ввод по фото*\n\n" +
                "🚧 Функция находится в разработке.\n\n" +
                "Скоро вы сможете отправлять фото еды, " +
                "и бот автоматически определит калорийность!");
        ReplyKeyboard keyboard = KeyboardFactory.getMealInputKeyboard();
        return new BotResponse(chatId, msg, "Markdown", keyboard);
    }
}