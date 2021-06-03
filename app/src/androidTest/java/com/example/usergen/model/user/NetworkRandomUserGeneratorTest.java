package com.example.usergen.model.user;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.usergen.model.exception.NoNetworkException;
import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.util.Tags;

import org.junit.Assume;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeTrue;


@RunWith(AndroidJUnit4.class)
public class NetworkRandomUserGeneratorTest {

    RandomUserGeneratorTest test;

    @Before
    public void setup() {
        test = new RandomUserGeneratorTest();
    }

    @Test
    public void nextRandomModel() {

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        RandomUserGeneratorInput input;

        input = new RandomUserGeneratorInput("BR", "male");

        try {
            RandomModelGenerator<User> subject = new NetworkRandomUserGenerator(context, input);
            test.nextRandomModel(context, subject);
        }
        catch (NoNetworkException ex) {
            Log.e(Tags.ERROR, "nextRandomModel: ", ex);

            assumeNoException(ex);
        }


    }
}
