package com.example.ktbapi.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TimeUtil {
    private TimeUtil() {}
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static String nowText() {
        return LocalDateTime.now().format(DTF);
    }
}
