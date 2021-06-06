package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbUtil;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.slf4j.LoggerFactory.getLogger;

public class MealDaoInMemory implements GenericDao<Meal> {
    private static final Logger log = getLogger(MealDaoInMemory.class);
    private static final SortedMap<Integer, Meal> meals = new TreeMap<>();

    public MealDaoInMemory() {
        DbUtil.getMealList().forEach(meal -> meals.put(meal.getId(), meal));
    }

    @Override
    public void add(Meal meal) {
        meals.put(meal.getId(), meal);
        log.debug("add meal with id " + meal.getId());
    }

    @Override
    public void delete(int mealId) {
        meals.remove(mealId);
        log.debug("delete meal with id " + mealId);
    }

    @Override
    public void update(Meal meal) {
        delete(meal.getId());
        add(meal);
        log.debug("update meal with id " + meal.getId());
    }

    @Override
    public List<Meal> getAll() {
        log.debug("get all meals");
        if (meals.isEmpty())
            return null;
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal getById(int mealId) throws InvalidKeyException {
        log.debug("get meal with id " + mealId);
        if (!meals.containsKey(mealId)) {
            throw new InvalidKeyException();
        }
        return meals.get(mealId);
    }
}
