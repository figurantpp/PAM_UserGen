package com.example.usergen.model.user;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

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
    public void testEmptyModels() throws InterruptedException {

        storage.clear();

        StorageRandomUserGenerator generator = new StorageRandomUserGenerator(storage);

        try {
            generator.nextRandomModel().get();
        }
        catch (ExecutionException ex) {

            assertThat(ex.getCause(), instanceOf(NoSuchElementException.class));
        }
    }
}