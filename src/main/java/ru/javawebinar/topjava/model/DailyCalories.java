package ru.javawebinar.topjava.model;

import java.util.concurrent.atomic.AtomicBoolean;

public class DailyCalories {
    private int calories;

    private AtomicBoolean excess;

    public DailyCalories(int calories, AtomicBoolean excess) {
        this.calories = calories;
        this.excess = excess;
    }

    public DailyCalories() {
        this.calories = 0;
        this.excess = new AtomicBoolean();
    }

    public int getCalories() {
        return calories;
    }

    public AtomicBoolean getExcess() {
        return excess;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setExcess(AtomicBoolean excess) {
        this.excess = excess;
    }

    public DailyCalories increaseCalories(int increase) {
        this.calories += increase;
        return this;
    }
}
