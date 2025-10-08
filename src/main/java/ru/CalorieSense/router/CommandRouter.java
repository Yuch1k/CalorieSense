package ru.CalorieSense.router;

import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.command.Command;

public class CommandRouter {
    private final Map<String, Command> commands = new HashMap<>();
    private final Command fallback;

    private final Map<Long, Command> activeScenarios = new HashMap<>();

    public CommandRouter(Command fallback) {
        this.fallback = fallback;
    }

    public void register(String name, Command handler) {
        commands.put(name.toLowerCase(), handler);
    }

    public void handle(Update update) throws Exception {
        String text = update.getMessage().getText().trim();
        Long chatId = update.getMessage().getChatId();

        String cmd = text.split("\\s+")[0].toLowerCase();

        if (cmd.startsWith("/")) {
            Command handler = commands.getOrDefault(cmd, fallback);
            activeScenarios.remove(chatId);
            handler.execute(update);

            if (handler.getClass().getSimpleName().contains("Scenario")) {
                activeScenarios.put(chatId, handler);
            }
        }
        else if (activeScenarios.containsKey(chatId)) {
            activeScenarios.get(chatId).execute(update);
        }
        else {
            fallback.execute(update);
        }
    }
}
