package ru.CalorieSense.command.user.profile;

import ru.CalorieSense.command.base.AbstractParameterCommand;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;

public class WeightCommand extends AbstractParameterCommand {
    public WeightCommand(UserRepository repository) {
        super(repository, "/weight", "вес");
    }

    @Override
    protected void applyValue(UserProfile profile, String value) throws Exception {
        double weight = Double.parseDouble(value.replace(",", "."));
        if (weight < 30 || weight > 300) {
            throw new IllegalArgumentException("Некорректный вес (допустимо: 30–300 кг)");
        }
        profile.setWeight(weight);
    }

    @Override
    protected String getExampleValue() {
        return "70";
    }

    @Override
    protected String getPromptMessage() {
        return "⚖️ Введите ваш вес (кг):\n\n" +
                "Формат: `/weight <значение>`\n\n" +
                "Например: `/weight 70`\n" +
                "Допустимый диапазон: 30–300 кг";
    }
}
