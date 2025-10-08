package ru.CalorieSense.command.feature;

import ru.CalorieSense.BotApp;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;

public class WeightCommand extends AbstractUserParamCommand {

    public WeightCommand(BotApp sender, UserRepository repository) {
        super(sender, repository, "/weight", "вес");
    }

    @Override
    protected void applyValue(UserProfile profile, String value) throws Exception {
        double weight = Double.parseDouble(value);
        if (weight <= 10 || weight > 500) {
            throw new IllegalArgumentException("Некорректный вес");
        }
        profile.setWeight(weight);
    }
}
