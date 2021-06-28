package ru.javawebinar.topjava.model;

import java.io.Serializable;

public class MealId implements Serializable {
    private Integer id;
    private User user;

    public MealId() {
    }

    public MealId(Integer id, User user) {
        this.id = id;
        this.user = user;
    }
}
