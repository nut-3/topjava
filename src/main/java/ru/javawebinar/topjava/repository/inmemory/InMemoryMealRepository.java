package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals1.forEach(meal -> this.save(meal, 1));
        MealsUtil.meals2.forEach(meal -> this.save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            log.info("save new meal for user {}", userId);
            meal.setId(counter.incrementAndGet());
            computeUserMeals(userId).put(meal.getId(), meal);
            return meal;
        }
        log.info("save meal {} for user {}", meal.getId(), userId);
        // handle case: update, but not present in storage
        return computeUserMeals(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete meal {} from user {}", id, userId);
        return computeUserMeals(userId)
                .remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get meal {} from user {}", id, userId);
        return computeUserMeals(userId)
                .get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("get all meals from user {}", userId);
        return getFiltered(LocalDate.MIN, LocalDate.MAX, userId);
    }

    @Override
    public List<Meal> getFiltered(LocalDate startDate, LocalDate endDate, int userId) {
        log.info("get filtered meals from user {}", userId);
        return computeUserMeals(userId).values().stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> computeUserMeals(int userId) {
        return repository.computeIfAbsent(userId, integer -> new ConcurrentHashMap<>());
    }
}

