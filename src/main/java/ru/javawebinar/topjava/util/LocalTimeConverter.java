package ru.javawebinar.topjava.util;


import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalTime;

public final class LocalTimeConverter implements Converter<String, LocalTime> {

    @Nullable
    @Override
    public LocalTime convert(@Nullable String source) {
        return DateTimeUtil.parseLocalTime(source);
    }

    @NonNull
    @Override
    public <U> Converter<String, U> andThen(@NonNull Converter<? super LocalTime, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
