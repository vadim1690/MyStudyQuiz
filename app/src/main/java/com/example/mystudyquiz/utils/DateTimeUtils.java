package com.example.mystudyquiz.utils;

import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {


    /**
     * Returns the current date and time as a Date object.
     *
     * @return The current date and time.
     */
    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * Returns the current year as an int.
     *
     * @return The current year.
     */
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Returns the current month as an int, with January being represented by 1 and December by 12.
     *
     * @return The current month.
     */
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * Returns the current day of the month as an int.
     *
     * @return The current day of the month.
     */
    public static int getCurrentDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Returns the current hour of the day as an int, in 24-hour format.
     *
     * @return The current hour of the day.
     */
    public static int getCurrentHourOfDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Returns the current minute of the hour as an int.
     *
     * @return The current minute of the hour.
     */
    public static int getCurrentMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }
}
