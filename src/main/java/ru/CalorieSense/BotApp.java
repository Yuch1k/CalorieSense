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
import ru.CalorieSense.command.nutrition.meal.ManualInputCommand;
import ru.CalorieSense.command.nutrition.meal.PhotoInputCommand;

// Сервисы
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;
import ru.CalorieSense.service.nutrition.MealTrackingService;

// Репозитории
import ru.CalorieSense.router.CommandRouter;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.repository.MealRepository;
import ru.CalorieSense.repository.JdbcUserRepository;
import ru.CalorieSense.repository.JdbcMealRepository;
import ru.CalorieSense.db.Database;

import ru.CalorieSense.messaging.MessageSender;

import ru.CalorieSense.service.calendar.CalendarService;
import ru.CalorieSense.command.calendar.CalendarCommand;
import ru.CalorieSense.command.nutrition.meal.MealEditCommand;

public class BotApp extends TelegramLongPollingBot {

    private final String botUsername;
    private final CommandRouter router;

    private BotApp(String botToken, String botUsername, Database database) {
        super(botToken);
        this.botUsername = botUsername;

        // Репозитории
        UserRepository userRepository = new JdbcUserRepository(database);
        MealRepository mealRepository = new JdbcMealRepository(database);

        // Сервисы
        CalorieCalculatorService calculator = new CalorieCalculatorService();
        MealTrackingService mealTracker = new MealTrackingService(mealRepository);
        CalendarService calendarService = new CalendarService();

        // Системные команды
        Command unknownCommand = new UnknownCommand();
        Command startCommand = new StartCommand();
        Command helpCommand = new HelpCommand();
        Command backToMainCommand = new BackToMainCommand();

        // Команды пользователя
        Command registerScenario = new RegisterScenario(userRepository);
        Command editParamsCommand = new ProfileCommand(userRepository, calculator);
        Command heightCommand = new HeightCommand(userRepository);
        Command weightCommand = new WeightCommand(userRepository);
        Command ageCommand = new AgeCommand(userRepository);
        Command sexCommand = new SexCommand(userRepository);
        Command goalCommand = new GoalCommand(userRepository);
        Command activityCommand = new ActivityCommand(userRepository, calculator);

        // Команды питания
        Command caloriesCommand = new CaloriesCommand(userRepository, calculator, mealTracker);
        Command addMealCommand = new AddMealCommand();
        Command manualInputCommand = new ManualInputCommand(mealRepository);
        Command photoInputCommand = new PhotoInputCommand();

        // Команды календарч
        Command calendarCommand = new CalendarCommand(calendarService);

        // Создаём sender
        MessageSender sender = new MessageSender(this);

        MealEditCommand mealEditCommand = new MealEditCommand(mealRepository);

        // Роутер
        CommandRouter router = new CommandRouter(
                unknownCommand,
                sender,
                calendarService,
                mealTracker,
                calculator,
                userRepository,
                mealRepository,
                mealEditCommand);
        this.router = router;

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

        // Регистрация команд календаря
        router.register("/calendar", calendarCommand);



    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            router.handle(update);
        } catch (Exception e) {
            e.printStackTrace();
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
        Database database = Database.fromProperties(props);
        return new BotApp(token, username, database);
    }
}
