package ru.CalorieSense.command.user.registration;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.messaging.BotResponse;
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;
import ru.CalorieSense.model.DialogState;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;
import ru.CalorieSense.ui.KeyboardFactory;
import ru.CalorieSense.service.UserProfileFormatter;

import java.util.HashMap;
import java.util.Map;

public class RegisterScenario implements Command {
    private final UserRepository repository;
    private final Map<Long, DialogState> stateMap = new HashMap<>();

    public RegisterScenario(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public BotResponse execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();
        UserProfile profile = repository.findByChatId(chatId);
        DialogState state = stateMap.getOrDefault(chatId, new DialogState());
        ReplyKeyboard keyboard = KeyboardFactory.getMainKeyboard();

        switch (state.getStep()) {
            case NONE -> {
                state.next();
                stateMap.put(chatId, state);
                return new BotResponse(chatId, "📝 *Регистрация профиля*\n\nВведите ваш рост в сантиметрах:", "Markdown", keyboard);
            }
            case HEIGHT -> {
                if (text.matches("\\d+")) {
                    int height = Integer.parseInt(text);
                    if (height >= 100 && height <= 250) {
                        profile.setHeight(height);
                        repository.save(profile);
                        state.next();
                        stateMap.put(chatId, state);
                        return new BotResponse(chatId, "✅ Рост сохранён: " + height + " см\n\nТеперь введите ваш вес в килограммах:", "Markdown", keyboard);
                    }
                    return new BotResponse(chatId, "❌ Рост должен быть от 100 до 250 см. Попробуйте ещё раз:", "Markdown", keyboard);
                }
                return new BotResponse(chatId, "❌ Некорректный ввод. Введите число (например: 175):", "Markdown", keyboard);
            }
            case WEIGHT -> {
                if (text.matches("\\d+(\\.\\d+)?")) {
                    double weight = Double.parseDouble(text);
                    if (weight >= 10 && weight <= 500) {
                        profile.setWeight(weight);
                        repository.save(profile);
                        state.next();
                        stateMap.put(chatId, state);
                        return new BotResponse(chatId, "✅ Вес сохранён: " + weight + " кг\n\nВведите ваш возраст в годах:", "Markdown", keyboard);
                    }
                    return new BotResponse(chatId, "❌ Вес должен быть от 10 до 500 кг. Попробуйте ещё раз:", "Markdown", keyboard);
                }
                return new BotResponse(chatId, "❌ Некорректный ввод. Введите число (например: 72.5):", "Markdown", keyboard);
            }
            case AGE -> {
                if (text.matches("\\d+")) {
                    int age = Integer.parseInt(text);
                    if (age >= 10 && age <= 120) {
                        profile.setAge(age);
                        repository.save(profile);
                        state.next();
                        stateMap.put(chatId, state);
                        return new BotResponse(chatId, "✅ Возраст сохранён: " + age + " лет\n\nВведите ваш пол (мужской/женский):", "Markdown", keyboard);
                    }
                    return new BotResponse(chatId, "❌ Возраст должен быть от 10 до 120 лет. Попробуйте ещё раз:", "Markdown", keyboard);
                }
                return new BotResponse(chatId, "❌ Некорректный ввод. Введите число (например: 30):", "Markdown", keyboard);
            }
            case GENDER -> {
                String normalized = text.trim().toLowerCase();
                String sex = switch (normalized) {
                    case "м", "муж", "мужской", "male" -> "male";
                    case "ж", "жен", "женский", "female" -> "female";
                    default -> "";
                };
                if (!sex.isEmpty()) {
                    profile.setSex(sex);
                    repository.save(profile);
                    state.next();
                    stateMap.put(chatId, state);
                    return new BotResponse(chatId,
                            "✅ Пол сохранён: " + (sex.equals("male") ? "Мужской" : "Женский") + "\n\n" +
                                    "🎯 *Выберите вашу цель:*\n\nВведите:\n" +
                                    "• 1 — Похудение\n" +
                                    "• 2 — Поддержание веса\n" +
                                    "• 3 — Набор массы",
                            "Markdown", keyboard);
                }
                return new BotResponse(chatId, "❌ Некорректный ввод. Введите 'мужской' или 'женский':", "Markdown", keyboard);
            }
            case GOAL -> {
                String goal;
                String goalText;
                if (text.equals("1") || text.equalsIgnoreCase("похудение")) {
                    goal = "lose";
                    goalText = "Похудение";
                } else if (text.equals("2") || text.equalsIgnoreCase("поддержание")) {
                    goal = "maintain";
                    goalText = "Поддержание веса";
                } else if (text.equals("3") || text.equalsIgnoreCase("набор массы") || text.equalsIgnoreCase("масса")) {
                    goal = "gain";
                    goalText = "Набор массы";
                } else {
                    return new BotResponse(chatId,
                            "❌ Некорректный ввод. Введите 1, 2 или 3 (или поддержание/похудение/масса):",
                            "Markdown", keyboard);
                }
                profile.setGoal(goal);
                repository.save(profile);
                state.next();
                stateMap.put(chatId, state);
                return new BotResponse(chatId,
                        "✅ Цель сохранена: *" + goalText + "*\n\n" +
                                "💪 *Выберите уровень физической активности:*\n\nВведите:\n" +
                                "• 1 — Малая активность (без тренировок)\n" +
                                "• 2 — Средняя активность (1-2 тренировки в неделю)\n" +
                                "• 3 — Большая активность (более 2 тренировок в неделю)",
                        "Markdown", keyboard);
            }
            case ACTIVITY -> {
                String activity;
                String activityText;
                if (text.equals("1") || text.equalsIgnoreCase("малая") || text.equalsIgnoreCase("low")) {
                    activity = "low";
                    activityText = "Малая активность (без тренировок)";
                } else if (text.equals("2") || text.equalsIgnoreCase("средняя") || text.equalsIgnoreCase("medium")) {
                    activity = "medium";
                    activityText = "Средняя активность (1-2 тренировки в неделю)";
                } else if (text.equals("3") || text.equalsIgnoreCase("большая") || text.equalsIgnoreCase("high")) {
                    activity = "high";
                    activityText = "Большая активность (более 2 тренировок в неделю)";
                } else {
                    return new BotResponse(chatId, "❌ Некорректный ввод. Введите 1, 2 или 3:", "Markdown", keyboard);
                }
                profile.setActivityLevel(activity);
                repository.save(profile);
                UserProfileFormatter formatter = new UserProfileFormatter(new CalorieCalculatorService());
                String completionMessage = formatter.formatCompletionMessage(profile);
                state.reset();
                stateMap.remove(chatId);
                return new BotResponse(chatId, completionMessage, "Markdown", KeyboardFactory.getMainKeyboard());
            }
        }

        // fallback (should not be reached)
        stateMap.put(chatId, state);
        return new BotResponse(chatId, "Ошибка сценария регистрации. Попробуйте команду /register ещё раз.", "Markdown", KeyboardFactory.getMainKeyboard());
    }
}
