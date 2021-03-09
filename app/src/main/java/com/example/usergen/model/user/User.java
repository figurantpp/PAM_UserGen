package com.example.usergen.model.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.usergen.model.OnlineImageResource;

import java.time.LocalDate;
import java.util.Date;

public class User {

    private String id;

    private String title;
    private String name;

    private String email;

    private String gender;

    private Date birthDate;
    private short age;

    private OnlineImageResource profileImage;

    private String nationality;

    @Nullable
    public String getId() {
        return id;
    }


    public void setId(@Nullable String id) {
        this.id = id;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Nullable
    public String getGender() {
        return gender;
    }

    public void setGender(@Nullable String gender) {
        this.gender = gender;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @NonNull
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(@NonNull Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public short getAge() {
        return age;
    }

    public void setAge(short age) {
        this.age = age;
    }

    @Nullable
    public OnlineImageResource getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(@Nullable OnlineImageResource profileImage) {
        this.profileImage = profileImage;
    }

    @Nullable
    public String getNationality() {
        return nationality;
    }

    public void setNationality(@Nullable String nationality) {
        this.nationality = nationality;
    }
}
