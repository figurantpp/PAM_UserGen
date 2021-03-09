package com.example.usergen.model.interfaces;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public interface ModelJsonManager<Model> {

    @NonNull
    Model getModelFromJSONObject(@NonNull JSONObject source) throws JSONException;
}
