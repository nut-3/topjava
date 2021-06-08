package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class InMemoryMealDao implements Dao<Meal> {
    private static final Logger log = getLogger(InMemoryMealDao.class);
    private static final Map<Integer, Meal> meals = new ConcurrentSkipListMap<>();
    private static final AtomicInteger sequencer = new AtomicInteger(0);

    public InMemoryMealDao() {
        if (meals.isEmpty()) {
            Arrays.asList(
                    new Meal(sequencer.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 30), "Завтрак", 500),
                    new Meal(sequencer.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 21), "Обед", 1000),
                    new Meal(sequencer.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 24), "Ужин", 500),
                    new Meal(sequencer.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                    new Meal(sequencer.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 12), "Завтрак", 1000),
                    new Meal(sequencer.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 45), "Обед", 500),
                    new Meal(sequencer.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 11), "Ужин", 410)
            ).forEach(meal -> meals.put(meal.getId(), meal));
            log.debug("meals created");
        }
    }

    @Override
    public Meal add(Meal meal) {
        log.debug("add meal with id " + meal.getId());
        int id = sequencer.getAndIncrement();
        meals.put(id,
                new Meal(id,
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories()));

        return meal;
    }

    @Override
    public void delete(int mealId) {
        checkId(mealId);
        log.debug("delete meal with id " + mealId);
        meals.remove(mealId);
    }

    @Override
    public Meal update(Meal meal) {
        log.debug("update meal with id " + meal.getId());
        checkId(meal.getId());
        meals.put(meal.getId(),
                new Meal(meal.getId(),
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories()));
        return meal;
    }

    @Override
    public List<Meal> getAll() {
        log.debug("get all meals");
        if (meals.isEmpty())
            return new ArrayList<>();
        return new ArrayList<>(meals.values());
    }

    @Override
    public Optional<Meal> getById(int mealId) {
        log.debug("get meal with id " + mealId);
        return Optional.of(meals.get(mealId));
    }

    private void checkId(int id) {
        if (!meals.containsKey(id))
            throw new RuntimeException("meal with id " + id + " not found!");
    }
}
