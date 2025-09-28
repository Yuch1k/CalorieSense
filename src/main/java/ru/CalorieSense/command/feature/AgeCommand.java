package ru.CalorieSense.command.feature;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.repository.UserRepository;

public class AgeCommand implements Command {
    private final BotApp sender;
    private final UserRepository repository;

    public AgeCommand(BotApp sender, UserRepository repository) {
        this.sender = sender;
        this.repository = repository;
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        String[] parts = update.getMessage().getText().trim().split("\\s+");

        if (parts.length < 2) {
            sender.execute(new SendMessage(String.valueOf(chatId), "Введите возраст: /age 30"));
            return;
        }

        try {
            int age = Integer.parseInt(parts[1]);
            if (age < 10 || age > 120) {
                sender.execute(new SendMessage(String.valueOf(chatId),
                        "Возраст должен быть в пределах от 14 до 120 лет"));
                return;
            }
            repository.findByChatId(chatId).setAge(age);
            sender.execute(new SendMessage(String.valueOf(chatId), "Возраст сохранен: " + age + " лет"));
        } catch (NumberFormatException e) {
            sender.execute(new SendMessage(String.valueOf(chatId),
                    "Ошибка: введите число, например /age 30"));
        }
    }
}
