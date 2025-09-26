// UnknownCommand.java
package ru.CalorieSense.command.system;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;

public class UnknownCommand implements Command {
    private final BotApp sender;

    public UnknownCommand(BotApp sender) { this.sender = sender; }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        sender.execute(new SendMessage(String.valueOf(chatId), "Неизвестная команда, введите /help"));
    }
}
