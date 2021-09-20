package com.example.usergen.model.user;

import android.os.Bundle;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.usergen.service.generator.RandomUserGeneratorInput;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.InputMismatchException;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RandomUserGeneratorInputTest {

    // Constructor Tests

    @Test(expected = InputMismatchException.class)
    public void constructorRejectsInvalidNationality() {
        new RandomUserGeneratorInput("ZZ", "male");
    }


    @Test(expected = InputMismatchException.class)
    public void constructorRejectsInvalidGender() {
        new RandomUserGeneratorInput("US", "helicopter");
    }

    @Test(expected = InputMismatchException.class)
    public void constructorRejectsInvalidInput() {
        new RandomUserGeneratorInput("ZZ", "helicopter");
    }

    @Test
    public void constructorAcceptsValid() {
        new RandomUserGeneratorInput("US", "male");
    }

    // Setter tests

    @Test(expected = InputMismatchException.class)
    public void NetworkRandomUser_setterRejectsInvalidNationality() {
        new RandomUserGeneratorInput("BR", "male").setNationality("ZZ");
    }

    @Test(expected = InputMismatchException.class)
    public void NetworkRandomUser_setterRejectsInvalidGender() {
        new RandomUserGeneratorInput("BR", "male").setGender("helicopter");
    }

    @Test
    public void NetworkRandomUserGeneratorInput_setterAcceptsValid() {

        RandomUserGeneratorInput input = new RandomUserGeneratorInput("BR", "male");

        input.setGender("male");
        input.setNationality("US");
    }

    @Test
    public void bundleConversionTest() {


        RandomUserGeneratorInput input = new RandomUserGeneratorInput("US", "male");

        Bundle bundle = input.asBundle();

        RandomUserGeneratorInput result = RandomUserGeneratorInput.fromBundle(bundle);

        assertEquals(input.getGender(), result.getGender());
        assertEquals(input.getNationality(), result.getNationality());
    }


}
