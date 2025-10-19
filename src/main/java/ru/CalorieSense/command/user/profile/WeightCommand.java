package ru.CalorieSense.command.user.profile;

import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.base.AbstractParameterCommand;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;

public class WeightCommand extends AbstractParameterCommand {

    public WeightCommand(BotApp sender, UserRepository repository) {
        super(sender, repository, "/weight", "вес");
    }

    @Override
    protected void applyValue(UserProfile profile, String value) throws Exception {
        double weight = Double.parseDouble(value);
        if (weight <= 10 || weight > 500) {
            throw new IllegalArgumentException("Некорректный вес (допустимо: 10-500 кг)");
        }
        profile.setWeight(weight);
    }

    @Override
    protected String getExampleValue() {
        return "75";
    }

    @Override
    protected String getPromptMessage() {
        return "⚖️ Введите ваш вес в килограммах:\n\n" +
                "Формат: `/weight <значение>`\n\n" +
                "Например: `/weight 75`\n\n" +
                "Допустимый диапазон: 10-500 кг";
    }
}
