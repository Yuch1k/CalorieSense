package ru.CalorieSense.command.nutrition.meal;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.model.Meal;
import ru.CalorieSense.service.nutrition.MealTrackingService;
import ru.CalorieSense.ui.KeyboardFactory;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

/**
 * /history — список приёмов пищи пользователя за сегодня с итогом по калориям и БЖУ.
 */
public class HistoryCommand implements Command {

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");

    private final BotApp sender;
    private final MealTrackingService mealTracker;

    public HistoryCommand(BotApp sender, MealTrackingService mealTracker) {
        this.sender = sender;
        this.mealTracker = mealTracker;
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        List<Meal> meals = mealTracker.getTodayMeals(chatId);

        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(buildText(meals))
                .parseMode("Markdown")
                .replyMarkup(KeyboardFactory.getMainKeyboard())
                .build();

        sender.execute(message);
    }

    static String buildText(List<Meal> meals) {
        if (meals == null || meals.isEmpty()) {
            return "📜 *История за сегодня*\n\nСегодня приёмов пищи ещё не было.\n" +
                    "Добавьте их через «🍽️ Добавить приём пищи».";
        }
        StringBuilder sb = new StringBuilder("📜 *История за сегодня*\n\n");
        int calories = 0, protein = 0, fats = 0, carbs = 0;
        int n = 1;
        for (Meal meal : meals.stream().sorted(Comparator.comparing(Meal::getTimestamp)).toList()) {
            String description = meal.getDescription();
            sb.append(n++).append(". ").append(meal.getTimestamp().format(TIME));
            if (description != null && !description.isBlank()) {
                sb.append(" — ").append(escape(description.trim()));
            }
            sb.append(String.format(": %d ккал (Б %d / Ж %d / У %d)%n",
                    meal.getCalories(), meal.getProtein(), meal.getFats(), meal.getCarbs()));
            calories += meal.getCalories();
            protein += meal.getProtein();
            fats += meal.getFats();
            carbs += meal.getCarbs();
        }
        sb.append(String.format("%n*Итого:* %d ккал (Б %d / Ж %d / У %d)", calories, protein, fats, carbs));
        return sb.toString();
    }

    /** Экранирует служебные символы Markdown в тексте пользователя. */
    private static String escape(String text) {
        return text.replace("\\", "\\\\").replace("_", "\\_").replace("*", "\\*")
                .replace("`", "\\`").replace("[", "\\[");
    }
}
