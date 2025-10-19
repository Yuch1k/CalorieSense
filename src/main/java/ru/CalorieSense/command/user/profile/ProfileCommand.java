package ru.CalorieSense.command.user.profile;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.service.UserProfileFormatter;
import ru.CalorieSense.ui.KeyboardFactory;

public class ProfileCommand implements Command {

    private final BotApp sender;
    private final UserRepository repository;
    private final UserProfileFormatter formatter;

    public ProfileCommand(BotApp sender, UserRepository repository,
                          CalorieCalculatorService calculator) {
        this.sender = sender;
        this.repository = repository;
        this.formatter = new UserProfileFormatter(calculator);
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        UserProfile profile = repository.findByChatId(chatId);

        String profileText = formatter.formatFullProfile(profile);

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(profileText)
                .parseMode("Markdown")
                .replyMarkup(KeyboardFactory.getEditParamsKeyboard())
                .build();

        sender.execute(message);
    }
}
