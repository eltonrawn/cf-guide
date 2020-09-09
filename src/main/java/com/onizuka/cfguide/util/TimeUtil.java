package com.onizuka.cfguide.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public final class TimeUtil {
    private TimeUtil() {}
    public static long getEpochBeforeNDays(long n) {
        return Instant.now().minus(n, ChronoUnit.DAYS).getEpochSecond();
    }

    /**
     * parameters should be given in milliseconds
     * */
    public static boolean isSameDay(Long day1, Long day2) {
        String sday1 = new SimpleDateFormat("dd/MM/yyyy").format(new Date(day1));
        String sday2 = new SimpleDateFormat("dd/MM/yyyy").format(new Date(day2));

        return sday1.equals(sday2);
    }
}
