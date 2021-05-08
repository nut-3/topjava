package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Meals {
    //Статическая Map для хранения калорий за день
    Map<LocalDate, Integer> DAILY_CALORIES = new HashMap<>();

    //Статический List для хранения времени всех приёмов пищи
    List<LocalDateTime> ALL_MEALS = new ArrayList<>();

    LocalDateTime getDateTime();

    String getDescription();

    int getCalories();

    /**
     * Метод для получения дневного потребления калорий для текущего приёма пищи
     * @return кол-во калорий
     */
    int getDailyCalories();
}
