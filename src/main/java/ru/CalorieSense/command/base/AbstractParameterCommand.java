package ru.CalorieSense.command.base;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.ui.KeyboardFactory;

public abstract class AbstractParameterCommand implements Command {

    protected final UserRepository repository;
    private final String commandName;
    private final String paramName;

    protected AbstractParameterCommand(UserRepository repository, String commandName, String paramName) {
        this.repository = repository;
        this.commandName = commandName;
        this.paramName = paramName;
    }

    protected abstract void applyValue(UserProfile profile, String value) throws Exception;

    @Override
    public BotResponse execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();
        boolean isDirectCommand = text.toLowerCase().startsWith(commandName);
        String[] parts = text.split("\\s+", 2);

        if (!isDirectCommand || parts.length < 2) {
            return sendPrompt(chatId);
        }
        try {
            UserProfile profile = repository.findByChatId(chatId);
            applyValue(profile, parts[1]);
            repository.save(profile);
            ReplyKeyboard keyboard = KeyboardFactory.getMainKeyboard();
            return new BotResponse(chatId, "✅ " + capitalize(paramName) + " обновлён!\n\nВозврат в главное меню.", "Markdown", keyboard);
        } catch (Exception e) {
            ReplyKeyboard keyboard = KeyboardFactory.getEditParamsKeyboard();
            return new BotResponse(chatId, "❌ Ошибка: " + e.getMessage() + "\n\nПопробуйте ещё раз.", "Markdown", keyboard);
        }
    }

    private BotResponse sendPrompt(Long chatId) {
        ReplyKeyboard keyboard = KeyboardFactory.getEditParamsKeyboard();
        return new BotResponse(chatId, getPromptMessage(), "Markdown", keyboard);
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
