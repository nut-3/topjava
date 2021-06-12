package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> this.save(meal, new User(null, null, null, null, null)));
    }

    @Override
    public Meal save(Meal meal, User user) {
        Map<Integer, Meal> userMeal = repository.computeIfAbsent(user.getId(), integer -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMeal.put(meal.getId(), meal);
            return meal;
        }
        if (repository.entrySet().stream()
                .anyMatch(integerMapEntry -> integerMapEntry.getValue()
                        .containsKey(meal.getId()) &&
                        !integerMapEntry.getKey().equals(user.getId()))) {
            return null;
        }
        // handle case: update, but not present in storage
        return userMeal.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, User user) {
        return repository.computeIfAbsent(user.getId(), integer -> new ConcurrentHashMap<>())
                .remove(id) != null;
    }

    @Override
    public Meal get(int id, User user) {
        return repository.computeIfAbsent(user.getId(), integer -> new ConcurrentHashMap<>())
                .get(id);
    }

    @Override
    public Collection<Meal> getAll(User user) {
        return repository.computeIfAbsent(user.getId(), integer -> new ConcurrentHashMap<>())
                .values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

