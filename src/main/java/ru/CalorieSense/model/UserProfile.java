package ru.CalorieSense.model;

public class UserProfile {
    private final Long chatId;
    private double weight;
    private int height;
    private String sex;
    private int caloriesGoal;
    private int age;


    public UserProfile(Long chatId) {
        this.chatId = chatId;
        this.caloriesGoal = 2200;
    }

    public UserProfile(Long chatId, double weight, int height, String sex, int age) {
        this.chatId = chatId;
        this.weight = weight;
        this.height = height;
        this.sex = sex;
        this.age = age;
        recalcCalories();
    }

    public Long getChatId() { return chatId; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; recalcCalories(); }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; recalcCalories(); }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; recalcCalories(); }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; recalcCalories(); }

    public int getCaloriesGoal() { return caloriesGoal; }

    private void recalcCalories() {
        if (weight > 0 && height > 0 && sex != null && age >= 14 ) {
            double bmr;
            if ("М".equalsIgnoreCase(sex)) {
                bmr = 10 * weight + 6.25 * height - 5 * age + 5;
            } else {
                bmr = 10 * weight + 6.25 * height - 5 * age - 161;
            }
            this.caloriesGoal = (int) Math.round(bmr * 1.2);
        }
    }
}
