package ru.javawebinar.topjava;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int NOT_FOUND = 20;
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
    public static final Meal userMealBreakfast30 = new Meal(START_SEQ + 4,
            LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
            "Завтрак",
            500);
    public static final Meal userMealLunch30 = new Meal(START_SEQ + 5,
            LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
            "Обед",
            1000);
    public static final Meal userMealDinner30 = new Meal(START_SEQ + 6,
            LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
            "Ужин",
            500);
    public static final Meal userMealBorder31 = new Meal(START_SEQ + 7,
            LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
            "Еда на граничное значение",
            100);
    public static final Meal userMealBreakfast31 = new Meal(START_SEQ + 8,
            LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
            "Завтрак",
            1000);
    public static final Meal userMealLunch31 = new Meal(START_SEQ + 9,
            LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
            "Обед",
            500);
    public static final Meal userMealDinner31 = new Meal(START_SEQ + 10,
            LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
            "Ужин",
            410);

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

    public static List<Meal> getSortedListOf(Meal... meals) {
        return Stream.of(meals)
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
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
