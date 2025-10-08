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
        if (height < 50 || height > 300) {
            throw new IllegalArgumentException("Некорректный рост");
        }
        profile.setHeight(height);
    }
}
