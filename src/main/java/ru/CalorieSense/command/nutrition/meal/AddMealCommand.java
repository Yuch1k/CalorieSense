package ru.CalorieSense.command.nutrition.meal;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.ui.KeyboardFactory;

public class AddMealCommand implements Command {

    private final BotApp sender;

    public AddMealCommand(BotApp sender) {
        this.sender = sender;
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("🍽️ *Добавление приёма пищи*\n\n" +
                        "Выберите способ ввода:")
                .parseMode("Markdown")
                .replyMarkup(KeyboardFactory.getMealInputKeyboard())
                .build();

        sender.execute(message);
    }
}
