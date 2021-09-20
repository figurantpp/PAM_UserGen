package com.example.usergen.service.generator;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface RandomModelGenerator<Model> {

    @NonNull
    Single<Model> nextRandomModel();

    @NonNull
    Single<List<Model>> nextModels(int limit);
}
