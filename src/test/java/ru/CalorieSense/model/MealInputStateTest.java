package ru.CalorieSense.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MealInputStateTest {

    @Test
    void nextCyclesThroughAllStepsIncludingDescription() {
        MealInputState state = new MealInputState();

        assertEquals(MealInputState.Step.NONE, state.getStep());
        state.next();
        assertEquals(MealInputState.Step.CALORIES, state.getStep());
        state.next();
        assertEquals(MealInputState.Step.PROTEIN, state.getStep());
        state.next();
        assertEquals(MealInputState.Step.FATS, state.getStep());
        state.next();
        assertEquals(MealInputState.Step.CARBS, state.getStep());
        state.next();
        assertEquals(MealInputState.Step.DESCRIPTION, state.getStep());
        state.next();
        assertEquals(MealInputState.Step.NONE, state.getStep());
    }

    @Test
    void resetClearsAllFields() {
        MealInputState state = new MealInputState();
        state.setCalories(500);
        state.setProtein(30);
        state.setFats(20);
        state.setCarbs(60);
        state.setDescription("Обед");

        state.reset();

        assertEquals(MealInputState.Step.NONE, state.getStep());
        assertNull(state.getCalories());
        assertNull(state.getProtein());
        assertNull(state.getFats());
        assertNull(state.getCarbs());
        assertNull(state.getDescription());
    }
}
