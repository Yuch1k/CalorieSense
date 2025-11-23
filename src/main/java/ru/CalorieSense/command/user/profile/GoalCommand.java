package ru.CalorieSense.command.user.profile;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.ui.KeyboardFactory;

public class GoalCommand implements Command {

    private final UserRepository repository;

    public GoalCommand(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public BotResponse execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();

        boolean isDirectCommand = text.toLowerCase().startsWith("/goal");

        if (!isDirectCommand) {
            return showGoalPrompt(chatId);
        }

        String[] parts = text.split("\\s+", 2);

        if (parts.length < 2) {
            return showGoalPrompt(chatId);
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

            ReplyKeyboard keyboard = KeyboardFactory.getEditParamsKeyboard();
            return new BotResponse(chatId, "❌ Некорректный выбор.\n\nВведите `/goal 1`, `/goal 2` или `/goal 3`",
                    "Markdown", keyboard);
        }

        UserProfile profile = repository.findByChatId(chatId);
        profile.setGoal(goal);
        repository.save(profile);

        ReplyKeyboard keyboard = KeyboardFactory.getMainKeyboard();
        return new BotResponse(chatId, "✅ Цель обновлена: *" + goalText + "*\n\n" +
                "Используйте '📊 Мои калории' для просмотра обновлённых расчётов.\n\n" +
                "Возврат в главное меню.", "Markdown", keyboard);
    }

    private BotResponse showGoalPrompt(Long chatId) throws Exception {
        UserProfile profile = repository.findByChatId(chatId);
        String currentGoal = getGoalText(profile.getGoal());
        ReplyKeyboard keyboard = KeyboardFactory.getEditParamsKeyboard();
        return new BotResponse(chatId, "🎯 *Текущая цель:* " + currentGoal + "\n\n" +
                "Выберите новую цель:\n\n" +
                "Введите:\n" +
                "• `/goal 1` — Похудение (-15% калорий)\n" +
                "• `/goal 2` — Поддержание веса (базовый уровень)\n" +
                "• `/goal 3` — Набор массы (+15% калорий)", "Markdown", keyboard);
    }

    private String getGoalText(String goal) {
        return switch (goal) {
            case "lose" -> "Похудение";
            case "gain" -> "Набор массы";
            default -> "Поддержание веса";
        };
    }
}
