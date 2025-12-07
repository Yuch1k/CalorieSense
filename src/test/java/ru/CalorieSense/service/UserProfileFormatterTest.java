package ru.CalorieSense.service;

import org.junit.jupiter.api.Test;
import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileFormatterTest {

    // Простейшая реализация CalorieCalculatorService только для теста форматтера
    static class DummyCalorieCalculatorService extends CalorieCalculatorService {
        @Override
        public String getActivityLevelText(String level) {
            return switch (level) {
                case "low" -> "Низкая";
                case "medium" -> "Средняя";
                case "high" -> "Высокая";
                default -> "Неизвестная";
            };
        }
    }

    @Test
    void formatFullProfileReturnsWarningWhenIncomplete() {
        UserProfileFormatter formatter = new UserProfileFormatter(new DummyCalorieCalculatorService());
        UserProfile profile = new UserProfile(1L);
        // профиль пустой, isComplete() вернёт false

        String text = formatter.formatFullProfile(profile);

        assertTrue(text.contains("Профиль не завершён"));
        assertTrue(text.contains("Регистрация"));
    }

    @Test
    void formatFullProfileFormatsAllFieldsWhenComplete() {
        UserProfileFormatter formatter = new UserProfileFormatter(new DummyCalorieCalculatorService());
        UserProfile profile = new UserProfile(1L);
        profile.setHeight(180);
        profile.setWeight(75.0);
        profile.setAge(30);
        profile.setSex("male");
        profile.setGoal("lose");
        profile.setActivityLevel("medium");

        // чтобы isComplete() стал true, в реальном классе могут быть и другие поля;
        // при необходимости заполни их здесь.

        String text = formatter.formatFullProfile(profile);

        assertTrue(text.contains("Ваш профиль"));
        assertTrue(text.contains("180 см"));
        assertTrue(text.contains("75.0 кг"));
        assertTrue(text.contains("30 лет"));
        assertTrue(text.contains("Похудение"));
        assertTrue(text.contains("Средняя"));
    }

    @Test
    void formatCompletionMessageUsesGoalAndActivityText() {
        UserProfileFormatter formatter = new UserProfileFormatter(new DummyCalorieCalculatorService());
        UserProfile profile = new UserProfile(1L);
        profile.setHeight(170);
        profile.setWeight(65.0);
        profile.setAge(25);
        profile.setSex("female");
        profile.setGoal("gain");
        profile.setActivityLevel("high");

        String text = formatter.formatCompletionMessage(profile);

        assertTrue(text.contains("Регистрация завершена"));
        assertTrue(text.contains("170 см"));
        assertTrue(text.contains("65.0 кг"));
        assertTrue(text.contains("Набор массы"));
        assertTrue(text.contains("Высокая"));
    }
}
