package ru.CalorieSense.router;

import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.command.Command;

public class CommandRouter {

    private final Map<String, Command> commands = new HashMap<>();
    private final Command fallback;
    private final Map<Long, Command> activeScenarios = new HashMap<>();

    private final Map<String, String> buttonToCommand = new HashMap<>();

    public CommandRouter(Command fallback) {
        this.fallback = fallback;
        initButtonMapping();
    }

    private void initButtonMapping() {
        // Главное меню
        buttonToCommand.put("📊 Мои калории", "/calories");
        buttonToCommand.put("📝 Регистрация", "/register");
        buttonToCommand.put("👤 Профиль", "/profile");
        buttonToCommand.put("🍽️ Добавить приём пищи", "/addmeal");
        buttonToCommand.put("❓ Помощь", "/help");

        // Меню редактирования параметров
        buttonToCommand.put("⚖️ Вес", "/weight");
        buttonToCommand.put("📏 Рост", "/height");
        buttonToCommand.put("🎂 Возраст", "/age");
        buttonToCommand.put("👤 Пол", "/sex");
        buttonToCommand.put("🎯 Цель", "/goal");
        buttonToCommand.put("💪 Активность", "/activity");

        // Меню добавления приёма пищи
        buttonToCommand.put("✍️ Ввести вручную", "/manualinput");
        buttonToCommand.put("📸 Ввести по фото", "/photoinput");

        buttonToCommand.put("◀️ Назад в главное меню", "/backtomain");
    }



    public void register(String name, Command handler) {
        commands.put(name.toLowerCase(), handler);
    }

    public void handle(Update update) throws Exception {
        String text = update.getMessage().getText().trim();
        Long chatId = update.getMessage().getChatId();

        if (buttonToCommand.containsKey(text)) {
            text = buttonToCommand.get(text);
        }

        String cmd = text.split("\\s+")[0].toLowerCase();

        if (cmd.startsWith("/")) {
            Command handler = commands.getOrDefault(cmd, fallback);

            boolean isScenario = handler.getClass().getSimpleName().contains("Scenario") ||
                    handler.getClass().getSimpleName().equals("ManualInputCommand");

            if (!isScenario) {
                activeScenarios.remove(chatId);
            }

            handler.execute(update);

            if (isScenario) {
                activeScenarios.put(chatId, handler);
            }
        } else if (activeScenarios.containsKey(chatId)) {
            activeScenarios.get(chatId).execute(update);
        } else {
            fallback.execute(update);
        }
    }
}
