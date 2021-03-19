package com.example.usergen.model.user;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.usergen.model.OnlineImageResource;
import com.example.usergen.util.ApiInfo;
import com.example.usergen.util.Tags;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class UserStorageTest {

    @Nullable
    UserStorage subject;

    @Before
    public void setup() {

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        subject = new UserStorage(context);

    }

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
    URL getExpectedURL() {

        try {
            return new URL("https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fi.redd.it%2Fmqxayon694i11.jpg&f=1&nofb=1");
        } catch (MalformedURLException ex) {
            Log.e(Tags.ERROR, "getExpectedURL: ", ex);
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void listStorageUsers() {
        List<User> users = subject.listStoredUsers();
    }

    @Test
    public void storeUser() {

        User expectedUser = new User();

        expectedUser.setId("person 001");
        expectedUser.setTitle("mr");
        expectedUser.setName("bob jhonson");
        expectedUser.setEmail("bob.j@bob.com");
        expectedUser.setGender("male");
        expectedUser.setBirthDate(getExpectedDate());
        expectedUser.setAge((short) 28);
        expectedUser.setProfileImage(new OnlineImageResource(getExpectedURL()));
        expectedUser.setNationality("CCCP");

        try {
            subject.storeModel(expectedUser);
        } catch (IOException ex) {
            fail("User storage failed");
        }

        List<User> users = subject.listStoredUsers();

        assertTrue("Empty user result list", users.size() != 0);

        User last = users.get(users.size() - 1);

        assertEquals(expectedUser.getId(), last.getId());
        assertEquals(expectedUser.getTitle(), last.getTitle());
        assertEquals(expectedUser.getName(), last.getName());
        assertEquals(expectedUser.getEmail(), last.getEmail());
        assertEquals(expectedUser.getGender(), last.getGender());
        assertEquals(expectedUser.getBirthDate(), last.getBirthDate());
        assertEquals(expectedUser.getAge(), last.getAge());
        assertEquals(expectedUser.getNationality(), last.getNationality());

        assertEquals(expectedUser.getProfileImage(), last.getProfileImage());
    }

    @Test
    public void clear() {

        subject.clear();

        List<User> users = subject.listStoredUsers();

        assertEquals(0, users.size());
    }

    @After
    public void tearDown() {
        subject.clear();
    }
}
