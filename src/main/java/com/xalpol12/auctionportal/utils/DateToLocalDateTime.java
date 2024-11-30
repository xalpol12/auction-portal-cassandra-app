package com.xalpol12.auctionportal.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateToLocalDateTime {
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
    }
}
