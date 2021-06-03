package com.example.usergen.model.user;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.usergen.model.exception.NoNetworkException;
import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.util.Tags;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assume.assumeNoException;


@RunWith(AndroidJUnit4.class)
public class NetworkRandomUserGeneratorTest {

    RandomUserGeneratorTest test;

    Context context;

    @Before
    public void setup() {
        test = new RandomUserGeneratorTest();
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void nextRandomModel() {

        RandomUserGeneratorInput input
                = new RandomUserGeneratorInput("BR", "male");

        try {
            RandomModelGenerator<User> subject
                    = new NetworkRandomUserGenerator(context, input);

            test.withInput(input).nextRandomModelOn(subject);
        }
        catch (NoNetworkException ex) {
            Log.e(Tags.ERROR, "nextRandomModel: ", ex);

            assumeNoException(ex);
        }


    }

    @Test
    public void nextModels() {

        RandomUserGeneratorInput input
               = new RandomUserGeneratorInput(null, "male");

        try {
            RandomModelGenerator<User> subject = new NetworkRandomUserGenerator(context, input);

            test.withInput(input).nextModelsOn(subject);
        }
        catch (NoNetworkException ex) {
            assumeNoException(ex);
        }



    }
}
