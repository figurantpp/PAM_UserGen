package com.example.usergen.model.user;

import androidx.annotation.NonNull;

import com.example.usergen.model.OnlineImageResource;

import java.util.Date;

public class User {

    @NonNull
    private final String id;

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
            @NonNull String id,
            @NonNull String title,
            @NonNull String name,
            @NonNull String email,
            @NonNull String gender,
            @NonNull Date birthDate,
            short age,
            @NonNull String nationality,
            @NonNull OnlineImageResource profileImage
    ) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.age = age;
        this.profileImage = profileImage;
        this.nationality = nationality;
    }

    @NonNull
    public String getId() {
        return id;
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
