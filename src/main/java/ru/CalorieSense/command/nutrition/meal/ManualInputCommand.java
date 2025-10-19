package ru.CalorieSense.command.nutrition.meal;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.model.Meal;
import ru.CalorieSense.model.MealInputState;
import ru.CalorieSense.repository.InMemoryMealRepository;
import ru.CalorieSense.ui.KeyboardFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ManualInputCommand implements Command {

    private final BotApp sender;
    private final InMemoryMealRepository mealRepository;
    private final Map<Long, MealInputState> stateMap = new HashMap<>();

    public ManualInputCommand(BotApp sender, InMemoryMealRepository mealRepository) {
        this.sender = sender;
        this.mealRepository = mealRepository;
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();

        MealInputState state = stateMap.getOrDefault(chatId, new MealInputState());

        switch (state.getStep()) {
            case NONE -> {
                sender.execute(SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("✍️ *Ручной ввод приёма пищи*\n\n" +
                                "Введите количество калорий:")
                        .parseMode("Markdown")
                        .replyMarkup(KeyboardFactory.getMealInputKeyboard())
                        .build());
                state.next();
            }

            case CALORIES -> {
                if (text.matches("\\d+")) {
                    int calories = Integer.parseInt(text);
                    if (calories > 0 && calories <= 10000) {
                        state.setCalories(calories);
                        sender.execute(SendMessage.builder()
                                .chatId(chatId.toString())
                                .text("✅ Калории: " + calories + " ккал\n\n" +
                                        "Теперь введите количество белков (в граммах):")
                                .replyMarkup(KeyboardFactory.getMealInputKeyboard())
                                .build());
                        state.next();
                    } else {
                        sender.execute(SendMessage.builder()
                                .chatId(chatId.toString())
                                .text("❌ Калории должны быть от 1 до 10000. Попробуйте ещё раз:")
                                .replyMarkup(KeyboardFactory.getMealInputKeyboard())
                                .build());
                    }
                } else {
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("❌ Введите число (например: 500)")
                            .replyMarkup(KeyboardFactory.getMealInputKeyboard())
                            .build());
                }
            }

            case PROTEIN -> {
                if (text.matches("\\d+")) {
                    int protein = Integer.parseInt(text);
                    if (protein >= 0 && protein <= 1000) {
                        state.setProtein(protein);
                        sender.execute(SendMessage.builder()
                                .chatId(chatId.toString())
                                .text("✅ Белки: " + protein + " г\n\n" +
                                        "Теперь введите количество жиров (в граммах):")
                                .replyMarkup(KeyboardFactory.getMealInputKeyboard())
                                .build());
                        state.next();
                    } else {
                        sender.execute(SendMessage.builder()
                                .chatId(chatId.toString())
                                .text("❌ Белки должны быть от 0 до 1000 г. Попробуйте ещё раз:")
                                .replyMarkup(KeyboardFactory.getMealInputKeyboard())
                                .build());
                    }
                } else {
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("❌ Введите число (например: 30)")
                            .replyMarkup(KeyboardFactory.getMealInputKeyboard())
                            .build());
                }
            }

            case FATS -> {
                if (text.matches("\\d+")) {
                    int fats = Integer.parseInt(text);
                    if (fats >= 0 && fats <= 1000) {
                        state.setFats(fats);
                        sender.execute(SendMessage.builder()
                                .chatId(chatId.toString())
                                .text("✅ Жиры: " + fats + " г\n\n" +
                                        "Наконец, введите количество углеводов (в граммах):")
                                .replyMarkup(KeyboardFactory.getMealInputKeyboard())
                                .build());
                        state.next();
                    } else {
                        sender.execute(SendMessage.builder()
                                .chatId(chatId.toString())
                                .text("❌ Жиры должны быть от 0 до 1000 г. Попробуйте ещё раз:")
                                .replyMarkup(KeyboardFactory.getMealInputKeyboard())
                                .build());
                    }
                } else {
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("❌ Введите число (например: 20)")
                            .replyMarkup(KeyboardFactory.getMealInputKeyboard())
                            .build());
                }
            }

            case CARBS -> {
                if (text.matches("\\d+")) {
                    int carbs = Integer.parseInt(text);
                    if (carbs >= 0 && carbs <= 1000) {
                        // Сохраняем приём пищи
                        Long mealId = mealRepository.generateId();
                        Meal meal = new Meal(
                                mealId,
                                chatId,
                                LocalDateTime.now(),
                                state.getCalories(),
                                state.getProtein(),
                                state.getFats(),
                                carbs,
                                "Ручной ввод"
                        );
                        mealRepository.addMeal(meal);

                        sender.execute(SendMessage.builder()
                                .chatId(chatId.toString())
                                .text("✅ *Приём пищи добавлен!*\n\n" +
                                        "🔥 Калории: " + state.getCalories() + " ккал\n" +
                                        "🍗 Белки: " + state.getProtein() + " г\n" +
                                        "🧈 Жиры: " + state.getFats() + " г\n" +
                                        "🍞 Углеводы: " + carbs + " г\n\n" +
                                        "Используйте '📊 Мои калории' для просмотра прогресса за день.")
                                .parseMode("Markdown")
                                .replyMarkup(KeyboardFactory.getMainKeyboard())
                                .build());

                        state.reset();
                        stateMap.remove(chatId);
                        return;
                    } else {
                        sender.execute(SendMessage.builder()
                                .chatId(chatId.toString())
                                .text("❌ Углеводы должны быть от 0 до 1000 г. Попробуйте ещё раз:")
                                .replyMarkup(KeyboardFactory.getMealInputKeyboard())
                                .build());
                    }
                } else {
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("❌ Введите число (например: 50)")
                            .replyMarkup(KeyboardFactory.getMealInputKeyboard())
                            .build());
                }
            }
        }

        stateMap.put(chatId, state);
    }
}
