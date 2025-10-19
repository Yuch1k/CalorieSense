package ru.CalorieSense.command.system;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.ui.KeyboardFactory;

public class UnknownCommand implements Command {

    private final BotApp sender;

    public UnknownCommand(BotApp sender) {
        this.sender = sender;
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("❓ Неизвестная команда.\n\nИспользуйте кнопки ниже или команду /help")
                .replyMarkup(KeyboardFactory.getMainKeyboard())
                .build();

        sender.execute(message);
    }
}