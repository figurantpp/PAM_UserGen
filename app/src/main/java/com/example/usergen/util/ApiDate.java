package com.example.usergen.util;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApiDate {

    @NonNull
    public static String formatDate(@NonNull Date date) {

        // this should be locale independent
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat(ApiInfo.DATE_FORMAT_STRING);

        return format.format(date);
    }


    @NonNull
    public static Date dateFromString(@NonNull String dateString) {

        // We don't want locale messing up the date format.
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat(ApiInfo.DATE_FORMAT_STRING);

        Date result;

        try {
            result = format.parse(dateString);


            if (result == null) {
                result = new Date();
            }

        } catch (ParseException ex) {
            Log.e(Tags.ERROR, "Failed to parse format", ex);
            throw new RuntimeException(ex);
        }

        return result;
    }
}
