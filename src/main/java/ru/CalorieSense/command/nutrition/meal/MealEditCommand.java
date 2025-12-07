package ru.CalorieSense.command.nutrition.meal;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.model.Meal;
import ru.CalorieSense.model.MealEditState;
import ru.CalorieSense.repository.MealRepository;
import ru.CalorieSense.ui.KeyboardFactory;

import java.util.HashMap;
import java.util.Map;

public class MealEditCommand implements Command {

    private final MealRepository mealRepository;
    private final Map<Long, MealEditState> stateByChat = new HashMap<>();

    public MealEditCommand(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    // Специальный вход для запуска сценария из callback (мы будем вызывать его из CommandRouter)
    public BotResponse startEdit(Long chatId, Long mealId) {
        Meal meal = mealRepository.findById(mealId);
        if (meal == null) {
            return BotResponse.simple(chatId, "❌ Приём пищи не найден.");
        }

        MealEditState state = new MealEditState();
        state.setMealId(mealId);
        state.next(); // CALORIES
        stateByChat.put(chatId, state);

        String text = "✏️ Редактирование приёма пищи:\n\n" +
                "Текущее название: " + meal.getDescription() + "\n" +
                "Калории: " + meal.getCalories() + "\n" +
                "Белки: " + meal.getProtein() + "\n" +
                "Жиры: " + meal.getFats() + "\n" +
                "Углеводы: " + meal.getCarbs() + "\n\n" +
                "Введите новое количество калорий (или то же самое):";

        ReplyKeyboard keyboard = KeyboardFactory.getMealInputKeyboard();
        return new BotResponse(chatId, text, "Markdown", keyboard);
    }

    @Override
    public BotResponse execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();
        ReplyKeyboard keyboard = KeyboardFactory.getMealInputKeyboard();

        MealEditState state = stateByChat.get(chatId);
        if (state == null || state.getMealId() == null) {
            return BotResponse.simple(chatId, "❌ Нет активного редактирования. Откройте блюдо из календаря ещё раз.");
        }

        Meal meal = mealRepository.findById(state.getMealId());
        if (meal == null) {
            stateByChat.remove(chatId);
            return BotResponse.simple(chatId, "❌ Приём пищи не найден.");
        }

        switch (state.getStep()) {
            case CALORIES -> {
                if (text.matches("\\d+")) {
                    int calories = Integer.parseInt(text);
                    if (calories > 0 && calories <= 10000) {
                        meal.setCalories(calories);
                        mealRepository.updateMeal(meal);
                        state.next();
                        return new BotResponse(chatId,
                                "✅ Калории обновлены: " + calories + "\n\nВведите новое количество белков:",
                                "Markdown", keyboard);
                    }
                    return new BotResponse(chatId, "Калории должны быть от 1 до 10000.", "Markdown", keyboard);
                }
                return new BotResponse(chatId, "Введите целое число калорий.", "Markdown", keyboard);
            }
            case PROTEIN -> {
                if (text.matches("\\d+")) {
                    int protein = Integer.parseInt(text);
                    if (protein >= 0 && protein <= 1000) {
                        meal.setProtein(protein);
                        mealRepository.updateMeal(meal);
                        state.next();
                        return new BotResponse(chatId,
                                "✅ Белки обновлены: " + protein + "\n\nВведите новое количество жиров:",
                                "Markdown", keyboard);
                    }
                    return new BotResponse(chatId, "Белки должны быть от 0 до 1000.", "Markdown", keyboard);
                }
                return new BotResponse(chatId, "Введите целое число белков.", "Markdown", keyboard);
            }
            case FATS -> {
                if (text.matches("\\d+")) {
                    int fats = Integer.parseInt(text);
                    if (fats >= 0 && fats <= 1000) {
                        meal.setFats(fats);
                        mealRepository.updateMeal(meal);
                        state.next();
                        return new BotResponse(chatId,
                                "✅ Жиры обновлены: " + fats + "\n\nВведите новое количество углеводов:",
                                "Markdown", keyboard);
                    }
                    return new BotResponse(chatId, "Жиры должны быть от 0 до 1000.", "Markdown", keyboard);
                }
                return new BotResponse(chatId, "Введите целое число жиров.", "Markdown", keyboard);
            }
            case CARBS -> {
                if (text.matches("\\d+")) {
                    int carbs = Integer.parseInt(text);
                    if (carbs >= 0 && carbs <= 1000) {
                        meal.setCarbs(carbs);
                        mealRepository.updateMeal(meal);
                        state.next();
                        return new BotResponse(chatId,
                                "✅ Углеводы обновлены: " + carbs + "\n\nВведите новое название приёма пищи:",
                                "Markdown", keyboard);
                    }
                    return new BotResponse(chatId, "Углеводы должны быть от 0 до 1000.", "Markdown", keyboard);
                }
                return new BotResponse(chatId, "Введите целое число углеводов.", "Markdown", keyboard);
            }
            case DESCRIPTION -> {
                if (text.isBlank()) {
                    return new BotResponse(chatId,
                            "Название не может быть пустым. Введите новое название:",
                            "Markdown", keyboard);
                }
                meal.setDescription(text);
                mealRepository.updateMeal(meal);
                state.next(); // DONE
                stateByChat.remove(chatId);

                return new BotResponse(chatId,
                        "✅ Приём пищи обновлён!\n\n" +
                                "Название: " + meal.getDescription() + "\n" +
                                "Калории: " + meal.getCalories() + "\n" +
                                "Белки: " + meal.getProtein() + "\n" +
                                "Жиры: " + meal.getFats() + "\n" +
                                "Углеводы: " + meal.getCarbs(),
                        "Markdown",
                        KeyboardFactory.getMainKeyboard());
            }
            default -> {
                stateByChat.remove(chatId);
                return BotResponse.simple(chatId, "Редактирование завершено. Откройте блюдо из календаря, чтобы начать заново.");
            }
        }
    }
}
