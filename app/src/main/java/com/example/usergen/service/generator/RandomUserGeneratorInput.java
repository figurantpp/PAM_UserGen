package com.example.usergen.service.generator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.usergen.util.RandomApiInfo;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Objects;

public class RandomUserGeneratorInput {

    @Nullable
    private String nationality;

    @NonNull
    private String gender;

    public RandomUserGeneratorInput(@Nullable String nationality, @NonNull String gender) {

        assertValidNationality(nationality);
        assertValidGender(gender);

        this.nationality = nationality;
        this.gender = gender;
    }

    private void assertValidNationality(@Nullable String nationality) {

        if (nationality != null) {

            if (Arrays.stream(RandomApiInfo.NATIONALITY_ACRONYMS)
                    .noneMatch(x -> x.equals(nationality))) {
                throw new InputMismatchException("Nationality " + nationality + " is not valid");
            }
        }
    }

    private void assertValidGender(@NonNull String gender) {

        Objects.requireNonNull(gender);

        if (!gender.equals("male") && !gender.equals("female"))
        {
            throw new InputMismatchException("Invalid API gender " + gender);
        }
    }

    @Nullable
    public String getNationality() {
        return nationality;
    }

    public void setNationality(@NonNull String nationality) {
        assertValidNationality(nationality);
        this.nationality = nationality;
    }

    @NonNull
    public String getGender() {
        return gender;
    }

    public void setGender(@NonNull String gender) {
        assertValidGender(gender);
        this.gender = gender;
    }

    @NonNull
    public Bundle asBundle()
    {
        Bundle result = new Bundle();
        result.putString("gender", gender);
        result.putString("nat", nationality);

        return result;

    }

    @NonNull
    public static RandomUserGeneratorInput fromBundle(@NonNull Bundle bundle)
    {
        return new RandomUserGeneratorInput(bundle.getString("nat"), bundle.getString("gender"));
    }
}
