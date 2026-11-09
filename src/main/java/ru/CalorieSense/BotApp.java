package ru.CalorieSense;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import ru.CalorieSense.command.Command;

// Системные команды
import ru.CalorieSense.command.system.StartCommand;
import ru.CalorieSense.command.system.HelpCommand;
import ru.CalorieSense.command.system.BackToMainCommand;
import ru.CalorieSense.command.system.UnknownCommand;

// Команды пользователя
import ru.CalorieSense.command.user.profile.ProfileCommand;
import ru.CalorieSense.command.user.profile.HeightCommand;
import ru.CalorieSense.command.user.profile.WeightCommand;
import ru.CalorieSense.command.user.profile.AgeCommand;
import ru.CalorieSense.command.user.profile.SexCommand;
import ru.CalorieSense.command.user.profile.GoalCommand;
import ru.CalorieSense.command.user.profile.ActivityCommand;
import ru.CalorieSense.command.user.registration.RegisterScenario;

// Команды питания
import ru.CalorieSense.command.nutrition.CaloriesCommand;
import ru.CalorieSense.command.nutrition.meal.AddMealCommand;
import ru.CalorieSense.command.nutrition.meal.HistoryCommand;
import ru.CalorieSense.command.nutrition.meal.ManualInputCommand;
import ru.CalorieSense.command.nutrition.meal.PhotoInputCommand;

// Сервисы
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;
import ru.CalorieSense.service.nutrition.MealTrackingService;

// Репозитории
import ru.CalorieSense.router.CommandRouter;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.repository.InMemoryUserRepository;
import ru.CalorieSense.repository.MealRepository;
import ru.CalorieSense.repository.InMemoryMealRepository;

public class BotApp extends TelegramLongPollingBot {

    private final String botUsername;
    private final CommandRouter router;

    private BotApp(String botToken, String botUsername) {
        super(botToken);
        this.botUsername = botUsername;

        // Репозитории
        UserRepository userRepository = new InMemoryUserRepository();
        InMemoryMealRepository mealRepository = new InMemoryMealRepository();

        // Сервисы
        CalorieCalculatorService calculator = new CalorieCalculatorService();
        MealTrackingService mealTracker = new MealTrackingService(mealRepository);

        // Системные команды
        Command unknownCommand = new UnknownCommand(this);
        Command startCommand = new StartCommand(this);
        Command helpCommand = new HelpCommand(this);
        Command backToMainCommand = new BackToMainCommand(this);

        // Команды пользователя
        Command registerScenario = new RegisterScenario(this, userRepository);
        Command editParamsCommand = new ProfileCommand(this, userRepository, calculator);
        Command heightCommand = new HeightCommand(this, userRepository);
        Command weightCommand = new WeightCommand(this, userRepository);
        Command ageCommand = new AgeCommand(this, userRepository);
        Command sexCommand = new SexCommand(this, userRepository);
        Command goalCommand = new GoalCommand(this, userRepository);
        Command activityCommand = new ActivityCommand(this, userRepository, calculator);

        // Команды питания
        Command caloriesCommand = new CaloriesCommand(this, userRepository, calculator, mealTracker);
        Command addMealCommand = new AddMealCommand(this);
        Command manualInputCommand = new ManualInputCommand(this, mealRepository);
        Command photoInputCommand = new PhotoInputCommand(this);
        Command historyCommand = new HistoryCommand(this, mealTracker);

        // Инициализация роутера
        this.router = new CommandRouter(unknownCommand);

        // Регистрация системных команд
        router.register("/start", startCommand);
        router.register("/help", helpCommand);
        router.register("/backtomain", backToMainCommand);

        // Регистрация команд пользователя
        router.register("/register", registerScenario);
        router.register("/profile", editParamsCommand);
        router.register("/height", heightCommand);
        router.register("/weight", weightCommand);
        router.register("/age", ageCommand);
        router.register("/sex", sexCommand);
        router.register("/goal", goalCommand);
        router.register("/activity", activityCommand);

        // Регистрация команд питания
        router.register("/calories", caloriesCommand);
        router.register("/addmeal", addMealCommand);
        router.register("/manualinput", manualInputCommand);
        router.register("/photoinput", photoInputCommand);
        router.register("/history", historyCommand);
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
