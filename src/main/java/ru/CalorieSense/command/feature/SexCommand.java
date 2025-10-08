package ru.CalorieSense.command.feature;

import ru.CalorieSense.BotApp;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;


public class SexCommand extends AbstractUserParamCommand {

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
}
