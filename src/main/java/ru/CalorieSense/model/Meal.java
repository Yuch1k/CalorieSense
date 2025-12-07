package ru.CalorieSense.model;

import java.time.LocalDateTime;

public class Meal {
    private final Long id;
    private final Long chatId;
    private LocalDateTime dateTime;
    private int calories;
    private int protein;
    private int fats;
    private int carbs;
    private String description;

    public Meal(Long id, Long chatId, LocalDateTime dateTime,
                int calories, int protein, int fats, int carbs, String description) {
        this.id = id;
        this.chatId = chatId;
        this.dateTime = dateTime;
        this.calories = calories;
        this.protein = protein;
        this.fats = fats;
        this.carbs = carbs;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Long getChatId() {
        return chatId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getCalories() {
        return calories;
    }

    public int getProtein() {
        return protein;
    }

    public int getFats() {
        return fats;
    }

    public int getCarbs() {
        return carbs;
    }

    public String getDescription() {
        return description;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
