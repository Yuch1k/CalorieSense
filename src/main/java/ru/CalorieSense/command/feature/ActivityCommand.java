package ru.CalorieSense.command.feature;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.ui.KeyboardFactory;

public class ActivityCommand implements Command {

    private final BotApp sender;
    private final UserRepository repository;
    private final CalorieCalculatorService calculator;

    public ActivityCommand(BotApp sender, UserRepository repository,
                           CalorieCalculatorService calculator) {
        this.sender = sender;
        this.repository = repository;
        this.calculator = calculator;
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();

        boolean isDirectCommand = text.toLowerCase().startsWith("/activity");

        if (!isDirectCommand) {
            showActivityPrompt(chatId);
            return;
        }

        String[] parts = text.split("\\s+", 2);

        if (parts.length < 2) {
            showActivityPrompt(chatId);
            return;
        }

        String input = parts[1];
        String activity;
        String activityText;

        if (input.equals("1") || input.equalsIgnoreCase("малая") ||
                input.equalsIgnoreCase("low")) {
            activity = "low";
            activityText = "Малая активность (без тренировок)";
        } else if (input.equals("2") || input.equalsIgnoreCase("средняя") ||
                input.equalsIgnoreCase("medium")) {
            activity = "medium";
            activityText = "Средняя активность (1-2 тренировки в неделю)";
        } else if (input.equals("3") || input.equalsIgnoreCase("большая") ||
                input.equalsIgnoreCase("high")) {
            activity = "high";
            activityText = "Большая активность (более 2 тренировок в неделю)";
        } else {
            sender.execute(SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text("❌ Некорректный выбор.\n\nВведите `/activity 1`, `/activity 2` или `/activity 3`")
                    .parseMode("Markdown")
                    .replyMarkup(KeyboardFactory.getEditParamsKeyboard())
                    .build());
            return;
        }

        UserProfile profile = repository.findByChatId(chatId);
        profile.setActivityLevel(activity);
        repository.save(profile);

        sender.execute(SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("✅ Уровень активности обновлён: *" + activityText + "*\n\n" +
                        "Используйте '📊 Мои калории' для просмотра обновлённых расчётов.\n\n" +
                        "Возврат в главное меню.")
                .parseMode("Markdown")
                .replyMarkup(KeyboardFactory.getMainKeyboard())
                .build());
    }

    private void showActivityPrompt(Long chatId) throws Exception {
        UserProfile profile = repository.findByChatId(chatId);
        String currentActivity = calculator.getActivityLevelText(profile.getActivityLevel());

        sender.execute(SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("💪 *Текущий уровень активности:* " + currentActivity + "\n\n" +
                        "Выберите новый уровень:\n\n" +
                        "Введите:\n" +
                        "• `/activity 1` — Малая (без тренировок, сидячая работа)\n" +
                        "• `/activity 2` — Средняя (1-2 тренировки в неделю)\n" +
                        "• `/activity 3` — Большая (более 2 тренировок в неделю)")
                .parseMode("Markdown")
                .replyMarkup(KeyboardFactory.getEditParamsKeyboard())
                .build());
    }
}
