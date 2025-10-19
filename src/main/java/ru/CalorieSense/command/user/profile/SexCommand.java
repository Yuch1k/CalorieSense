package ru.CalorieSense.command.user.profile;

import ru.CalorieSense.BotApp;
import ru.CalorieSense.command.base.AbstractParameterCommand;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;

public class SexCommand extends AbstractParameterCommand {

    public SexCommand(BotApp sender, UserRepository repository) {
        super(sender, repository, "/sex", "пол");
    }

    @Override
    protected void applyValue(UserProfile profile, String value) throws Exception {
        String normalized = value.trim().toUpperCase();
        if (!normalized.equals("М") && !normalized.equals("Ж")) {
            throw new IllegalArgumentException("Введите М или Ж");
        }
        profile.setSex(normalized);
    }

    @Override
    protected String getExampleValue() {
        return "М";
    }

    @Override
    protected String getPromptMessage() {
        return "👤 Введите ваш пол:\n\n" +
                "Формат: `/sex <М или Ж>`\n\n" +
                "Например: `/sex М`";
    }
}
