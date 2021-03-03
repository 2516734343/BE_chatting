package com.server.be_chatting.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class DateUtil {
    public static final String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYYMMDDTHHMMSSSSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    public static final String YYYYMMDDHHMMSSZ = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YYYYMMDDHH = "yyyy-MM-dd HH";
    public static final String YYYYMMDD = "yyyy-MM-dd";
    public static final String YYYYMM = "yyyy-MM";
    public static final String YYYY = "yyyy";

    public static String format(Long time, String format) {
        return format(time, format, null);
    }

    public static String format(Long time, String format, TimeZone timeZone) {
        if (time == null || format == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            if (timeZone != null) {
                sdf.setTimeZone(timeZone);
            }
            return sdf.format(new Date(time));
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    /**
     * 毫秒值转换为日期，返回t+days天.
     *
     * @param timestamp 日期毫秒值
     * @param plusDays 要增加的天数
     */
    public static String timeToDateStr(long timestamp, int plusDays, String pattern) {
        return new SimpleDateFormat(pattern).format(new Date(plusDays(timestamp, plusDays)));
    }

    /**
     * 毫秒转毫秒，返回t+days天后的时间毫秒值
     *
     * @param timestamp 日期毫秒值
     * @param plusDays 要增加的天数
     */
    public static long plusDays(long timestamp, int plusDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));
        calendar.add(Calendar.DAY_OF_MONTH, plusDays);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取精确到小时的时间戳
     */
    public static long getTimeHour(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取精确到天的时间戳
     */
    public static long getTimeDay(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 将日期字符串转换为对应的毫秒值
     */
    public static long dateStrToTime(String dateStr, String pattern) throws ParseException {
        return new SimpleDateFormat(pattern).parse(dateStr).getTime();
    }

    /**
     * 根据指定格式获取当前日期字符串
     */
    public static String getCurrentDate(String pattern) {
        LocalDateTime dateTime = LocalDateTime.now();
        dateTime.atOffset(ZoneOffset.ofHours(8));
        String date = dateTime.format(DateTimeFormatter.ofPattern(pattern));
        return date;
    }

    public static long dateStrToTime(String dateStr) throws ParseException {
        String format = null;
        if (YYYY.length() == dateStr.length()) {
            format = YYYY;
        } else if (YYYYMM.length() == dateStr.length()) {
            format = YYYYMM;
        } else if (YYYYMMDD.length() == dateStr.length()) {
            format = YYYYMMDD;
        } else if (YYYYMMDDHH.length() == dateStr.length()) {
            format = YYYYMMDDHH;
        }
        return new SimpleDateFormat(format).parse(dateStr).getTime();
    }

    /**
     * 将2016-08-15T16:00:00.000Z格式转换为long类型毫秒数
     */
    public static Long formatDate(String date) throws ParseException {
        date = date.replace("Z", " UTC");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        Date time = format.parse(date);
        return time.getTime();
    }

    public static long get30DaysBefore() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        c.add(Calendar.MONTH, -1);
        return c.getTimeInMillis();
    }

    public static String dueTimeToExpiredAt(Long dueTime) {
        if (dueTime == null || dueTime <= 0L) {
            return "";
        }
        return DateUtil.format(dueTime, DateUtil.YYYYMMDD);
    }

    public static Long expiredAtToDueTime(String expiredAt) {
        if (StringUtils.isEmpty(expiredAt)) {
            return 0L;
        }
        DateTime today = new DateTime().withTimeAtStartOfDay();
        String todayString = today.toString(DateUtil.YYYYMMDD);
        if (expiredAt.equals(todayString)) {
            return today.plusDays(1).plusHours(8).plusMinutes(5).getMillis();
        }
        DateTime date = DateTime.parse(expiredAt, DateTimeFormat.forPattern(DateUtil.YYYYMMDD));
        return date.plusHours(8).plusMinutes(5).getMillis();
    }

    public static String getNextDayIfToday(String yyyyMMdd) {
        if (StringUtils.isEmpty(yyyyMMdd)) {
            return yyyyMMdd;
        }
        DateTime today = new DateTime().withTimeAtStartOfDay();
        String todayString = today.toString(DateUtil.YYYYMMDD);
        if (yyyyMMdd.equals(todayString)) {
            today = today.plusDays(1);
            return DateUtil.format(today.getMillis(), DateUtil.YYYYMMDD);
        }
        return yyyyMMdd;
    }

    public static boolean expiredAtBeforeToday(String expiredAt) {
        if (StringUtils.isEmpty(expiredAt)) {
            return false;
        }
        DateTime date = DateTime.parse(expiredAt, DateTimeFormat.forPattern(DateUtil.YYYYMMDD));
        DateTime today = new DateTime().withTimeAtStartOfDay();
        return date.isBefore(today);
    }
}