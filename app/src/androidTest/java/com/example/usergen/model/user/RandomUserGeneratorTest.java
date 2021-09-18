package com.example.usergen.model.user;

import androidx.annotation.NonNull;

import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.model.user.generator.RandomUserGeneratorInput;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeNoException;

public class RandomUserGeneratorTest {

    private RandomUserGeneratorInput input;

    public RandomUserGeneratorTest withInput(RandomUserGeneratorInput input) {
        this.input = input;
        return this;
    }

    public void nextRandomModelOn(@NonNull RandomModelGenerator<User> subject) {

        try {
            testUser(subject.nextRandomModel().blockingGet());
        } catch (NoSuchElementException ex) {

            assumeNoException(ex);
        }
    }

    public void nextModelsOn(@NonNull RandomModelGenerator<User> subject) {

        try {

            final int limit = 25;

            List<User> users = subject.nextModels(limit).blockingGet();

            assertNotNull(users);

            assertThat(users.size(), allOf(greaterThan(0), lessThanOrEqualTo(limit)));

            users.forEach(this::testUser);

        } catch (NoSuchElementException ex) {

            assumeNoException(ex);
        }

    }

    private void testUser(User user) {

        UserStorageTest.assertNotNullUser(user);

        testInputOn(user);
    }

    private void testInputOn(User user) {
        if (input != null) {
            if (input.getNationality() != null) {
                assertEquals(user.getNationality(), input.getNationality());
            }

            assertEquals(user.getGender(), input.getGender());
        }
    }
}
