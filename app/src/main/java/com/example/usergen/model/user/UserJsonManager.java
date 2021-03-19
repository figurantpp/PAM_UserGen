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

        user.setBirthDate(ApiDate.dateFromString(birthDateObject.getString("date")));

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
