package com.example.usergen.model.user;

import androidx.annotation.NonNull;

import com.example.usergen.model.OnlineImageResource;
import com.example.usergen.model.interfaces.ModelJsonManager;
import com.example.usergen.util.ApiDate;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class UserJsonManager implements ModelJsonManager<User> {


    @NonNull
    @Override
    public User getModelFromJSONObject(@NonNull JSONObject sourceObject) throws JSONException {

        JSONObject nameObject = sourceObject.getJSONObject("name");
        JSONObject birthDateObject = sourceObject.getJSONObject("dob");
        JSONObject imageObject = sourceObject.getJSONObject("picture");
        JSONObject idObject = sourceObject.getJSONObject("id");

        String first = nameObject.getString("first");
        String last = nameObject.getString("last");

        OnlineImageResource imageResource;

        try {
            imageResource = new OnlineImageResource(
                    new URL(imageObject.getString("large"))
            );
        } catch (MalformedURLException ex) {
            throw new JSONException(
                    "Failed to parse image resource URL" +
                            (ex.getMessage() != null ? ex.getMessage() : ""));
        }

        return new User(

                idObject.getString("name") + " " + idObject.getString("value"),
                nameObject.getString("title"),
                first + " " + last,
                sourceObject.getString("email"),
                sourceObject.getString("gender"),
                ApiDate.dateFromString(birthDateObject.getString("date")),
                (short) birthDateObject.getInt("age"),
                sourceObject.getString("nat"),
                imageResource
        );

    }

}
