package ru.CalorieSense.command.feature;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.repository.UserRepository;

public class HeightCommand implements Command {
    private final BotApp sender;
    private final UserRepository repository;

    public HeightCommand(BotApp sender, UserRepository repository) {
        this.sender = sender;
        this.repository = repository;
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        String[] parts = update.getMessage().getText().trim().split("\\s+");

        if (parts.length < 2) {
            sender.execute(new SendMessage(String.valueOf(chatId), "Введите рост, например /height 180"));
            return;
        }

        try {
            int height = Integer.parseInt(parts[1]);
            repository.updateHeight(chatId, height);
            sender.execute(new SendMessage(String.valueOf(chatId), "Рост сохранен: " + height + " см"));
        } catch (NumberFormatException e) {
            sender.execute(new SendMessage(String.valueOf(chatId), "Ошибка: введите число, например /height 180"));
        }
    }
}
