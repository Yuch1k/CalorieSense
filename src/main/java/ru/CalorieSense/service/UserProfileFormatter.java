package ru.CalorieSense.service;

import ru.CalorieSense.model.UserProfile;
import ru.CalorieSense.service.nutrition.CalorieCalculatorService;

public class UserProfileFormatter {

    private final CalorieCalculatorService calculator;

    public UserProfileFormatter(CalorieCalculatorService calculator) {
        this.calculator = calculator;
    }

    /**
     * Форматирует полный профиль пользователя в читаемый вид
     */
    public String formatFullProfile(UserProfile profile) {
        if (!profile.isComplete()) {
            return "⚠️ *Профиль не завершён*\n\n" +
                    "Для полного профиля необходимо пройти регистрацию.\n\n" +
                    "Нажмите '📝 Регистрация' для заполнения всех параметров.";
        }

        String goalText = getGoalText(profile.getGoal());
        String activityText = calculator.getActivityLevelText(profile.getActivityLevel());

        return "👤 *Ваш профиль:*\n\n" +
                "📏 Рост: *" + profile.getHeight() + " см*\n" +
                "⚖️ Вес: *" + profile.getWeight() + " кг*\n" +
                "🎂 Возраст: *" + profile.getAge() + " лет*\n" +
                "👤 Пол: *" + profile.getSex() + "*\n" +
                "🎯 Цель: *" + goalText + "*\n" +
                "💪 Активность: *" + activityText + "*\n\n" +
                "━━━━━━━━━━━━━━━━\n" +
                "Выберите параметр для изменения:";
    }

    /**
     * Форматирует краткий профиль (для завершения регистрации)
     */
    public String formatCompletionMessage(UserProfile profile) {
        String goalText = getGoalText(profile.getGoal());
        String activityText = calculator.getActivityLevelText(profile.getActivityLevel());

        return "🎉 *Регистрация завершена!*\n\n" +
                "Ваш профиль:\n" +
                "• Рост: " + profile.getHeight() + " см\n" +
                "• Вес: " + profile.getWeight() + " кг\n" +
                "• Возраст: " + profile.getAge() + " лет\n" +
                "• Пол: " + profile.getSex() + "\n" +
                "• Цель: " + goalText + "\n" +
                "• Активность: " + activityText + "\n\n" +
                "Используйте кнопку '📊 Мои калории' для просмотра " +
                "вашей суточной нормы калорий и БЖУ!";
    }

    private String getGoalText(String goal) {
        return switch (goal) {
            case "lose" -> "Похудение";
            case "gain" -> "Набор массы";
            default -> "Поддержание веса";
        };
    }
}
