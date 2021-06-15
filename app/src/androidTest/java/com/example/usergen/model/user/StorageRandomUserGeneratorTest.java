package com.example.usergen.model.user;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.usergen.model.user.generator.RandomUserGeneratorInput;
import com.example.usergen.model.user.generator.StorageRandomUserGenerator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

import static com.example.usergen.model.user.UserStorageTest.withBackup;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class StorageRandomUserGeneratorTest {

    private RandomUserGeneratorTest test;
    private UserStorage storage;

    @Before
    public void before() {

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        storage = new UserStorage(context);

        test = new RandomUserGeneratorTest();

    }


    @Test
    public void nextRandomModel() {

        StorageRandomUserGenerator generator = new StorageRandomUserGenerator(storage);

        test.nextRandomModelOn(generator);
    }

    @Test
    public void testNextModels() {

        StorageRandomUserGenerator generator = new StorageRandomUserGenerator(storage);

        test.nextModelsOn(generator);

    }

    @Test
    public void testEmptyModels() {

        withBackup(storage, () -> {

            storage.clear();

            StorageRandomUserGenerator generator = new StorageRandomUserGenerator(storage);

            try {
                generator.nextRandomModel().get();
                fail("Exception was not thrown");
            }
            catch (ExecutionException ex) {
                assertThat(ex.getCause(), instanceOf(NoSuchElementException.class));
            }
            catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

    }

    @Test
    public void testFemaleGenderInput() {
        testGenderInput("female");
    }

    @Test
    public void testMaleGenderInput() {
        testGenderInput("male");
    }

    public void testNationalityInput() {
        RandomUserGeneratorInput input = new RandomUserGeneratorInput(
                "US", "male"
        );

        StorageRandomUserGenerator generator = new StorageRandomUserGenerator(
                storage, input
        );

        test.withInput(input).nextModelsOn(generator);
    }

    private void testGenderInput(String gender) {

        RandomUserGeneratorInput input = new RandomUserGeneratorInput(
                null, gender
        );

        StorageRandomUserGenerator generator = new StorageRandomUserGenerator(
                storage,
                input
        );

        test.withInput(input).nextModelsOn(generator);

    }


}
