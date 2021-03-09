package com.example.usergen.model.user;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.usergen.model.OnlineImageResource;
import com.example.usergen.model.interfaces.ModelJsonManager;
import com.example.usergen.util.ApiInfo;
import com.example.usergen.util.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class UserJsonManger implements ModelJsonManager<User> {

    @NonNull
    private Date dateFromString(@NonNull String dateString) {

        // We don't want locale messing up the date format.
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat(ApiInfo.DATE_FORMAT_STRING);

        Date result;

        try {
            result = format.parse(dateString);


            if (result == null)
            {
                result = new Date();
            }

        } catch (ParseException ex) {
            Log.e(Tags.ERROR, "Failed to parse format", ex);
            throw new RuntimeException(ex);
        }

        return result;
    }

    @NonNull
    @Override
    public User getModelFromJSONObject(@NonNull JSONObject sourceObject) throws JSONException {

        User user = new User();

        JSONObject nameObject = sourceObject.getJSONObject("name");
        JSONObject birthDateObject = sourceObject.getJSONObject("dob");
        JSONObject imageObject = sourceObject.getJSONObject("picture");
        JSONObject idObject = sourceObject.getJSONObject("id");

        user.setId(idObject.getString("name") + " " + idObject.getString("value"));

        user.setTitle(nameObject.getString("title"));

        String first = nameObject.getString("first");
        String last = nameObject.getString("last");

        user.setName(first + " " + last);

        user.setEmail(sourceObject.getString("email"));

        user.setAge((short) birthDateObject.getInt("age"));

        user.setGender(sourceObject.getString("gender"));

        user.setBirthDate(dateFromString(birthDateObject.getString("date")));

        user.setNationality(sourceObject.getString("nat"));

        try {
            user.setProfileImage(new OnlineImageResource(new URL(
                    imageObject.getString("medium")
            )));
        }
        catch (MalformedURLException ex)
        {
            throw new JSONException(
                    "Failed to parse image resource URL" +
                            (ex.getMessage() != null ? ex.getMessage() : ""));
        }


        return user;
    }

}
