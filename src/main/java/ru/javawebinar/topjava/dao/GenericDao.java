package ru.javawebinar.topjava.dao;

import java.security.InvalidKeyException;
import java.util.List;

public interface GenericDao<T> {
    void add(T item);

    void delete(int mealId);

    void update(T item);

    List<T> getAll();

    T getById(int id) throws InvalidKeyException;
}
