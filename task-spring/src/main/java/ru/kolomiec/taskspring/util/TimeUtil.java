package ru.kolomiec.taskspring.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtil {

    public static String getCurrentTimeInHoursAndMinutesFormat() {
        SimpleDateFormat formatForHoursAndMinutes = new SimpleDateFormat("hh:mm");
        return formatForHoursAndMinutes.format(new Date());
    }
    public static String getCurrentDate() {
        SimpleDateFormat formatForYearMonthDay = new SimpleDateFormat("yyyy-MM-dd");
        return formatForYearMonthDay.format(new Date());
    }
}
