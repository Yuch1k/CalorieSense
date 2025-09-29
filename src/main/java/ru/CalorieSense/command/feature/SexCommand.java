package ru.CalorieSense.command.feature;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.repository.UserRepository;

public class SexCommand implements Command {
    private final BotApp sender;
    private final UserRepository repository;

    public SexCommand(BotApp sender, UserRepository repository) {
        this.sender = sender;
        this.repository = repository;
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        String[] parts = update.getMessage().getText().trim().split("\\s+");

        if (parts.length < 2) {
            sender.execute(new SendMessage(String.valueOf(chatId), "Введите пол: /sex М или /sex Ж"));
            return;
        }

        String sex = parts[1].toUpperCase();
        if (!(sex.equals("М") || sex.equals("Ж"))) {
            sender.execute(new SendMessage(String.valueOf(chatId), "Ошибка: допустимые значения М или Ж"));
            return;
        }

        repository.updateSex(chatId, sex);
        sender.execute(new SendMessage(String.valueOf(chatId), "Пол сохранен: " + (sex.equals("М") ? "Мужской" : "Женский")));
    }
}
