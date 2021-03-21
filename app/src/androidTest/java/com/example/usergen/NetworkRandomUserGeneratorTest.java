package com.example.usergen;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.model.user.NetworkRandomUserGenerator;
import com.example.usergen.model.user.RandomUserGeneratorInput;
import com.example.usergen.model.user.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


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

        RandomModelGenerator<User> subject = new NetworkRandomUserGenerator(context, input);


        test.nextRandomModel(context, subject);
    }
}
