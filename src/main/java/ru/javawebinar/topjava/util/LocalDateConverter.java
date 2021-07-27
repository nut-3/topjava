package ru.javawebinar.topjava.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

public class LocalDateConverter implements Converter<String, LocalDate> {

    @Nullable
    @Override
    public LocalDate convert(@Nullable String source) {
        if (source == null || source.equals("")) {
            return null;
        }
        return LocalDate.parse(source);
    }

    @NonNull
    @Override
    public <U> Converter<String, U> andThen(@NonNull Converter<? super LocalDate, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
