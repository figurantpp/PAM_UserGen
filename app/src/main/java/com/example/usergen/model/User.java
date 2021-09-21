package com.example.usergen.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.usergen.service.http.OnlineImageResource;

import java.util.Date;

public class User {

    @Nullable
    private final String sourceId;

    @Nullable
    private final String apiId;

    @NonNull
    private final String title;

    @NonNull
    private final String name;

    @NonNull
    private final String email;

    @NonNull
    private final String gender;

    @NonNull
    private final Date birthDate;

    private final short age;

    @NonNull
    private final String nationality;

    @NonNull
    private final OnlineImageResource profileImage;

    public User(
            @Nullable String sourceId,
            @NonNull String title,
            @NonNull String name,
            @NonNull String email,
            @NonNull String gender,
            @NonNull Date birthDate,
            short age,
            @NonNull String nationality,
            @NonNull OnlineImageResource profileImage
    ) {
        this(
                sourceId,
                null,
                title,
                name,
                email,
                gender,
                birthDate,
                age,
                nationality,
                profileImage
        );
    }

    public User(
            @Nullable String sourceId,
            @Nullable String apiId,
            @NonNull String title,
            @NonNull String name,
            @NonNull String email,
            @NonNull String gender,
            @NonNull Date birthDate,
            short age,
            @NonNull String nationality,
            @NonNull OnlineImageResource profileImage
    ) {
        this.sourceId = sourceId;
        this.apiId = apiId;
        this.title = title;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.age = age;
        this.nationality = nationality;
        this.profileImage = profileImage;
    }

    @Nullable
    public String getSourceId() {
        return sourceId;
    }

    @Nullable
    public String getApiId() {
        return apiId;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getGender() {
        return gender;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public Date getBirthDate() {
        return birthDate;
    }

    public short getAge() {
        return age;
    }

    @NonNull
    public String getNationality() {
        return nationality;
    }

    @NonNull
    public OnlineImageResource getProfileImage() {
        return profileImage;
    }
}
