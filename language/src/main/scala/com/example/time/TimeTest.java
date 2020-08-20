package com.example.time;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeTest {
    public static void main(String[] args) {
        Calendar gmt0cal = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT0"));
        gmt0cal.set(Calendar.HOUR, 10);
        gmt0cal.set(Calendar.MINUTE, 30);
        gmt0cal.set(Calendar.SECOND, 0);
        gmt0cal.set(Calendar.MILLISECOND, 0);
        System.out.println(gmt0cal.getTime());

        long gmt0 = gmt0cal.getTime().getTime();

        Calendar slCal = Calendar.getInstance();
        slCal.set(Calendar.HOUR, 10);
        slCal.set(Calendar.MINUTE, 30);
        slCal.set(Calendar.SECOND, 0);
        slCal.set(Calendar.MILLISECOND, 0);

        long slTime = slCal.getTime().getTime();
        System.out.println("SL Time " + slTime);
        System.out.println("GMT0 Time " + gmt0);

        gmt0cal.setTime(new Date(slTime));
        System.out.println(gmt0cal.getTime());

    }
}
