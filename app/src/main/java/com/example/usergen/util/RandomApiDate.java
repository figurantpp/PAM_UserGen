package com.example.usergen.util;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@SuppressLint("SimpleDateFormat")
public class RandomApiDate {

    @NonNull
    public static String formatRandomApiDate(@NonNull Date date) {

        SimpleDateFormat format = new SimpleDateFormat(RandomApiInfo.DATE_FORMAT_STRING);

        return format.format(date);
    }


    @NonNull
    public static Date parseRandomApiDate(@NonNull String dateString) {

        SimpleDateFormat format = new SimpleDateFormat(RandomApiInfo.DATE_FORMAT_STRING);

        try {
            return Objects.requireNonNull(format.parse(dateString));
        } catch (ParseException ex) {
            Log.e(Tags.ERROR, "Failed to parse format", ex);
            throw new RuntimeException(ex);
        }
    }
}
