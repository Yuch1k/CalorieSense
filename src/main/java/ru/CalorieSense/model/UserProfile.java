package ru.CalorieSense.model;

public class UserProfile {

    private final Long chatId;
    private double weight;
    private int height;
    private int age;
    private String sex;
    private int caloriesGoal;
    private String goal; // "maintain", "lose", "gain"
    private String activityLevel; // "low", "medium", "high"

    public UserProfile(Long chatId) {
        this.chatId = chatId;
        this.caloriesGoal = 2200;
        this.goal = "maintain";
        this.activityLevel = "low";
    }

    public UserProfile(Long chatId, double weight, int height, String sex, int age) {
        this.chatId = chatId;
        this.weight = weight;
        this.height = height;
        this.sex = sex;
        this.age = age;
        this.caloriesGoal = 0;
        this.goal = "maintain";
        this.activityLevel = "low";
    }

    public Long getChatId() { return chatId; }
    public double getWeight() { return weight; }
    public int getHeight() { return height; }
    public int getAge() { return age; }
    public String getSex() { return sex; }
    public int getCaloriesGoal() { return caloriesGoal; }
    public String getGoal() { return goal; }
    public String getActivityLevel() { return activityLevel; }

    public void setWeight(double weight) { this.weight = weight; }
    public void setHeight(int height) { this.height = height; }
    public void setAge(int age) { this.age = age; }
    public void setSex(String sex) { this.sex = sex; }
    public void setCaloriesGoal(int caloriesGoal) { this.caloriesGoal = caloriesGoal; }
    public void setGoal(String goal) { this.goal = goal; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }

    public boolean isComplete() {
        return weight > 0 && height > 0 && age > 0 &&
                sex != null && !sex.isBlank();
    }

    @Override
    public String toString() {
        return String.format(
                "UserProfile{id=%d, weight=%.1f, height=%d, age=%d, sex=%s, goal=%s, activity=%s, caloriesGoal=%d}",
                chatId, weight, height, age, sex, goal, activityLevel, caloriesGoal
        );
    }
}
