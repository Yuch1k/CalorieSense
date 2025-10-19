package ru.CalorieSense.command.feature;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.ui.KeyboardFactory;

public abstract class AbstractUserParamCommand implements Command {

    protected final BotApp sender;
    protected final UserRepository repository;
    private final String commandName;
    private final String paramName;

    protected AbstractUserParamCommand(BotApp sender, UserRepository repository,
                                       String commandName, String paramName) {
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

        boolean isDirectCommand = text.toLowerCase().startsWith(commandName);

        String[] parts = text.split("\\s+", 2);


        if (!isDirectCommand || parts.length < 2) {
            sendPrompt(chatId);
            return;
        }

        try {
            UserProfile profile = repository.findByChatId(chatId);
            applyValue(profile, parts[1]);
            repository.save(profile);

            SendMessage message = SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text("✅ " + capitalize(paramName) + " обновлён!\n\nВозврат в главное меню.")
                    .replyMarkup(KeyboardFactory.getMainKeyboard())
                    .build();

            sender.execute(message);
        } catch (Exception e) {
            SendMessage message = SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text("❌ Ошибка: " + e.getMessage() + "\n\nПопробуйте ещё раз.")
                    .replyMarkup(KeyboardFactory.getEditParamsKeyboard())
                    .build();

            sender.execute(message);
        }
    }

    private void sendPrompt(Long chatId) throws Exception {
        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(getPromptMessage())
                .parseMode("Markdown")
                .replyMarkup(KeyboardFactory.getEditParamsKeyboard())
                .build();

        sender.execute(message);
    }

    protected String getPromptMessage() {
        return "📝 Введите " + paramName + ":\n\n" +
                "Формат: `" + commandName + " <значение>`\n\n" +
                "Например: `" + commandName + " " + getExampleValue() + "`";
    }

    protected String getExampleValue() {
        return "значение";
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
