package com.example.usergen.model.user;

import androidx.annotation.Nullable;

public class RandomUserGeneratorInput {

    private String nationality;
    private String gender;

    public RandomUserGeneratorInput() {
    }

    public RandomUserGeneratorInput(@Nullable String nationality, @Nullable String gender) {
        this.nationality = nationality;
        this.gender = gender;
    }

    @Nullable
    public String getNationality() {
        return nationality;
    }

    public void setNationality(@Nullable String nationality) {
        this.nationality = nationality;
    }

    @Nullable
    public String getGender() {
        return gender;
    }

    public void setGender(@Nullable String gender) {
        this.gender = gender;
    }
}
