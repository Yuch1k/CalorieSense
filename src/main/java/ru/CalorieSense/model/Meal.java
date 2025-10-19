package ru.CalorieSense.model;

import java.time.LocalDateTime;

public class Meal {

    private final Long id;                    // Уникальный ID приёма пищи
    private final Long chatId;                // ID пользователя
    private final LocalDateTime timestamp;    // Время приёма пищи
    private final int calories;               // Калории
    private final int protein;                // Белки (г)
    private final int fats;                   // Жиры (г)
    private final int carbs;                  // Углеводы (г)
    private final String description;         // Описание (опционально)

    public Meal(Long id, Long chatId, LocalDateTime timestamp,
                int calories, int protein, int fats, int carbs, String description) {
        this.id = id;
        this.chatId = chatId;
        this.timestamp = timestamp;
        this.calories = calories;
        this.protein = protein;
        this.fats = fats;
        this.carbs = carbs;
        this.description = description;
    }

    public Long getId() { return id; }
    public Long getChatId() { return chatId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getCalories() { return calories; }
    public int getProtein() { return protein; }
    public int getFats() { return fats; }
    public int getCarbs() { return carbs; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return String.format(
                "Meal{id=%d, chatId=%d, time=%s, cal=%d, P=%dг, F=%dг, C=%dг}",
                id, chatId, timestamp, calories, protein, fats, carbs
        );
    }
}
