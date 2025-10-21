package com.example.ktbapi.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TimeUtil {
    private TimeUtil() {}

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String nowText() {
        return LocalDateTime.now().format(FORMATTER);
    }

    public static String format(LocalDateTime time) {
        return time.format(FORMATTER);
    }
}