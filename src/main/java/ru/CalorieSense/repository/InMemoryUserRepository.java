package ru.CalorieSense.repository;

import ru.CalorieSense.model.UserProfile;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, UserProfile> storage = new HashMap<>();

    @Override
    public UserProfile findByChatId(Long chatId) {
        return storage.computeIfAbsent(chatId, UserProfile::new);
    }

    @Override
    public void save(UserProfile profile) {
        storage.put(profile.getChatId(), profile);
    }

    @Override
    public void updateWeight(Long chatId, double weight) {
        UserProfile profile = findByChatId(chatId);
        profile.setWeight(weight);
        save(profile);
    }

    @Override
    public void updateHeight(Long chatId, int height) {
        UserProfile profile = findByChatId(chatId);
        profile.setHeight(height);
        save(profile);
    }

    @Override
    public void updateSex(Long chatId, String sex) {
        UserProfile profile = findByChatId(chatId);
        profile.setSex(sex);
        save(profile);
    }

    @Override
    public void updateAge(Long chatId, int age) {
        UserProfile profile = findByChatId(chatId);
        profile.setAge(age);
        save(profile);
    }
}
