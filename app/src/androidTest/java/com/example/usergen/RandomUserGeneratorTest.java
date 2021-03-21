package com.example.usergen;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.model.user.User;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class RandomUserGeneratorTest {

    public void nextRandomModel(@NonNull Context context,
                                @NonNull RandomModelGenerator<User> subject) {

        assertNotNull(context);

        Future<User> output = subject.nextRandomModel();

        User user;

        try {
            user = output.get();
        } catch (InterruptedException | ExecutionException ex) {

            Objects.requireNonNull(ex.getMessage());

            fail(ex.getMessage());
            return;
        }

        assertNotNull(user);

        assertNotNull(user.getId());
        assertNotNull(user.getTitle());
        assertNotNull(user.getName());
        assertNotNull(user.getEmail());
        assertNotNull(user.getGender());
        assertNotNull(user.getBirthDate());
        assertNotNull(user.getProfileImage());
        assertNotNull(user.getProfileImage().getUrl());
        assertNotNull(user.getNationality());
    }
}
