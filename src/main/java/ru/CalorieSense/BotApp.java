package ru.CalorieSense;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import ru.CalorieSense.command.Command;
import ru.CalorieSense.command.scenario.RegisterScenario;
import ru.CalorieSense.command.system.HelpCommand;
import ru.CalorieSense.command.system.StartCommand;
import ru.CalorieSense.command.system.UnknownCommand;
import ru.CalorieSense.command.system.EditParamsCommand;
import ru.CalorieSense.command.system.BackToMainCommand;
import ru.CalorieSense.command.feature.HeightCommand;
import ru.CalorieSense.command.feature.WeightCommand;
import ru.CalorieSense.command.feature.CaloriesCommand;
import ru.CalorieSense.command.feature.SexCommand;
import ru.CalorieSense.command.feature.AgeCommand;
import ru.CalorieSense.command.feature.GoalCommand;
import ru.CalorieSense.command.feature.ActivityCommand;
import ru.CalorieSense.router.CommandRouter;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.repository.InMemoryUserRepository;
import ru.CalorieSense.command.feature.CalorieCalculatorService;

public class BotApp extends TelegramLongPollingBot {

    private final String botUsername;
    private final CommandRouter router;

    private BotApp(String botToken, String botUsername) {
        super(botToken);
        this.botUsername = botUsername;

        UserRepository repository = new InMemoryUserRepository();
        CalorieCalculatorService calculator = new CalorieCalculatorService();

        Command unknownCommand = new UnknownCommand(this);
        Command startCommand = new StartCommand(this);
        Command helpCommand = new HelpCommand(this);
        Command registerScenario = new RegisterScenario(this, repository);
        Command caloriesCommand = new CaloriesCommand(this, repository, calculator);
        Command weightCommand = new WeightCommand(this, repository);
        Command heightCommand = new HeightCommand(this, repository);
        Command sexCommand = new SexCommand(this, repository);
        Command ageCommand = new AgeCommand(this, repository);
        Command editParamsCommand = new EditParamsCommand(this, repository, calculator);
        Command backToMainCommand = new BackToMainCommand(this);
        Command goalCommand = new GoalCommand(this, repository);
        Command activityCommand = new ActivityCommand(this, repository, calculator);


        this.router = new CommandRouter(unknownCommand);

        router.register("/start", startCommand);
        router.register("/help", helpCommand);
        router.register("/register", registerScenario);
        router.register("/calories", caloriesCommand);
        router.register("/weight", weightCommand);
        router.register("/height", heightCommand);
        router.register("/sex", sexCommand);
        router.register("/age", ageCommand);
        router.register("/profile", editParamsCommand);
        router.register("/backtomain", backToMainCommand);
        router.register("/goal", goalCommand);
        router.register("/activity", activityCommand);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                router.handle(update);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    public static BotApp fromProperties(String resourceName) throws IOException {
        Properties props = new Properties();
        try (InputStream is = BotApp.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new IOException("Не найден файл: " + resourceName);
            }
            props.load(is);
        }
        String token = props.getProperty("bot.token");
        String username = props.getProperty("bot.username");
        return new BotApp(token, username);
    }
}