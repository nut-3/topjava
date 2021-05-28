package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collector;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println();

        List<UserMealWithExcess> mealsToTo = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToTo.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        class DailyCalories {
            private int calories;

            private final AtomicBoolean excess;

            public DailyCalories() {
                this.calories = 0;
                this.excess = new AtomicBoolean();
            }

            public int getCalories() {
                return calories;
            }

            public AtomicBoolean getExcess() {
                return excess;
            }

            public DailyCalories increaseCalories(int increase) {
                this.calories += increase;
                return this;
            }
        }

        Map<LocalDate, DailyCalories> dailyCalories = new HashMap<>();
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();

        meals.forEach(userMeal -> {
            LocalDate mealDate = userMeal.getDateTime().toLocalDate();
            DailyCalories currDailyCalories = dailyCalories.computeIfAbsent(mealDate, localDate -> new DailyCalories());
            currDailyCalories.increaseCalories(userMeal.getCalories())
                    .getExcess()
                    .set(currDailyCalories.getCalories() > caloriesPerDay);

            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealWithExcesses.add(new UserMealWithExcess(
                        userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        currDailyCalories.getExcess()));
            }
        });
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream().collect(
                Collector.of(
                        HashMap::new,
                        (HashMap<LocalDate, ArrayList<UserMeal>> userMeals, UserMeal userMeal) -> {
                            LocalDate mealDate = userMeal.getDateTime().toLocalDate();
                            userMeals.computeIfAbsent(mealDate, localDate -> new ArrayList<>()).add(userMeal);
                        },
                        (HashMap<LocalDate, ArrayList<UserMeal>> dailyMeals1,
                         HashMap<LocalDate, ArrayList<UserMeal>> dailyMeals2) -> {
                            dailyMeals2.forEach((localDate, userMeals) -> dailyMeals1.merge(localDate, userMeals,
                                    (list1, list2) -> {
                                        list1.addAll(list2);
                                        return list1;
                                    }));
                            return dailyMeals1;
                        },
                        (HashMap<LocalDate, ArrayList<UserMeal>> dailyMeals) -> {
                            List<UserMealWithExcess> userMealsWithExcesses = new ArrayList<>();

                            dailyMeals.forEach((localDate, userMeals) -> {
                                AtomicBoolean dailyExcess = new AtomicBoolean();
                                dailyExcess.set(userMeals.stream().peek(userMeal -> {
                                    if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                                        userMealsWithExcesses.add(new UserMealWithExcess(userMeal.getDateTime(),
                                                userMeal.getDescription(),
                                                userMeal.getCalories(),
                                                dailyExcess));
                                    }
                                }).mapToInt(UserMeal::getCalories).sum() > caloriesPerDay);
                            });
                            return userMealsWithExcesses;
                        }
                ));
    }
}
