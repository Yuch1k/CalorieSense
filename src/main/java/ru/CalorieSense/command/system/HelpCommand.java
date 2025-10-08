package ru.CalorieSense.command.system;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;

public class HelpCommand implements Command {
    private final BotApp sender;

    public HelpCommand(BotApp sender) {
        this.sender = sender;
    }

    private static final String HELP_MESSAGE =
            "Доступные команды:\n" +
                    "/start - начать работу\n" +
                    "/help - показать эту справку\n" +
                    "/weight - обновить мой вес\n" +
                    "/height - обновить мой рост\n" +
                    "/sex - указать пол (М/Ж)\n" +
                    "/register — пройти регистрацию (ввод параметров: пол, возраст, рост, вес)\n" +
                    "/age - обновить возраст\n" +
                    "/calories - показать дневную норму калорий\n";

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        sender.execute(SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(HELP_MESSAGE)
                .build());
    }
}
