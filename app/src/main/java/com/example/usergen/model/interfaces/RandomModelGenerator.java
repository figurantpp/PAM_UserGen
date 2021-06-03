package com.example.usergen.model.interfaces;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

public interface RandomModelGenerator<Model> {

    @NonNull
    Future<Model> nextRandomModel();

    @NonNull
    Future<List<Model>> nextModels(int limit);
}
