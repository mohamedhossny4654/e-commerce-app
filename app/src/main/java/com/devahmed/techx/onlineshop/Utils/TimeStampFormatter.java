package com.devahmed.techx.onlineshop.Utils;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class TimeStampFormatter {

    public TimeStampFormatter() {
    }

    //FN to conver timestamp object from long data type into a readable string
    public static String timeStampToString(long timeStamp){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timeStamp);
        String date = DateFormat.format("dd/MM/yyyy" , calendar).toString();
        String time = DateFormat.format("HH:MM" , calendar).toString();
        return date + "     at      " + time;
    }
}
