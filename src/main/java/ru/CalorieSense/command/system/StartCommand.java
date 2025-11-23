package ru.CalorieSense.command.system;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.ui.KeyboardFactory;

public class StartCommand implements Command {
    @Override
    public BotResponse execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String message = "👋 Добро пожаловать в CalorieSense!\n\n" +
                "Я помогу вам рассчитать дневную норму калорий.\n\n" +
                "Используйте кнопки ниже или команды.";
        return new BotResponse(chatId, message, "Markdown", KeyboardFactory.getMainKeyboard());
    }
}
