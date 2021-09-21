package com.example.usergen.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Settings {

    @NonNull
    private final String sexQuery;

    @Nullable
    private final String nationalityQuery;

    public Settings(@NonNull String sexQuery, @Nullable String nationalityQuery) {
        this.sexQuery = sexQuery;
        this.nationalityQuery = nationalityQuery;
    }

    @NonNull
    public String getSexQuery() {
        return sexQuery;
    }

    @Nullable
    public String getNationalityQuery() {
        return nationalityQuery;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return sexQuery.equals(settings.sexQuery) &&
                Objects.equals(nationalityQuery, settings.nationalityQuery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sexQuery, nationalityQuery);
    }
}
