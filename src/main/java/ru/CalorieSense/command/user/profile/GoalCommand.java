package ru.CalorieSense.command.user.profile;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.ui.KeyboardFactory;

public class GoalCommand implements Command {

    private final BotApp sender;
    private final UserRepository repository;

    public GoalCommand(BotApp sender, UserRepository repository) {
        this.sender = sender;
        this.repository = repository;
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();

        boolean isDirectCommand = text.toLowerCase().startsWith("/goal");

        if (!isDirectCommand) {
            showGoalPrompt(chatId);
            return;
        }

        String[] parts = text.split("\\s+", 2);

        if (parts.length < 2) {
            showGoalPrompt(chatId);
            return;
        }

        String input = parts[1];
        String goal;
        String goalText;

        if (input.equals("1") || input.equalsIgnoreCase("похудение") ||
                input.equalsIgnoreCase("lose")) {
            goal = "lose";
            goalText = "Похудение";
        } else if (input.equals("2") || input.equalsIgnoreCase("поддержание") ||
                input.equalsIgnoreCase("maintain")) {
            goal = "maintain";
            goalText = "Поддержание веса";
        } else if (input.equals("3") || input.equalsIgnoreCase("набор") ||
                input.equalsIgnoreCase("gain")) {
            goal = "gain";
            goalText = "Набор массы";
        } else {
            sender.execute(SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text("❌ Некорректный выбор.\n\nВведите `/goal 1`, `/goal 2` или `/goal 3`")
                    .parseMode("Markdown")
                    .replyMarkup(KeyboardFactory.getEditParamsKeyboard())
                    .build());
            return;
        }

        UserProfile profile = repository.findByChatId(chatId);
        profile.setGoal(goal);
        repository.save(profile);

        sender.execute(SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("✅ Цель обновлена: *" + goalText + "*\n\n" +
                        "Используйте '📊 Мои калории' для просмотра обновлённых расчётов.\n\n" +
                        "Возврат в главное меню.")
                .parseMode("Markdown")
                .replyMarkup(KeyboardFactory.getMainKeyboard())
                .build());
    }

    private void showGoalPrompt(Long chatId) throws Exception {
        UserProfile profile = repository.findByChatId(chatId);
        String currentGoal = getGoalText(profile.getGoal());

        sender.execute(SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text("🎯 *Текущая цель:* " + currentGoal + "\n\n" +
                        "Выберите новую цель:\n\n" +
                        "Введите:\n" +
                        "• `/goal 1` — Похудение (-15% калорий)\n" +
                        "• `/goal 2` — Поддержание веса (базовый уровень)\n" +
                        "• `/goal 3` — Набор массы (+15% калорий)")
                .parseMode("Markdown")
                .replyMarkup(KeyboardFactory.getEditParamsKeyboard())
                .build());
    }

    private String getGoalText(String goal) {
        return switch (goal) {
            case "lose" -> "Похудение";
            case "gain" -> "Набор массы";
            default -> "Поддержание веса";
        };
    }
}
