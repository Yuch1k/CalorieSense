package ru.CalorieSense.command.feature;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;

public class CaloriesCommand implements Command {
    private final BotApp sender;
    private final UserRepository repository;

    public CaloriesCommand(BotApp sender, UserRepository repository) {
        this.sender = sender;
        this.repository = repository;
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        UserProfile profile = repository.findByChatId(chatId);

        if (profile == null || profile.getHeight() == 0 || profile.getWeight() == 0 || profile.getSex() == null || profile.getAge() == 0) {
            sender.execute(new SendMessage(String.valueOf(chatId),
                    "Недостаточно данных. Введите: /weight, /height, /sex, /age"));
            return;
        }

        String message = String.format(
                "Ваши данные:\n" +
                        "Вес: %.1f кг\nРост: %d см\nПол: %s\nВозраст: %d лет\n\n" +
                        "Ваша дневная норма калорий: %d ккал",
                profile.getWeight(),
                profile.getHeight(),
                profile.getSex(),
                profile.getAge(),
                profile.getCaloriesGoal()
        );

        sender.execute(new SendMessage(String.valueOf(chatId), message));
    }
}
