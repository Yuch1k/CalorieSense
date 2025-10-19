package ru.CalorieSense.command.nutrition;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.model.DailyProgress;
import ru.CalorieSense.model.MacronutrientResult;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;
import ru.CalorieSense.service.nutrition.MealTrackingService;
import ru.CalorieSense.ui.KeyboardFactory;

public class CaloriesCommand implements Command {

    private final BotApp sender;
    private final UserRepository userRepository;
    private final CalorieCalculatorService calculator;
    private final MealTrackingService mealTracker;

    public CaloriesCommand(BotApp sender, UserRepository userRepository,
                           CalorieCalculatorService calculator,
                           MealTrackingService mealTracker) {
        this.sender = sender;
        this.userRepository = userRepository;
        this.calculator = calculator;
        this.mealTracker = mealTracker;
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        UserProfile profile = userRepository.findByChatId(chatId);

        MacronutrientResult target = calculator.calculateWithMacros(profile);

        DailyProgress progress = mealTracker.calculateDailyProgress(chatId, target);

        String responseText;
        if (!profile.isComplete()) {
            responseText = "⚠️ *Для точного расчёта пройдите регистрацию!*\n\n" +
                    "📊 Текущая оценка (усредненная):\n\n" +
                    "🔥 Калории: *" + target.getCalories() + " ккал/день*\n\n" +
                    "Нажмите '📝 Регистрация' для ввода ваших параметров.";
        } else {
            responseText = formatProgressWithTarget(progress, profile);
        }

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(responseText)
                .parseMode("Markdown")
                .replyMarkup(KeyboardFactory.getMainKeyboard())
                .build();

        sender.execute(message);
    }

    private String formatProgressWithTarget(DailyProgress progress, UserProfile profile) {
        String goalText = getGoalText(profile.getGoal());
        String activityText = calculator.getActivityLevelText(profile.getActivityLevel());

        double percentage = progress.getCaloriesPercentage();
        String progressBar = createProgressBar(percentage);

        String statusEmoji;
        if (percentage < 50) {
            statusEmoji = "🟢"; // Можно еще много съесть
        } else if (percentage < 90) {
            statusEmoji = "🟡"; // Близко к цели
        } else if (percentage <= 110) {
            statusEmoji = "✅"; // В пределах нормы
        } else {
            statusEmoji = "🔴"; // Перебор
        }

        return statusEmoji + " *Ваш прогресс за сегодня:*\n\n" +
                "🔥 *Калории:* " + progress.getConsumedCalories() + " / " +
                progress.getTargetCalories() + " ккал\n" +
                progressBar + " " + String.format("%.0f%%", percentage) + "\n\n" +
                "📊 *БЖУ:*\n" +
                "🍗 Белки: *" + progress.getConsumedProtein() + " / " +
                progress.getTargetProtein() + " г*\n" +
                "🧈 Жиры: *" + progress.getConsumedFats() + " / " +
                progress.getTargetFats() + " г*\n" +
                "🍞 Углеводы: *" + progress.getConsumedCarbs() + " / " +
                progress.getTargetCarbs() + " г*\n\n" +
                "━━━━━━━━━━━━━━━━\n" +
                "📋 *Осталось:*\n" +
                "🔥 " + Math.max(0, progress.getRemainingCalories()) + " ккал\n" +
                "🍗 " + Math.max(0, progress.getRemainingProtein()) + " г белка\n" +
                "🧈 " + Math.max(0, progress.getRemainingFats()) + " г жиров\n" +
                "🍞 " + Math.max(0, progress.getRemainingCarbs()) + " г углеводов\n\n" +
                "━━━━━━━━━━━━━━━━\n" +
                "⚙️ *Ваши настройки:*\n" +
                "• Цель: " + goalText + "\n" +
                "• Активность: " + activityText;
    }

    /**
     * Создаёт визуальную полосу прогресса
     */
    private String createProgressBar(double percentage) {
        int filled = (int) (percentage / 10);
        filled = Math.min(filled, 10); // Максимум 10 блоков

        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            if (i < filled) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        return bar.toString();
    }

    private String getGoalText(String goal) {
        return switch (goal) {
            case "lose" -> "Похудение";
            case "gain" -> "Набор массы";
            default -> "Поддержание веса";
        };
    }
}
