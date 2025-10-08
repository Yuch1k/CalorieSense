package ru.CalorieSense.command.feature;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;


public abstract class AbstractUserParamCommand implements Command {
    protected final BotApp sender;
    protected final UserRepository repository;
    private final String commandName;
    private final String paramName;

    protected AbstractUserParamCommand(BotApp sender, UserRepository repository, String commandName, String paramName) {
        this.sender = sender;
        this.repository = repository;
        this.commandName = commandName;
        this.paramName = paramName;
    }

    protected abstract void applyValue(UserProfile profile, String value) throws Exception;

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();
        UserProfile profile = repository.findByChatId(chatId);

        String[] parts = text.split("\\s+");
        if (parts.length < 2) {
            sender.execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("Введите " + paramName + " после команды, например: " + commandName + " 70")
                    .build());
            return;
        }

        try {
            applyValue(profile, parts[1]);
            repository.save(profile);
            sender.execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("Ваш " + paramName + " обновлён.")
                    .build());
        } catch (Exception e) {
            sender.execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("Ошибка ввода " + paramName + ". Попробуйте ещё раз.")
                    .build());
        }
    }
}
