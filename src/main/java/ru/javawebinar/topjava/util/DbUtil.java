package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.slf4j.LoggerFactory.getLogger;

public class DbUtil {
    private static List<Meal> meals = null;
    private static final Logger log = getLogger(DbUtil.class);

    public static List<Meal> getMealList() {
        if (meals == null) {
            meals = new CopyOnWriteArrayList<>(Arrays.asList(
                    new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 30), "Завтрак", 500),
                    new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 21), "Обед", 1000),
                    new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 24), "Ужин", 500),
                    new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                    new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 12), "Завтрак", 1000),
                    new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 45), "Обед", 500),
                    new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 11), "Ужин", 410)
            ));
            log.debug("Loaded meals");
        }
        return meals;
    }
}
