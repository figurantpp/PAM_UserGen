package com.example.usergen;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.usergen.model.user.User;
import com.example.usergen.model.user.UserJsonManager;
import com.example.usergen.util.ApiInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class UserJsonManagerTest {

    @NonNull
    private Date getExpectedDate() {
        Date date = null;

        SimpleDateFormat format = new SimpleDateFormat(ApiInfo.DATE_FORMAT_STRING);

        try {
            date = format.parse("1993-07-20T09:44:18.674Z");

            if (date == null) {
                throw new ParseException("Invalid date", -1);
            }
        } catch (ParseException ex) {
            Objects.requireNonNull(ex);

            fail("ParseException: " + ex.getMessage());
        }

        return date;
    }

    @NonNull
    URL getExpectedImageUrl() {

        URL url = null;

        try {
            url = new URL("https://randomuser.me/api/portraits/med/men/75.jpg");
        } catch (MalformedURLException ex) {
            Objects.requireNonNull(ex.getMessage());

            fail("MalformedURLException: " + ex.getMessage());
        }

        return url;
    }

    @NonNull
    JSONObject getSubjectJSONObject() throws JSONException {

        JSONObject object;

        object = new JSONObject(TestStrings.USER_SAMPLE_STRING);

        return object;
    }

    @Test
    public void UserJsonManger_loadsJSON() {

        final String expectedId = "PPS 0390511T";

        final String expectedName = "brad gibson";
        final String expectedTitle = "mr";

        final String expectedEmail = "brad.gibson@example.com";

        final String expectedGender = "male";

        final Date expectedBirthDate = getExpectedDate();
        final short expectedAge = 26;

        final URL expectedImageURL = getExpectedImageUrl();

        final String expectedNationality = "IE";

        User output;

        try {
            JSONObject subjectObject = getSubjectJSONObject();

            UserJsonManager subject = new UserJsonManager();

            output = subject.getModelFromJSONObject(subjectObject);

        } catch (JSONException ex) {
            Objects.requireNonNull(ex.getMessage());

            fail("JSONException: " + ex.getMessage());

            return;
        }

        assertEquals(expectedId, output.getId());

        assertEquals(expectedTitle, output.getTitle());
        assertEquals(expectedName, output.getName());

        assertEquals(expectedEmail, output.getEmail());
        assertEquals(expectedGender, output.getGender());

        assertEquals(expectedBirthDate, output.getBirthDate());
        assertEquals(expectedAge, output.getAge());

        assertNotNull(output.getProfileImage());
        assertEquals(expectedImageURL, output.getProfileImage().getUrl());
        assertEquals(expectedNationality, output.getNationality());
    }
}
