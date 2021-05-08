package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class UserMeal implements Meals {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    public UserMeal(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        if (!ALL_MEALS.contains(dateTime))
            DAILY_CALORIES.put(dateTime.toLocalDate(), DAILY_CALORIES.getOrDefault(dateTime.toLocalDate(), 0) + calories);
    }

    @Override
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getCalories() {
        return calories;
    }

    @Override
    public int getDailyCalories() {
        return DAILY_CALORIES.get(this.getDateTime().toLocalDate());
    }
}
