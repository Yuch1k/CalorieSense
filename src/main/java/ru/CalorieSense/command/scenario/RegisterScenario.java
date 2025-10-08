package ru.CalorieSense.command.scenario;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.Command;
import ru.CalorieSense.model.DialogState;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Реализует сценарий пошаговой регистрации пользователя.
 * Диалог проходит через стадии: HEIGHT → WEIGHT → AGE → GENDER.
 */
public class RegisterScenario implements Command {
    private final BotApp sender;
    private final UserRepository repository;
    private final Map<Long, DialogState> stateMap = new HashMap<>();

    public RegisterScenario(BotApp sender, UserRepository repository) {
        this.sender = sender;
        this.repository = repository;
    }

    @Override
    public void execute(Update update) throws Exception {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();

        UserProfile profile = repository.findByChatId(chatId);
        DialogState state = stateMap.getOrDefault(chatId, new DialogState());

        switch (state.getStep()) {
            case NONE -> {
                sender.execute(SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("Давайте зарегистрируем ваш профиль.\nВведите ваш рост (см):")
                        .build());
                state.next();
            }

            case HEIGHT -> {
                if (text.matches("\\d+")) {
                    profile.setHeight(Integer.parseInt(text));
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("Теперь введите ваш вес (в кг):")
                            .build());
                    state.next();
                } else {
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("Некорректный ввод. Введите число (например: 175):")
                            .build());
                }
            }

            case WEIGHT -> {
                if (text.matches("\\d+(\\.\\d+)?")) {
                    profile.setWeight(Double.parseDouble(text));
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("Введите ваш возраст (в годах):")
                            .build());
                    state.next();
                } else {
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("Некорректный ввод. Введите число (например: 72.5):")
                            .build());
                }
            }

            case AGE -> {
                if (text.matches("\\d+")) {
                    profile.setAge(Integer.parseInt(text));
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("Введите ваш пол (М/Ж):")
                            .build());
                    state.next();
                } else {
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("Некорректный ввод. Введите число (например: 30):")
                            .build());
                }
            }

            case GENDER -> {
                if (text.equalsIgnoreCase("М") || text.equalsIgnoreCase("Ж")) {
                    profile.setSex(text.toUpperCase());
                    repository.save(profile);
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("Регистрация завершена!\nИспользуйте /calories для просмотра вашей нормы калорий.")
                            .build());
                    state.reset();
                } else {
                    sender.execute(SendMessage.builder()
                            .chatId(chatId.toString())
                            .text("Некорректный ввод. Введите 'М' или 'Ж':")
                            .build());
                }
            }
        }

        stateMap.put(chatId, state);
    }
}
