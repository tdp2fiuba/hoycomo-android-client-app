package com.ar.tdp2fiuba.hoycomo.utils;

import android.content.Context;

import com.ar.tdp2fiuba.hoycomo.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.ISODateTimeFormat;

public class DateUtils {

    public static Integer secToRoundedMin(Double seconds) {
        return (int) (seconds / 60D);
    }

    public static String formatTime(String time) {
        String[] units = time.split(":");
        return units[1].equals("00") ? units[0] : units[0] + ":" + units[1];
    }

    public static String elapsedTimeFromUTC(String utcTimestamp, Context context) {
        DateTime now = DateTime.now(DateTimeZone.getDefault());

        DateTime dateTime = ISODateTimeFormat.dateTimeParser()
                .parseDateTime(utcTimestamp)
                .toDateTime(DateTimeZone.getDefault());

        Period period = new Period(dateTime, now);

        if (period.getHours() > 0) {
            if (period.getHours() == 1) {
                return context.getString(R.string.one_hour_ago);
            } else {
                return context.getString(R.string.hours_ago)
                        .replace(":h", String.valueOf(period.getHours()));
            }
        } else {
            if (period.getMinutes() <= 1) {
                return context.getString(R.string.recently);
            } else {
                return context.getString(R.string.minutes_ago)
                        .replace(":m", String.valueOf(period.getMinutes()));
            }
        }
    }

    public static String formatFromUTC(String utcTimestamp) {
        DateTime dateTime = ISODateTimeFormat.dateTimeParser()
                .parseDateTime(utcTimestamp)
                .toDateTime(DateTimeZone.getDefault());
        return dateTime.toString("dd/MM/yyyy HH:mm");
    }

    private static boolean isToday(DateTime dateTime) {
        DateTime startOfDay = DateTime.now(DateTimeZone.getDefault()).withTimeAtStartOfDay();
        Interval today = new Interval(startOfDay, Days.ONE);
        return today.contains(dateTime);
    }
}
