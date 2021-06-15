package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static ru.javawebinar.topjava.util.ValidationUtil.isEmpty;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T current, T start, T end) {
        return current.compareTo(start) >= 0 && current.compareTo(end) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalTime parseTimeIso(String input) {
        if (isEmpty(input)) return null;
        return LocalTime.parse(input);
    }

    public static LocalDate parseDateIso(String input) {
        if (isEmpty(input)) return null;
        return LocalDate.parse(input);
    }
}

