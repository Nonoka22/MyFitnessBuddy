package com.example.myfitnessbuddy.models;

public class Matcher {
    private boolean firstCriteria;
    private boolean secondCriteria;
    private int matchCounter;

    public Matcher() {
    }

    public boolean isFirstCriteria() {
        return firstCriteria;
    }

    public void setFirstCriteria(boolean firstCriteria) {
        this.firstCriteria = firstCriteria;
    }

    public boolean isSecondCriteria() {
        return secondCriteria;
    }

    public void setSecondCriteria(boolean secondCriteria) {
        this.secondCriteria = secondCriteria;
    }

    public int getMatchCounter() {
        return matchCounter;
    }

    public void setMatchCounter(int matchCounter) {
        this.matchCounter = matchCounter;
    }
}
