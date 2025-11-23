package com.example.expensetracker.common.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

public final class DateUtil {

    private DateUtil() {
    }

    public static LocalDateTime startOfMonth(LocalDateTime dateTime) {
        return dateTime
                .withDayOfMonth(1)
                .with(LocalTime.MIN);
    }

    public static LocalDateTime endOfMonth(LocalDateTime dateTime) {
        return dateTime
                .withDayOfMonth(dateTime.toLocalDate().lengthOfMonth())
                .with(LocalTime.MAX);
    }
}
