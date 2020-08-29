package com.onizuka.cfguide.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class TimeUtil {
    public static Long getEpochBeforeNDays(Long n) {
        return Instant.now().minus(n, ChronoUnit.DAYS).getEpochSecond();
    }
}
