package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class InMemoryMealDao implements Dao<Meal> {
    private static final Logger log = getLogger(InMemoryMealDao.class);
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private static final AtomicInteger sequencer = new AtomicInteger(0);

    public InMemoryMealDao() {
        Arrays.asList(
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 30), "Завтрак", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 21), "Обед", 1000),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 24), "Ужин", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 12), "Завтрак", 1000),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 45), "Обед", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 11), "Ужин", 410)
        ).forEach(this::add);
        log.debug("meals created");
    }

    @Override
    public Meal add(Meal meal) {
        int id = sequencer.getAndIncrement();
        log.debug("add meal with id {}", id);
        return meals.put(id,
                new Meal(id,
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories()));
    }

    @Override
    public boolean delete(int mealId) {
        log.debug("delete meal with id {}", mealId);
        return !Objects.equals(meals.remove(mealId), null);
    }

    @Override
    public Meal update(Meal meal) {
        int mealId = meal.getId();
        log.debug("update meal with id {}", mealId);
        meals.replace(mealId,
                new Meal(mealId,
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories()) );
        return getById(mealId).orElse(null);
    }

    @Override
    public List<Meal> getAll() {
        log.debug("get all meals");
        return new ArrayList<>(meals.values());
    }

    @Override
    public Optional<Meal> getById(int mealId) {
        log.debug("get meal with id {}", mealId);
        return Optional.ofNullable(meals.get(mealId));
    }
}
