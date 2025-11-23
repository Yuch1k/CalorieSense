package ru.CalorieSense.service;
import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;
import ru.CalorieSense.service.UserProfileFormatter;

import static org.junit.jupiter.api.Assertions.*;
public class UserProfileFormatterTest {
    @Test
    void testFormatFullProfileIncomplete() {
        UserProfile profile = new UserProfile(7L);
        CalorieCalculatorService calculator = new CalorieCalculatorService();
        UserProfileFormatter formatter = new UserProfileFormatter(calculator);

        String result = formatter.formatFullProfile(profile);
        assertTrue(result.contains("Профиль не завершён"));
        assertTrue(result.contains("Регистрация"));
    }

    @Test
    void testFormatFullProfileComplete() {
        UserProfile profile = new UserProfile(8L);
        profile.setWeight(80);
        profile.setHeight(180);
        profile.setAge(35);
        profile.setSex("male");
        profile.setGoal("lose");
        profile.setActivityLevel("medium");

        CalorieCalculatorService calculator = new CalorieCalculatorService();
        UserProfileFormatter formatter = new UserProfileFormatter(calculator);

        String result = formatter.formatFullProfile(profile);
        assertTrue(result.contains("Ваш профиль"));
        assertTrue(result.contains("Рост: *180 см*"));
        assertTrue(result.contains("Пол: *male*"));
        assertTrue(result.contains("Цель: *Похудение*"));
        assertTrue(result.contains("Активность: *Средняя"));
    }

    @Test
    void testFormatCompletionMessage() {
        UserProfile profile = new UserProfile(9L);
        profile.setWeight(75);
        profile.setHeight(170);
        profile.setAge(28);
        profile.setSex("female");
        profile.setGoal("gain");
        profile.setActivityLevel("high");

        CalorieCalculatorService calculator = new CalorieCalculatorService();
        UserProfileFormatter formatter = new UserProfileFormatter(calculator);

        String result = formatter.formatCompletionMessage(profile);
        assertTrue(result.contains("Регистрация завершена"));
        assertTrue(result.contains("Ваш профиль"));
        assertTrue(result.contains("Вес: 75 кг"));
        assertTrue(result.contains("Пол: female"));
        assertTrue(result.contains("Цель: Набор массы"));
        assertTrue(result.contains("Активность: Большая"));
    }
}
