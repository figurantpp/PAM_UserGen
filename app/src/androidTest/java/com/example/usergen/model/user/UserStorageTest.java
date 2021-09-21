package com.example.usergen.model.user;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.usergen.model.User;
import com.example.usergen.service.http.OnlineImageResource;
import com.example.usergen.service.storage.UserStorage;
import com.example.usergen.util.RandomApiInfo;
import com.example.usergen.util.Tags;

import org.junit.Assert;
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
import java.util.stream.Stream;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeThat;

@RunWith(AndroidJUnit4.class)
public class UserStorageTest {

    @Nullable
    UserStorage subject;

    @Before
    public void setup() {

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        subject = new UserStorage(context);

    }

    @Test
    public void listStoredUsers() {

        assertNotNull(subject);

        List<User> users = subject.listStoredUsers();

        assertNotNull(users);

        users.forEach(UserStorageTest::assertNotNullUser);

    }

    public static void assertNotNullUser(@NonNull User user) {

        Stream.of(
                user,
                user.getSourceId(),
                user.getTitle(),
                user.getName(),
                user.getEmail(),
                user.getGender(),
                user.getBirthDate(),
                user.getProfileImage(),
                user.getNationality()
        ).forEach(Assert::assertNotNull);

    }

    @Test
    public void storeUser() {

        assertNotNull(subject);

        withBackup(subject, () -> {

            User expectedUser = new User(
                    "person 001",
                    "mr",
                    "bob jhonson",
                    "bob.j@bob.com",
                    "male",
                    getExpectedDate(),
                    (short) 28,
                    "US",
                    new OnlineImageResource(getExpectedURL())
            );


            try {
                subject.storeModel(expectedUser);
            } catch (IOException ex) {
                assumeNoException(ex);
            }

            List<User> users = subject.listStoredUsers();

            assertTrue("Empty user result list", users.size() != 0);

            User last = users.get(users.size() - 1);

            assertEquals(expectedUser.getSourceId(), last.getSourceId());
            assertEquals(expectedUser.getTitle(), last.getTitle());
            assertEquals(expectedUser.getName(), last.getName());
            assertEquals(expectedUser.getEmail(), last.getEmail());
            assertEquals(expectedUser.getGender(), last.getGender());
            assertEquals(expectedUser.getBirthDate(), last.getBirthDate());
            assertEquals(expectedUser.getAge(), last.getAge());
            assertEquals(expectedUser.getNationality(), last.getNationality());

            assertEquals(expectedUser.getProfileImage(), last.getProfileImage());

        });
    }

    @Test
    public void clear() {

        assertNotNull(subject);

        withBackup(
                subject,
                () -> {
                    subject.clear();
                    List<User> users = subject.listStoredUsers();
                    assertEquals(0, users.size());
                }
        );
    }


    public static void withBackup(@NonNull UserStorage storage, @NonNull Runnable action) {

        List<User> previousUsers = storage.listStoredUsers();

        assumeThat(previousUsers.size(), is(greaterThan(0)));

        try {
            action.run();
        } finally {
            storage.clear();

            for (User user : previousUsers) {
                try {
                    storage.storeModel(user);
                } catch (IOException ex) { /**/ }
            }
        }

    }

    @NonNull
    private Date getExpectedDate() {
        Date date = null;

        SimpleDateFormat format = new SimpleDateFormat(RandomApiInfo.DATE_FORMAT_STRING);

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
            return new URL("https://i.redd.it/mqxayon694i11.jpg");
        } catch (MalformedURLException ex) {
            Log.e(Tags.ERROR, "getExpectedURL: ", ex);
            throw new RuntimeException(ex);
        }
    }

}
