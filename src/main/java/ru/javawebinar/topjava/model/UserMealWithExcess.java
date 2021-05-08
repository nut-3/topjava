package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class UserMealWithExcess implements Meals {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final boolean excess;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
        if (!ALL_MEALS.contains(dateTime))
            DAILY_CALORIES.put(dateTime.toLocalDate(), DAILY_CALORIES.getOrDefault(dateTime.toLocalDate(), 0) + calories);
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }

    @Override
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getCalories() {
        return this.calories;
    }

    @Override
    public int getDailyCalories() {
        return DAILY_CALORIES.get(this.getDateTime().toLocalDate());
    }
}
