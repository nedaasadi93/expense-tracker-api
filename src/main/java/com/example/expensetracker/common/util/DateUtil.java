package com.example.expensetracker.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

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

    public static LocalDateTime startOfMonth(int year, int month) {
        return LocalDateTime.of(year, month, 1, 0, 0, 0, 0);
    }


    public static LocalDateTime endOfMonth(int year, int month) {
        LocalDate lastDay = YearMonth.of(year, month).atEndOfMonth();
        return LocalDateTime.of(lastDay, LocalDateTime.MAX.toLocalTime());
    }
}
