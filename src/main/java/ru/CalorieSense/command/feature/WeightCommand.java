package ru.CalorieSense.command.feature;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.repository.UserRepository;

public class WeightCommand implements Command {
    private final BotApp sender;
    private final UserRepository repository;

    public WeightCommand(BotApp sender, UserRepository repository) {
        this.sender = sender;
        this.repository = repository;
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        String[] parts = update.getMessage().getText().trim().split("\\s+");

        if (parts.length < 2) {
            sender.execute(new SendMessage(String.valueOf(chatId), "Введите вес, например /weight 75"));
            return;
        }

        try {
            double weight = Double.parseDouble(parts[1]);
            if (weight < 30 || weight > 300) {
                sender.execute(new SendMessage(String.valueOf(chatId),
                        "Вес должен быть в пределах от 30 до 300 кг"));
                return;
            }
            repository.updateWeight(chatId, weight);
            sender.execute(new SendMessage(String.valueOf(chatId), "Вес сохранен: " + weight + " кг"));
        } catch (NumberFormatException e) {
            sender.execute(new SendMessage(String.valueOf(chatId), "Ошибка: введите число, например /weight 75"));
        }
    }
}
