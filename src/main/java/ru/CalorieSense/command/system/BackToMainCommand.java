package ru.CalorieSense.command.system;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.ui.KeyboardFactory;

public class BackToMainCommand implements Command {

    private final BotApp sender;

    public BackToMainCommand(BotApp sender) {
        this.sender = sender;
    }

    @Override
    public BotResponse execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        ReplyKeyboard keyboard = KeyboardFactory.getMainKeyboard();
        return new BotResponse(chatId, ("◀️ Возврат в главное меню"), "Markdown", keyboard);
    }
}
