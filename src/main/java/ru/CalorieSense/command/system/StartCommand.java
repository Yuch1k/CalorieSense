package ru.CalorieSense.command.system;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.command.Command;

public class StartCommand implements Command {
    private final AbsSender sender;
    public StartCommand(AbsSender sender) { this.sender = sender; }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        sender.execute(new SendMessage(String.valueOf(chatId), "Привет! Введите /help"));
    }
}