package ru.CalorieSense.command.system;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.ui.KeyboardFactory;

public class StartCommand implements Command {

    private final AbsSender sender;

    public StartCommand(AbsSender sender) {
        this.sender = sender;
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("👋 Добро пожаловать в CalorieSense!\n\n" +
                        "Я помогу вам рассчитать дневную норму калорий.\n\n" +
                        "Используйте кнопки ниже или команды.")
                .replyMarkup(KeyboardFactory.getMainKeyboard())
                .build();

        sender.execute(message);
    }
}
