package ru.CalorieSense.repository;

import ru.CalorieSense.model.Meal;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private final ConcurrentHashMap<Long, Meal> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public void addMeal(Meal meal) {
        storage.put(meal.getId(), meal);
    }

    @Override
    public List<Meal> getMealsByDate(Long chatId, LocalDate date) {
        return storage.values().stream()
                .filter(meal -> meal.getChatId().equals(chatId))
                .filter(meal -> meal.getDateTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getTodayMeals(Long chatId) {
        return getMealsByDate(chatId, LocalDate.now());
    }

    @Override
    public void deleteMeal(Long mealId) {
        storage.remove(mealId);
    }

    /**
     * Генерирует уникальный ID для нового приёма пищи
     */
    public Long generateId() {
        return idGenerator.getAndIncrement();
    }
}
