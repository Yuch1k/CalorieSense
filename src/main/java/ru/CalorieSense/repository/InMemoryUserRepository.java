package ru.CalorieSense.repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import ru.CalorieSense.model.UserProfile;

public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, UserProfile> storage = new ConcurrentHashMap<>();

    @Override
    public UserProfile findByChatId(Long chatId) {
        return storage.get(chatId);
    }

    private UserProfile getOrCreate(Long chatId) {
        return storage.computeIfAbsent(chatId, id -> new UserProfile());
    }

    @Override
    public void updateWeight(Long chatId, double weight) {
        getOrCreate(chatId).setWeight(weight);
    }

    @Override
    public void updateHeight(Long chatId, int height) {
        getOrCreate(chatId).setHeight(height);
    }

    @Override
    public void updateSex(Long chatId, String sex) {
        getOrCreate(chatId).setSex(sex);
    }


    public void updateAge(Long chatID, int age) {
        getOrCreate(chatID).setAge(age);
    }
}
