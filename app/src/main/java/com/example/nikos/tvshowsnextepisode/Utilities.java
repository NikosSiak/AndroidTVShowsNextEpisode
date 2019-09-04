package com.example.nikos.tvshowsnextepisode;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utilities {

    private static SimpleDateFormat df = new SimpleDateFormat("MMMM d, yyyy", Locale.US);

    public static String createURL(String show, int i) {
        if (i == 0) {
            return "https://en.wikipedia.org/wiki/List_of_" + show.replace(" ", "_") + "_episodes";
        } else {
            return "https://en.wikipedia.org/wiki/" + show.replace(" ", "_") + "_(TV_series)";
        }
    }

    public static boolean isDate(String td){
        try {
            df.parse(td);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static boolean compareDates(String today, String epDate){

        Date todayDate;
        Date episodeDate;
        try {
            todayDate = df.parse(today);
            episodeDate = df.parse(epDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return episodeDate.compareTo(todayDate) >= 0;
    }

    public static String addADay(String day) {
        Date date;
        try {
            date = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return df.format(calendar.getTime());
    }
}
