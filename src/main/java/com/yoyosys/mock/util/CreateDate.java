package com.yoyosys.mock.util;

import com.apifan.common.random.source.DateTimeSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @Author: yjj
 * Date: 2021/10/28
 */
public class CreateDate {

    public static String randomDate(String start,String end) throws ParseException {
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate st = yyyyMMdd.parse(start).toInstant().atZone(zoneId).toLocalDate();
        LocalDate en = yyyyMMdd.parse(end).toInstant().atZone(zoneId).toLocalDate();
        return DateTimeSource.getInstance().randomDate(st,en,"yyyyMMdd");
    }

    public static String randomFutureDate(String start) throws ParseException {
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate st = yyyyMMdd.parse(start).toInstant().atZone(zoneId).toLocalDate();
        return DateTimeSource.getInstance().randomFutureDate(st,"yyyyMMdd");
    }

    public static String randomPastDate(String end) throws ParseException {
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate st = yyyyMMdd.parse(end).toInstant().atZone(zoneId).toLocalDate();
        return DateTimeSource.getInstance().randomFutureDate(st,"yyyyMMdd");
    }

    public static String input() {
        return null;
    }
}
