package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
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
        MealsUtil.meals.forEach(meal -> this.save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> userMeal = computeUserMeals(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMeal.put(meal.getId(), meal);
            return meal;
        }
        if (repository.entrySet().stream()
                .anyMatch(integerMapEntry -> integerMapEntry.getValue().containsKey(meal.getId()) &&
                        !integerMapEntry.getKey().equals(userId))) {
            return null;
        }
        // handle case: update, but not present in storage
        return userMeal.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete meal {} from user {}", id, userId);
        return computeUserMeals(userId)
                .remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return computeUserMeals(userId)
                .get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return computeUserMeals(userId)
                .values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getFiltered(LocalDate startDate, LocalDate endDate, int userId) {
        return computeUserMeals(userId).values().stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(),
                        startDate != null ? startDate : LocalDate.MIN,
                        endDate != null ? endDate.plusDays(1) : LocalDate.MAX))
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> computeUserMeals(int userId) {
        return repository.computeIfAbsent(userId, integer -> new ConcurrentHashMap<>());
    }
}

