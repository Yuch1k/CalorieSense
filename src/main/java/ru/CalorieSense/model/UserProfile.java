package ru.CalorieSense.model;


public class UserProfile {

    private final Long chatId;
    private double weight;     // вес, кг
    private int height;        // рост, см
    private int age;           // возраст, лет
    private String sex;        // пол: M или F
    private int caloriesGoal;  // рассчитанная дневная норма калорий

    public UserProfile(Long chatId) {
        this.chatId = chatId;
        this.caloriesGoal = 2200; // дефолтная цель до регистрации
    }

    public UserProfile(Long chatId, double weight, int height, String sex, int age) {
        this.chatId = chatId;
        this.weight = weight;
        this.height = height;
        this.sex = sex;
        this.age = age;
        this.caloriesGoal = 0;
    }

    public Long getChatId() { return chatId; }
    public double getWeight() { return weight; }
    public int getHeight() { return height; }
    public int getAge() { return age; }
    public String getSex() { return sex; }
    public int getCaloriesGoal() { return caloriesGoal; }

    public void setWeight(double weight) { this.weight = weight; }
    public void setHeight(int height) { this.height = height; }
    public void setAge(int age) { this.age = age; }
    public void setSex(String sex) { this.sex = sex; }

    public void setCaloriesGoal(int caloriesGoal) {
        this.caloriesGoal = caloriesGoal;
    }

    public boolean isComplete() {
        return weight > 0 && height > 0 && age > 0 &&
                sex != null && !sex.isBlank();
    }

    @Override
    public String toString() {
        return String.format(
                "UserProfile{id=%d, weight=%.1f, height=%d, age=%d, sex=%s, caloriesGoal=%d}",
                chatId, weight, height, age, sex, caloriesGoal
        );
    }
}
