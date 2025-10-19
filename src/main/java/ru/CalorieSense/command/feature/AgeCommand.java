package ru.CalorieSense.command.feature;

import ru.CalorieSense.BotApp;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;

public class AgeCommand extends AbstractUserParamCommand {

    public AgeCommand(BotApp sender, UserRepository repository) {
        super(sender, repository, "/age", "возраст");
    }

    @Override
    protected void applyValue(UserProfile profile, String value) throws Exception {
        int age = Integer.parseInt(value);
        if (age < 10 || age > 120) {
            throw new IllegalArgumentException("Некорректный возраст (допустимо: 10-120 лет)");
        }
        profile.setAge(age);
    }

    @Override
    protected String getExampleValue() {
        return "25";
    }

    @Override
    protected String getPromptMessage() {
        return "🎂 Введите ваш возраст в годах:\n\n" +
                "Формат: `/age <значение>`\n\n" +
                "Например: `/age 25`\n\n" +
                "Допустимый диапазон: 10-120 лет";
    }
}
