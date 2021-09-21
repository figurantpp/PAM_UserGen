package com.example.usergen.util;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class UserApiDate {

    @NonNull
    public static String formatUserApiDate(@NonNull Date date) {

        return new SimpleDateFormat(UserApiInfo.DATE_FORMAT_STRING).format(date);
    }


    @NonNull
    public static Date parseUserApiDate(@NonNull String dateString) {

        SimpleDateFormat format = new SimpleDateFormat(UserApiInfo.DATE_FORMAT_STRING);

        try {
            return format.parse(dateString);
        } catch (ParseException ex) {
            Log.e(Tags.ERROR, "Failed to parse format", ex);
            throw new RuntimeException(ex);
        }
    }
}
