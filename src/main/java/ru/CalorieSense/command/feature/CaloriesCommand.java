package ru.CalorieSense.command.feature;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.command.feature.CalorieCalculatorService;

public class CaloriesCommand implements Command {
    private final BotApp sender;
    private final UserRepository repository;
    private final CalorieCalculatorService calculator;

    public CaloriesCommand(BotApp sender, UserRepository repository, CalorieCalculatorService calculator) {
        this.sender = sender;
        this.repository = repository;
        this.calculator = calculator;
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        UserProfile profile = repository.findByChatId(chatId);
        int calories = calculator.calculate(profile);

        sender.execute(SendMessage.builder()
                .chatId(chatId.toString())
                .text("Ваша дневная норма калорий: " + calories + " ккал")
                .build());
    }
}
