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
        if (age < 5 || age > 120) {
            throw new IllegalArgumentException("Некорректный возраст");
        }
        profile.setAge(age);
    }
}
