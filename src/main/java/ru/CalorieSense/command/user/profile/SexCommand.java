package ru.CalorieSense.command.user.profile;

import ru.CalorieSense.command.base.AbstractParameterCommand;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;

public class SexCommand extends AbstractParameterCommand {
    public SexCommand(UserRepository repository) {
        super(repository, "/sex", "пол");
    }

    @Override
    protected void applyValue(UserProfile profile, String value) throws Exception {
        String normalized = value.trim().toLowerCase();
        String sex = switch (normalized) {
            case "м", "муж", "мужской", "male", "man" -> "male";
            case "ж", "жен", "женский", "female", "woman" -> "female";
            default -> throw new IllegalArgumentException("Некорректное значение. Допустимо: мужской или женский");
        };
        profile.setSex(sex);
    }

    @Override
    protected String getExampleValue() {
        return "мужской";
    }

    @Override
    protected String getPromptMessage() {
        return "🚻 Введите ваш пол:\n\n" +
                "Формат: `/sex <мужской|женский>`\n\n" +
                "Например: `/sex мужской`";
    }
}
