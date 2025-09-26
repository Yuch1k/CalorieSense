// CommandRouter.java
package ru.CalorieSense.router;
import java.util.Map;
import java.util.HashMap;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.command.Command;

public class CommandRouter {
    private final Map<String, Command> commands = new HashMap<>();
    private final Command fallback;

    public CommandRouter(Command fallback) { this.fallback = fallback; }

    public void register(String name, Command handler) { commands.put(name.toLowerCase(), handler); }

    public void handle(Update update) throws Exception {
        String text = update.getMessage().getText().trim();
        String cmd = text.split("\\s+")[0].toLowerCase();
        commands.getOrDefault(cmd, fallback).execute(update);
    }
}
