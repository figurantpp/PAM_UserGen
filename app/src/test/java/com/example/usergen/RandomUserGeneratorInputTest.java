package com.example.usergen;

import com.example.usergen.model.user.RandomUserGeneratorInput;

import org.junit.Test;

import java.util.InputMismatchException;

public class RandomUserGeneratorInputTest {

    // Constructor Tests

    @Test(expected = InputMismatchException.class)
    public void NetworkRandomUserGeneratorInput_constructorRejectsInvalidNationality() {
        new RandomUserGeneratorInput("ZZ", "male");
    }


    @Test(expected = InputMismatchException.class)
    public void NetworkRandomUserGeneratorInput_constructorRejectsInvalidGender() {
        new RandomUserGeneratorInput("US", "helicopter");
    }

    @Test(expected = InputMismatchException.class)
    public void NetworkRandomUserGeneratorInput_constructorRejectsInvalidInput() {
        new RandomUserGeneratorInput("ZZ", "helicopter");
    }

    @Test
    public void NetworkRandomUserGeneratorInput_constructorAcceptsValid() {
        new RandomUserGeneratorInput("US", "male");
    }

    // Setter tests

    @Test(expected =  InputMismatchException.class)
    public void NetworkRandomUser_setterRejectsInvalidNationality() {
        new RandomUserGeneratorInput().setNationality("ZZ");
    }

    @Test(expected = InputMismatchException.class)
    public void NetworkRandomUser_setterRejectsInvalidGender() {
        new RandomUserGeneratorInput().setGender("helicopter");
    }

    @Test
    public void NetworkRandomUserGeneratorInput_setterAcceptsValid() {

        RandomUserGeneratorInput input = new RandomUserGeneratorInput();

        input.setGender("male");
        input.setNationality("US");
    }

}
