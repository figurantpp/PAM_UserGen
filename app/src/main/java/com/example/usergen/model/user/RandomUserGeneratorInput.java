package com.example.usergen.model.user;

import androidx.annotation.NonNull;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.stream.Stream;

public class RandomUserGeneratorInput {

    private String nationality;
    private String gender;

    public RandomUserGeneratorInput() {
    }

    public RandomUserGeneratorInput(@NonNull String nationality, @NonNull String gender) {

        assertValidNationality(nationality);
        assertValidGender(gender);

        this.nationality = nationality;
        this.gender = gender;
    }

    private void assertValidNationality(@NonNull String nationality) {

        Objects.requireNonNull(nationality);

        String[] validNationalities = new String[]{
                "AU", "BR", "CA", "CH", "DE", "DK", "ES", "FI",
                "FR", "GB", "IE", "IR", "NO", "NL", "NZ", "TR", "US"
        };

        if (Stream.of(validNationalities).noneMatch(x -> x.equals(nationality)))
        {
            throw new InputMismatchException("Nationality " + nationality + " is not valid");
        }
    }

    private void assertValidGender(@NonNull String gender) {

        Objects.requireNonNull(gender);

        if (!gender.equals("male") && !gender.equals("female"))
        {
            throw new InputMismatchException("Invalid API gender " + gender);
        }
    }

    @NonNull
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
}
