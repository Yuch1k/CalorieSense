package ru.CalorieSense.model;

public class MealEditState {

    public enum Step {
        NONE,
        CALORIES,
        PROTEIN,
        FATS,
        CARBS,
        DESCRIPTION,
        DONE
    }

    private Step step = Step.NONE;
    private Long mealId;

    public Step getStep() {
        return step;
    }

    public void next() {
        switch (step) {
            case NONE -> step = Step.CALORIES;
            case CALORIES -> step = Step.PROTEIN;
            case PROTEIN -> step = Step.FATS;
            case FATS -> step = Step.CARBS;
            case CARBS -> step = Step.DESCRIPTION;
            case DESCRIPTION -> step = Step.DONE;
            case DONE -> step = Step.NONE;
        }
    }

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public void reset() {
        step = Step.NONE;
        mealId = null;
    }
}
