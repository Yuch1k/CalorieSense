package ru.CalorieSense.command.feature;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.model.MacronutrientResult;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.ui.KeyboardFactory;

public class CaloriesCommand implements Command {

    private final BotApp sender;
    private final UserRepository repository;
    private final CalorieCalculatorService calculator;

    public CaloriesCommand(BotApp sender, UserRepository repository,
                           CalorieCalculatorService calculator) {
        this.sender = sender;
        this.repository = repository;
        this.calculator = calculator;
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        UserProfile profile = repository.findByChatId(chatId);

        MacronutrientResult result = calculator.calculateWithMacros(profile);

        String responseText;
        if (!profile.isComplete()) {
            responseText = "⚠️ *Для точного расчёта пройдите регистрацию!*\n\n" +
                    "📊 Текущая оценка (усредненная):\n\n" +
                    "🔥 Калории: *" + result.getCalories() + " ккал/день*\n\n" +
                    "Нажмите '📝 Регистрация' для ввода ваших параметров.";
        } else {
            responseText = formatMacronutrientResult(result, profile);
        }

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(responseText)
                .parseMode("Markdown")
                .replyMarkup(KeyboardFactory.getMainKeyboard())
                .build();

        sender.execute(message);
    }

    private String formatMacronutrientResult(MacronutrientResult result, UserProfile profile) {
        CalorieCalculatorService calc = new CalorieCalculatorService();
        String goalText = getGoalText(profile.getGoal());
        String activityText = calc.getActivityLevelText(profile.getActivityLevel());

        return "📊 *Ваша суточная норма:*\n\n" +
                "🔥 *Калории:* " + result.getCalories() + " ккал\n\n" +
                "🍗 *Белки:* " + result.getProtein() + " г (30%)\n" +
                "🧈 *Жиры:* " + result.getFats() + " г (25%)\n" +
                "🍞 *Углеводы:* " + result.getCarbs() + " г (45%)\n\n" +
                "━━━━━━━━━━━━━━━━\n" +
                "📋 *Ваш профиль:*\n" +
                "• Цель: " + goalText + "\n" +
                "• Активность: " + activityText + "\n\n" +
                "_Расчёт основан на формуле Миффлина-Сан Жеора_\n\n" +
                "💡 *Что это значит?*\n" +
                "• Белки — строительный материал для мышц\n" +
                "• Жиры — источник энергии и гормонов\n" +
                "• Углеводы — основное топливо для организма";
    }

    private String getGoalText(String goal) {
        return switch (goal) {
            case "lose" -> "Похудение";
            case "gain" -> "Набор массы";
            default -> "Поддержание веса";
        };
    }
}
