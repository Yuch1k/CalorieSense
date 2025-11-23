package ru.CalorieSense.command.nutrition.meal;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.Meal;
import ru.CalorieSense.model.MealInputState;
import ru.CalorieSense.repository.MealRepository;
import ru.CalorieSense.ui.KeyboardFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ManualInputCommand implements Command {
    private final MealRepository mealRepository;
    private final Map<Long, MealInputState> stateMap = new HashMap<>();

    public ManualInputCommand(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Override
    public BotResponse execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();
        ReplyKeyboard keyboard = KeyboardFactory.getMealInputKeyboard();
        MealInputState state = stateMap.getOrDefault(chatId, new MealInputState());

        switch (state.getStep()) {
            case NONE -> {
                state.next();
                stateMap.put(chatId, state);
                return new BotResponse(chatId,
                        "✍️ *Ручной ввод приёма пищи*\n\nВведите количество калорий:",
                        "Markdown", keyboard);
            }
            case CALORIES -> {
                if (text.matches("\\d+")) {
                    int calories = Integer.parseInt(text);
                    if (calories > 0 && calories <= 10000) {
                        state.setCalories(calories);
                        state.next();
                        stateMap.put(chatId, state);
                        return new BotResponse(chatId,
                                "✅ Калории: " + calories + " ккал\n\nТеперь введите количество белков (в граммах):",
                                "Markdown", keyboard);
                    } else {
                        return new BotResponse(chatId,
                                "❌ Калории должны быть от 1 до 10000. Попробуйте ещё раз:",
                                "Markdown", keyboard);
                    }
                } else {
                    return new BotResponse(chatId,
                            "❌ Введите число (например: 500)",
                            "Markdown", keyboard);
                }
            }
            case PROTEIN -> {
                if (text.matches("\\d+")) {
                    int protein = Integer.parseInt(text);
                    if (protein >= 0 && protein <= 1000) {
                        state.setProtein(protein);
                        state.next();
                        stateMap.put(chatId, state);
                        return new BotResponse(chatId,
                                "✅ Белки: " + protein + " г\n\nТеперь введите количество жиров (в граммах):",
                                "Markdown", keyboard);
                    } else {
                        return new BotResponse(chatId,
                                "❌ Белки должны быть от 0 до 1000 г. Попробуйте ещё раз:",
                                "Markdown", keyboard);
                    }
                } else {
                    return new BotResponse(chatId,
                            "❌ Введите число (например: 30)", "Markdown", keyboard);
                }
            }
            case FATS -> {
                if (text.matches("\\d+")) {
                    int fats = Integer.parseInt(text);
                    if (fats >= 0 && fats <= 1000) {
                        state.setFats(fats);
                        state.next();
                        stateMap.put(chatId, state);
                        return new BotResponse(chatId,
                                "✅ Жиры: " + fats + " г\n\nНаконец, введите количество углеводов (в граммах):",
                                "Markdown", keyboard);
                    } else {
                        return new BotResponse(chatId,
                                "❌ Жиры должны быть от 0 до 1000 г. Попробуйте ещё раз:",
                                "Markdown", keyboard);
                    }
                } else {
                    return new BotResponse(chatId,
                            "❌ Введите число (например: 20)", "Markdown", keyboard);
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

                        ReplyKeyboard keyboard1 = KeyboardFactory.getMainKeyboard();
                        String msg = "✅ *Приём пищи добавлен!*\n\n" +
                                "🔥 Калории: " + state.getCalories() + " ккал\n" +
                                "🍗 Белки: " + state.getProtein() + " г\n" +
                                "🧈 Жиры: " + state.getFats() + " г\n" +
                                "🍞 Углеводы: " + carbs + " г\n\n" +
                                "Используйте '📊 Мои калории' для просмотра прогресса за день.";
                        state.reset();
                        stateMap.remove(chatId);
                        return new BotResponse(chatId, msg, "Markdown", keyboard1);
                    } else {
                        return new BotResponse(chatId,
                                "❌ Углеводы должны быть от 0 до 1000 г. Попробуйте ещё раз:",
                                "Markdown", keyboard);
                    }
                } else {
                    return new BotResponse(chatId,
                            "❌ Введите число (например: 50)", "Markdown", keyboard);
                }
            }
        }


        stateMap.put(chatId, state);

        return new BotResponse(chatId,
                "Что-то пошло не так при вводе приёма пищи. Начните заново.",
                "Markdown", KeyboardFactory.getMainKeyboard());
    }
}
