package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int NOT_FOUND = 10;
    public static final int ADMIN_LUNCH_ID = START_SEQ + 2;
    public static final int ADMIN_DINNER_ID = START_SEQ + 3;
    public static final Meal adminLunch = new Meal(ADMIN_LUNCH_ID,
            LocalDateTime.of(2015, Month.JUNE, 1, 14, 0),
            "Админ ланч",
            510);
    public static final Meal adminDinner = new Meal(ADMIN_DINNER_ID,
            LocalDateTime.of(2015, Month.JUNE, 1, 21, 0),
            "Админ ужин",
            1500);
    public static final List<Meal> userMeals;

    static {
        AtomicInteger cequencerId = new AtomicInteger(ADMIN_DINNER_ID);
        userMeals = MealsUtil.meals.stream()
                .peek(meal -> meal.setId(cequencerId.incrementAndGet()))
                .collect(Collectors.toList());
    }

    public static Meal getNew() {
        return new Meal(null,
                LocalDateTime.of(2021, Month.JUNE, 1, 9, 40),
                "Новый завтрак",
                500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(adminLunch);
        updated.setCalories(2000);
        updated.setDateTime(updated.getDateTime().plus(1, ChronoUnit.DAYS));
        updated.setDescription("Измененный админ ланч");
        return updated;
    }

    public static List<Meal> getUserFilteredMeals() {
        return userMeals.stream()
                .filter(meal -> meal.getDateTime()
                        .compareTo(LocalDateTime.of(LocalDate.of(2020, Month.JANUARY, 31),
                                LocalTime.MIDNIGHT)) < 0)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
