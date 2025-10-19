package ru.CalorieSense.command.feature;

import ru.CalorieSense.BotApp;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;

public class HeightCommand extends AbstractUserParamCommand {

    public HeightCommand(BotApp sender, UserRepository repository) {
        super(sender, repository, "/height", "рост");
    }

    @Override
    protected void applyValue(UserProfile profile, String value) throws Exception {
        int height = Integer.parseInt(value);
        if (height < 100 || height > 250) {
            throw new IllegalArgumentException("Некорректный рост (допустимо: 100-250 см)");
        }
        profile.setHeight(height);
    }

    @Override
    protected String getExampleValue() {
        return "175";
    }

    @Override
    protected String getPromptMessage() {
        return "📏 Введите ваш рост в сантиметрах:\n\n" +
                "Формат: `/height <значение>`\n\n" +
                "Например: `/height 175`\n\n" +
                "Допустимый диапазон: 100-250 см";
    }
}
