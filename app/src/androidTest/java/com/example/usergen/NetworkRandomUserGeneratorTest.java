package com.example.usergen;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.model.user.NetworkRandomUserGenerator;
import com.example.usergen.model.user.NetworkRandomUserGeneratorInput;
import com.example.usergen.model.user.User;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class NetworkRandomUserGeneratorTest {

    @Test
    public void NetworkRandomUserGenerator_doesNotFail()
    {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertNotNull(context);

        NetworkRandomUserGeneratorInput input = new NetworkRandomUserGeneratorInput();

        input.setNationality("BR");
        input.setGender("male");

        RandomModelGenerator<User> subject = new NetworkRandomUserGenerator(context, input);

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
