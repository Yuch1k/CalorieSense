package ru.CalorieSense.model;

public class UserProfile {
    private double weight;
    private int height;
    private String sex;
    private int caloriesGoal;
    private int age;

    public UserProfile() {
        this.caloriesGoal = 2200; // дефолтная цель
    }

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
        if (weight <= 0 || height <= 0 || gender == null) {
            return;
        }
        double bmr;
        if ("M".equalsIgnoreCase(gender)) {
            bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        }
        this.caloriesGoal = (int) Math.round(bmr * 1.2);
    }
}
