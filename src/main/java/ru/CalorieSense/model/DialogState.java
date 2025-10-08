package ru.CalorieSense.model;

public class DialogState {
    public enum Step { NONE, HEIGHT, WEIGHT, AGE, GENDER }
    private Step step = Step.NONE;

    public Step getStep() { return step; }
    public void next() {
        switch (step) {
            case NONE -> step = Step.HEIGHT;
            case HEIGHT -> step = Step.WEIGHT;
            case WEIGHT -> step = Step.AGE;
            case AGE -> step = Step.GENDER;
            case GENDER -> step = Step.NONE;
        }
    }
    public void reset() { step = Step.NONE; }
}
