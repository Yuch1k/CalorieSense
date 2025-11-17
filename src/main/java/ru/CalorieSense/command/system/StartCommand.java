package ru.CalorieSense.command.system;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.ui.KeyboardFactory;

public class StartCommand implements Command {

    private final AbsSender sender;

    public StartCommand(AbsSender sender) {
        this.sender = sender;
    }

    @Override
    public BotResponse execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();

        String message = "👋 Добро пожаловать в CalorieSense!\n\n" +
                        "Я помогу вам рассчитать дневную норму калорий.\n\n" +
                        "Используйте кнопки ниже или команды.";
        ReplyKeyboard keyboard = KeyboardFactory.getMainKeyboard();

        return new BotResponse(chatId, message, "Markdown", keyboard);
    }
}
