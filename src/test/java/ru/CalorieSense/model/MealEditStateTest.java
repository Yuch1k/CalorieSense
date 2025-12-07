package ru.CalorieSense.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MealEditStateTest {

    @Test
    void defaultStateIsNoneAndMealIdNull() {
        MealEditState state = new MealEditState();

        assertEquals(MealEditState.Step.NONE, state.getStep());
        assertNull(state.getMealId());
    }

    @Test
    void nextCyclesThroughAllSteps() {
        MealEditState state = new MealEditState();

        state.next();
        assertEquals(MealEditState.Step.CALORIES, state.getStep());

        state.next();
        assertEquals(MealEditState.Step.PROTEIN, state.getStep());

        state.next();
        assertEquals(MealEditState.Step.FATS, state.getStep());

        state.next();
        assertEquals(MealEditState.Step.CARBS, state.getStep());

        state.next();
        assertEquals(MealEditState.Step.DESCRIPTION, state.getStep());

        state.next();
        assertEquals(MealEditState.Step.DONE, state.getStep());

        state.next();
        assertEquals(MealEditState.Step.NONE, state.getStep());
    }

    @Test
    void setMealIdAndResetWorkCorrectly() {
        MealEditState state = new MealEditState();
        state.setMealId(10L);
        state.next(); // move away from NONE

        assertEquals(10L, state.getMealId());
        assertNotEquals(MealEditState.Step.NONE, state.getStep());

        state.reset();

        assertEquals(MealEditState.Step.NONE, state.getStep());
        assertNull(state.getMealId());
    }
}
