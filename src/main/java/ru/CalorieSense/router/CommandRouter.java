package ru.CalorieSense.router;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.messaging.MessageSender;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CommandRouter {

    private final Map<String, Command> commands = new HashMap<>();
    private final Command fallback;
    private final Map<Long, Command> activeScenarios = new HashMap<>();

    private final Map<String, String> buttonToCommand = new HashMap<>();

    private final MessageSender sender;

    public CommandRouter(Command fallback, MessageSender sender) {
        this.fallback = fallback;
        this.sender = sender;
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
        if (name == null || handler == null) {
            return;
        }
        commands.put(name.toLowerCase(Locale.ROOT), handler);
    }

    public void handle(Update update) throws Exception {
        if (update == null) {
            return;
        }

        Message message = update.getMessage();
        if (message == null || !message.hasText()) {
            return;
        }

        String text = message.getText().trim();
        Long chatId = message.getChatId();

        // Преобразуем текст кнопки в команду, если нужно
        if (buttonToCommand.containsKey(text)) {
            text = buttonToCommand.get(text);
        }

        String cmd = text.split("\\s+")[0].toLowerCase(Locale.ROOT);

        BotResponse response;

        if (cmd.startsWith("/")) {
            Command handler = commands.getOrDefault(cmd, fallback);

            boolean isScenario = handler.getClass().getSimpleName().contains("Scenario")
                    || handler.getClass().getSimpleName().equals("ManualInputCommand");

            if (!isScenario) {
                activeScenarios.remove(chatId);
            }

            response = handler.execute(update);

            if (isScenario) {
                activeScenarios.put(chatId, handler);
            }
        } else if (activeScenarios.containsKey(chatId)) {
            Command scenario = activeScenarios.get(chatId);
            response = scenario.execute(update);
        } else {
            response = fallback.execute(update);
        }

        if (sender != null && response != null) {
            sender.send(response);
        }
    }
}
