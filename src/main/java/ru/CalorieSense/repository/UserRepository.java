package ru.CalorieSense.repository;

import ru.CalorieSense.model.UserProfile;

public interface UserRepository {
    UserProfile findByChatId(Long chatId);
    void save(UserProfile profile);
    void updateWeight(Long chatId, double weight);
    void updateHeight(Long chatId, int height);
    void updateSex(Long chatId, String sex);
    void updateAge(Long chatId, int age);
    void updateGoal(Long chatId, String goal);
    void updateActivityLevel(Long chatId, String activityLevel);
}
