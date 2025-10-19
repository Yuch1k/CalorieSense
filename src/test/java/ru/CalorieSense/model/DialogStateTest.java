package ru.CalorieSense.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DialogStateTest {

    private DialogState state;

    @BeforeEach
    void setUp() {
        state = new DialogState();
    }

    @Test
    void testInitialState() {
        assertEquals(DialogState.Step.NONE, state.getStep());
    }

    @Test
    void testNext_FullCycle() {
        assertEquals(DialogState.Step.NONE, state.getStep());

        state.next();
        assertEquals(DialogState.Step.HEIGHT, state.getStep());

        state.next();
        assertEquals(DialogState.Step.WEIGHT, state.getStep());

        state.next();
        assertEquals(DialogState.Step.AGE, state.getStep());

        state.next();
        assertEquals(DialogState.Step.GENDER, state.getStep());

        state.next();
        assertEquals(DialogState.Step.GOAL, state.getStep());

        state.next();
        assertEquals(DialogState.Step.ACTIVITY, state.getStep());

        state.next();
        assertEquals(DialogState.Step.NONE, state.getStep());
    }

    @Test
    void testReset() {
        state.next();
        state.next();
        state.next();
        assertEquals(DialogState.Step.AGE, state.getStep());

        state.reset();

        assertEquals(DialogState.Step.NONE, state.getStep());
    }

    @Test
    void testMultipleResets() {
        state.reset();
        assertEquals(DialogState.Step.NONE, state.getStep());

        state.reset();
        assertEquals(DialogState.Step.NONE, state.getStep());
    }
}
