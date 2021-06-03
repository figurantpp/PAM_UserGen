package com.example.usergen.model.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.util.Tags;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;

public class RandomUserGeneratorTest {

    private RandomUserGeneratorInput input;

    public RandomUserGeneratorTest withInput(RandomUserGeneratorInput input) {
        this.input = input;
        return this;
    }

    public void nextRandomModelOn(@NonNull RandomModelGenerator<User> subject) {

        try {
            testUser(subject.nextRandomModel().get());

        } catch (InterruptedException ex) {
            failInterrupted(ex);

        } catch (ExecutionException ex) {
            checkExecutionException(ex);
        }
    }

    public void nextModelsOn(@NonNull RandomModelGenerator<User> subject) {

        try {

            final int limit = 25;

            List<User> users = subject.nextModels(limit).get();

            assertNotNull(users);

            assertThat(users.size(), allOf(greaterThan(0), lessThanOrEqualTo(limit)));

            users.forEach(this::testUser);

        } catch (ExecutionException ex) {
            checkExecutionException(ex);

        } catch (InterruptedException ex) {
            failInterrupted(ex);
        }

    }

    private void checkExecutionException(ExecutionException ex) {

        assumeNotNull(ex.getCause());

        Log.e(Tags.ERROR, "checkExecutionException: ", ex);

        assertThat(ex.getCause(), instanceOf(NoSuchElementException.class));

        assumeNoException(ex.getCause());

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

    private void failInterrupted(InterruptedException ex) {
        assertNotNull(ex.getMessage());

        fail(ex.getMessage());
    }
}
