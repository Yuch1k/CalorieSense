package ru.CalorieSense;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import ru.CalorieSense.command.system.HelpCommand;
import ru.CalorieSense.command.system.StartCommand;
import ru.CalorieSense.command.system.UnknownCommand;
import ru.CalorieSense.command.feature.HeightCommand;
import ru.CalorieSense.command.feature.WeightCommand;
import ru.CalorieSense.command.feature.CaloriesComand;
import ru.CalorieSense.router.CommandRouter;

public class BotApp extends TelegramLongPollingBot {

    private final String botToken;
    private final CommandRouter router;

    private BotApp(String botToken) {
        if (botToken == null || botToken.isBlank()) {
            throw new IllegalStateException("bot.token is missing or empty");
        }
        this.botToken = botToken;
        this.router = new CommandRouter(new UnknownCommand(this));
        UserRepository repo = new InMemoryUserRepository();
        this.router.register("/start", new StartCommand(this));
        this.router.register("/help", new HelpCommand(this));
        this.router.register("/height", new HeightCommand(this, repo));
        this.router.register("/weight", new WeightCommand(this, repo));
        this.router.register("/sex", new SexCommand(this, repo));
        this.router.register("/calories", new CaloriesCommand(this, repo));
        this.router.register("/age", new AgeCommand(this, repo));

    }

    public static BotApp fromProperties(String resource) {
        Properties p = new Properties();
        try (InputStream in = BotApp.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("Resource not found: " + resource);
            }
            p.load(in);
            return new BotApp(p.getProperty("bot.token"));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load token from " + resource, e);
        }
    }

    @Override
    public String getBotToken() { return botToken; }

    @Override
    public String getBotUsername() { return "CalorieSense_bot"; }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                router.handle(update);
            }
        } catch (Exception e) {

        }
    }
}
