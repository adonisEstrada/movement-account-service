package com.adonis.nttdata.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class DateUtils {

    private static final DateUtils INSTANCE;

    static{
        INSTANCE = new DateUtils();
    }

    private DateUtils(){

    }

    public static DateUtils instance() {
        return INSTANCE;
    }

    public Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
