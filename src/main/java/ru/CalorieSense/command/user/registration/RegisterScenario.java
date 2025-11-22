package ru.CalorieSense.command.user.registration;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.CalorieSense.BotApp;
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

/**
 * Реализует сценарий пошаговой регистрации пользователя.
 * Диалог проходит через стадии: HEIGHT → WEIGHT → AGE → GENDER → GOAL
 */
public class RegisterScenario implements Command {

    private final BotApp sender;
    private final UserRepository repository;
    private final Map<Long, DialogState> stateMap = new HashMap<>();

    private String getGoalText(String goal) {
        return switch (goal) {
            case "lose" -> "Похудение";
            case "gain" -> "Набор массы";
            default -> "Поддержание веса";
        };
    }

    public RegisterScenario(BotApp sender, UserRepository repository) {
        this.sender = sender;
        this.repository = repository;
    }

    @Override
    public BotResponse execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();
        UserProfile profile = repository.findByChatId(chatId);
        DialogState state = stateMap.getOrDefault(chatId, new DialogState());
        ReplyKeyboard keyboard = KeyboardFactory.getMainKeyboard();
        switch (state.getStep()) {
            case NONE -> {
                return new BotResponse(chatId,
                        "📝 *Регистрация профиля*\n\nВведите ваш рост в сантиметрах:",
                        "Markdown",
                        keyboard);
                state.next();
            }

            case HEIGHT -> {
                if (text.matches("\\d+")) {
                    int height = Integer.parseInt(text);
                    if (height >= 100 && height <= 250) {
                        profile.setHeight(height);
                        sender.execute(SendMessage.builder()
                                .chatId(chatId.toString())
                                .text("✅ Рост сохранён: " + height + " см\n\nТеперь введите ваш вес в килограммах:")
                                .replyMarkup(KeyboardFactory.getMainKeyboard())
                                .build());
                        state.next();
                    } else {
                        sender.execute(SendMessage.builder()
                                .chatId(chatId.toString())
                                .text("❌ Рост должен быть от 100 до 250 см. Попробуйте ещё раз:")
                                .replyMarkup(KeyboardFactory.getMainKeyboard())
                                .build());
                    }
                } else {
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("❌ Некорректный ввод. Введите число (например: 175):")
                            .replyMarkup(KeyboardFactory.getMainKeyboard())
                            .build());
                }
            }

            case WEIGHT -> {
                if (text.matches("\\d+(\\.\\d+)?")) {
                    double weight = Double.parseDouble(text);
                    if (weight >= 10 && weight <= 500) {
                        profile.setWeight(weight);
                        sender.execute(SendMessage.builder()
                                .chatId(chatId.toString())
                                .text("✅ Вес сохранён: " + weight + " кг\n\nВведите ваш возраст в годах:")
                                .replyMarkup(KeyboardFactory.getMainKeyboard())
                                .build());
                        state.next();
                    } else {
                        sender.execute(SendMessage.builder()
                                .chatId(chatId.toString())
                                .text("❌ Вес должен быть от 10 до 500 кг. Попробуйте ещё раз:")
                                .replyMarkup(KeyboardFactory.getMainKeyboard())
                                .build());
                    }
                } else {
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("❌ Некорректный ввод. Введите число (например: 72.5):")
                            .replyMarkup(KeyboardFactory.getMainKeyboard())
                            .build());
                }
            }

            case AGE -> {
                if (text.matches("\\d+")) {
                    int age = Integer.parseInt(text);
                    if (age >= 10 && age <= 120) {
                        profile.setAge(age);
                        sender.execute(SendMessage.builder()
                                .chatId(chatId.toString())
                                .text("✅ Возраст сохранён: " + age + " лет\n\nВведите ваш пол (М/Ж):")
                                .replyMarkup(KeyboardFactory.getMainKeyboard())
                                .build());
                        state.next();
                    } else {
                        sender.execute(SendMessage.builder()
                                .chatId(chatId.toString())
                                .text("❌ Возраст должен быть от 10 до 120 лет. Попробуйте ещё раз:")
                                .replyMarkup(KeyboardFactory.getMainKeyboard())
                                .build());
                    }
                } else {
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("❌ Некорректный ввод. Введите число (например: 30):")
                            .replyMarkup(KeyboardFactory.getMainKeyboard())
                            .build());
                }
            }

            case GENDER -> {
                if (text.equalsIgnoreCase("М") || text.equalsIgnoreCase("Ж")) {
                    profile.setSex(text.toUpperCase());
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("✅ Пол сохранён: " + text.toUpperCase() + "\n\n" +
                                    "🎯 *Выберите вашу цель:*\n\n" +
                                    "Введите:\n" +
                                    "• *1* — Похудение (дефицит калорий -15%)\n" +
                                    "• *2* — Поддержание веса (базовый уровень)\n" +
                                    "• *3* — Набор массы (профицит калорий +15%)")
                            .parseMode("Markdown")
                            .replyMarkup(KeyboardFactory.getMainKeyboard())
                            .build());
                    state.next();
                } else {
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("❌ Некорректный ввод. Введите 'М' или 'Ж':")
                            .replyMarkup(KeyboardFactory.getMainKeyboard())
                            .build());
                }
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
                } else if (text.equals("3") || text.equalsIgnoreCase("набор массы")) {
                    goal = "gain";
                    goalText = "Набор массы";
                } else {
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("❌ Некорректный ввод. Введите 1, 2 или 3:")
                            .replyMarkup(KeyboardFactory.getMainKeyboard())
                            .build());
                    return;
                }

                profile.setGoal(goal);

                sender.execute(SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("✅ Цель сохранена: *" + goalText + "*\n\n" +
                                "💪 *Выберите уровень физической активности:*\n\n" +
                                "Введите:\n" +
                                "• *1* — Малая активность (без тренировок, сидячая работа)\n" +
                                "• *2* — Средняя активность (1-2 тренировки в неделю)\n" +
                                "• *3* — Большая активность (более 2 тренировок в неделю)")
                        .parseMode("Markdown")
                        .replyMarkup(KeyboardFactory.getMainKeyboard())
                        .build());

                state.next();
            }
            case ACTIVITY -> {
                String activity;
                String activityText;

                if (text.equals("1") || text.equalsIgnoreCase("малая") ||
                        text.equalsIgnoreCase("low")) {
                    activity = "low";
                    activityText = "Малая активность (без тренировок)";
                } else if (text.equals("2") || text.equalsIgnoreCase("средняя") ||
                        text.equalsIgnoreCase("medium")) {
                    activity = "medium";
                    activityText = "Средняя активность (1-2 тренировки в неделю)";
                } else if (text.equals("3") || text.equalsIgnoreCase("большая") ||
                        text.equalsIgnoreCase("high")) {
                    activity = "high";
                    activityText = "Большая активность (более 2 тренировок в неделю)";
                } else {
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("❌ Некорректный ввод. Введите 1, 2 или 3:")
                            .replyMarkup(KeyboardFactory.getMainKeyboard())
                            .build());
                    return;
                }

                profile.setActivityLevel(activity);
                repository.save(profile);

                UserProfileFormatter formatter = new UserProfileFormatter(new CalorieCalculatorService());
                String completionMessage = formatter.formatCompletionMessage(profile);

                sender.execute(SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(completionMessage)
                        .parseMode("Markdown")
                        .replyMarkup(KeyboardFactory.getMainKeyboard())
                        .build());

                state.reset();
                stateMap.remove(chatId);
                return;
            }
        }
        stateMap.put(chatId, state);
    }
}
