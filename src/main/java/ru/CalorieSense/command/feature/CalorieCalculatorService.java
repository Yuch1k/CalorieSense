package ru.CalorieSense.command.feature;

import ru.CalorieSense.model.UserProfile;

public class CalorieCalculatorService {
    public int calculate(UserProfile profile) {
        if (profile.getSex() == null || profile.getHeight() <= 0 || profile.getWeight() <= 0 || profile.getAge() <= 0)
            return 2200;

        double bmr;
        if ("М".equalsIgnoreCase(profile.getSex())) {
            bmr = 10 * profile.getWeight() + 6.25 * profile.getHeight() - 5 * profile.getAge() + 5;
        } else {
            bmr = 10 * profile.getWeight() + 6.25 * profile.getHeight() - 5 * profile.getAge() - 161;
        }
        return (int) Math.round(bmr * 1.2);
    }
}
