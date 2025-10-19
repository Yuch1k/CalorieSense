package ru.CalorieSense.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MealInputStateTest {

    private MealInputState state;

    @BeforeEach
    void setUp() {
        state = new MealInputState();
    }

    @Test
    void testInitialState() {
        assertEquals(MealInputState.Step.NONE, state.getStep());
        assertNull(state.getCalories());
        assertNull(state.getProtein());
        assertNull(state.getFats());
    }

    @Test
    void testNext_FullCycle() {
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
        assertEquals(MealInputState.Step.NONE, state.getStep());
    }

    @Test
    void testSetters() {
        state.setCalories(500);
        state.setProtein(30);
        state.setFats(15);

        assertEquals(500, state.getCalories());
        assertEquals(30, state.getProtein());
        assertEquals(15, state.getFats());
    }

    @Test
    void testReset_ClearsData() {
        state.setStep(MealInputState.Step.PROTEIN);
        state.setCalories(500);
        state.setProtein(30);
        state.setFats(15);

        state.reset();

        assertEquals(MealInputState.Step.NONE, state.getStep());
        assertNull(state.getCalories());
        assertNull(state.getProtein());
        assertNull(state.getFats());
    }

    @Test
    void testSetStep() {
        state.setStep(MealInputState.Step.FATS);

        assertEquals(MealInputState.Step.FATS, state.getStep());
    }
}
