package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicInteger;

public class SequenceUtil {
    private static final SequenceUtil mealSequence = new SequenceUtil();

    private final AtomicInteger sequenceNumber = new AtomicInteger(0);

    public int next() {
        return sequenceNumber.getAndIncrement();
    }

    public static SequenceUtil ofMeal() {
        return mealSequence;
    }
}
