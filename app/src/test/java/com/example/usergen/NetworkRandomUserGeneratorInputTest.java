package com.example.usergen;

import com.example.usergen.model.user.NetworkRandomUserGeneratorInput;

import org.junit.Test;

import java.util.InputMismatchException;

public class NetworkRandomUserGeneratorInputTest {

    // Constructor Tests

    @Test(expected = InputMismatchException.class)
    public void NetworkRandomUserGeneratorInput_constructorRejectsInvalidNationality() {
        new NetworkRandomUserGeneratorInput("ZZ", "male");
    }


    @Test(expected = InputMismatchException.class)
    public void NetworkRandomUserGeneratorInput_constructorRejectsInvalidGender() {
        new NetworkRandomUserGeneratorInput("US", "helicopter");
    }

    @Test(expected = InputMismatchException.class)
    public void NetworkRandomUserGeneratorInput_constructorRejectsInvalidInput() {
        new NetworkRandomUserGeneratorInput("ZZ", "helicopter");
    }

    @Test
    public void NetworkRandomUserGeneratorInput_constructorAcceptsValid() {
        new NetworkRandomUserGeneratorInput("US", "male");
    }

    // Setter tests

    @Test(expected =  InputMismatchException.class)
    public void NetworkRandomUser_setterRejectsInvalidNationality() {
        new NetworkRandomUserGeneratorInput().setNationality("ZZ");
    }

    @Test(expected = InputMismatchException.class)
    public void NetworkRandomUser_setterRejectsInvalidGender() {
        new NetworkRandomUserGeneratorInput().setGender("helicopter");
    }

    @Test
    public void NetworkRandomUserGeneratorInput_setterAcceptsValid() {

        NetworkRandomUserGeneratorInput input = new NetworkRandomUserGeneratorInput();

        input.setGender("male");
        input.setNationality("US");
    }

}
