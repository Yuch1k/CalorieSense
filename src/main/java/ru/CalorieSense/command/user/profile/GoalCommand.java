package ru.CalorieSense.command.user.profile;

import ru.CalorieSense.command.base.AbstractParameterCommand;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.repository.UserRepository;

public class GoalCommand extends AbstractParameterCommand {
    public GoalCommand(UserRepository repository) {
        super(repository, "/goal", "цель");
    }

    @Override
    protected void applyValue(UserProfile profile, String value) throws Exception {
        String goal = switch (value.trim().toLowerCase()) {
            case "maintain", "поддержание", "0" -> "maintain";
            case "lose", "похудение", "-1" -> "lose";
            case "gain", "масса", "1" -> "gain";
            default -> throw new IllegalArgumentException(
                    "Некорректная цель: поддержание, похудение или масса"
            );
        };
        profile.setGoal(goal);
    }

    @Override
    protected String getExampleValue() {
        return "похудение";
    }

    @Override
    protected String getPromptMessage() {
        return "🎯 Введите вашу цель:\n\n" +
                "Формат: `/goal <поддержание|похудение|масса>`\n\n" +
                "Например: `/goal похудение`";
    }
}
