package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T current, T start, T end) {
        return current.compareTo(start) >= 0 && current.compareTo(end) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalTime parseTime(String input) {
        if (checkInput(input)) return null;
        return LocalTime.parse(input, TIME_FORMATTER);
    }

    public static LocalDate parseDate(String input) {
        if (checkInput(input)) return null;
        return LocalDate.parse(input, DATE_FORMATTER);
    }

    private static boolean checkInput(String input) {
        return (input == null || input.equals(""));
    }
}

