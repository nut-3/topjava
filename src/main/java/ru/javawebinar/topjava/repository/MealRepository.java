package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.util.Collection;

public interface MealRepository {
    // null if updated meal do not belong to userId
    Meal save(Meal meal, User user);

    // false if meal do not belong to userId
    boolean delete(int id, User user);

    // null if meal do not belong to userId
    Meal get(int id, User user);

    // ORDERED dateTime desc
    Collection<Meal> getAll(User user);
}
