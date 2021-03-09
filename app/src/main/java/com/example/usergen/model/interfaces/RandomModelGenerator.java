package com.example.usergen.model.interfaces;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.Future;

public interface RandomModelGenerator<Model> {

    @NonNull
    Future<Model> nextRandomModel();
}
