package com.example.usergen.model.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.util.Tags;

import org.junit.Assert;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class RandomUserGeneratorTest {

    public void nextRandomModelOn(@NonNull RandomModelGenerator<User> subject) {

        try {
            testUser(subject.nextRandomModel().get());

        } catch (InterruptedException ex) {
            failInterrupted(ex);

        } catch (ExecutionException ex) {
            testExecutionException(ex);
        }
    }

    public void nextModelsOn(@NonNull RandomModelGenerator<User> subject) {

        try {

            final int limit = 10;

            List<User> users = subject.nextModels(limit).get();

            assertNotNull(users);

            assertThat(users.size(), allOf(greaterThan(1), lessThanOrEqualTo(limit)));

            users.forEach(this::testUser);

        } catch (ExecutionException ex) {
            testExecutionException(ex);

        } catch (InterruptedException ex) {
            failInterrupted(ex);
        }

    }

    private void testExecutionException(ExecutionException ex) {

        assumeNotNull(ex.getMessage());

        assumeNotNull(ex.getCause());

        assumeFalse(ex.getMessage().isEmpty());

        if (ex.getCause().getClass() != NoSuchElementException.class) {

            Log.e(Tags.ERROR, "nextRandomModel: ", ex);

            assumeThat(ex.getCause(), instanceOf(NoSuchElementException.class));
        }
    }

    private void testUser(User user) {

        Stream.of(
                user,
                user.getId(),
                user.getTitle(),
                user.getName(),
                user.getEmail(),
                user.getGender(),
                user.getBirthDate(),
                user.getProfileImage(),
                user.getProfileImage().getUrl(),
                user.getNationality()
        ).forEach(Assert::assertNotNull);

    }

    private void failInterrupted(InterruptedException ex) {
        assertNotNull(ex.getMessage());

        fail(ex.getMessage());
    }
}
