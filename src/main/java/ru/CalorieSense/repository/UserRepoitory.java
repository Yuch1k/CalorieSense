package ru.CalorieSense.repository;

import ru.CalorieSense.model.UserProfile;

public interface UserRepository {
    UserProfile findByChatId(Long chatId);
    void updateWeight(Long chatId, double weight);
    void updateHeight(Long chatId, int height);
    void updateSex(Long chatId, String sex);
    void updateAge(Long chatID, int age);
}
