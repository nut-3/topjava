package ru.javawebinar.topjava.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    T add(T item);

    void delete(int mealId);

    T update(T item);

    List<T> getAll();

    Optional<T> getById(int id);
}
