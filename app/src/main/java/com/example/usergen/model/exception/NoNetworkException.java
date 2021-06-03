package com.example.usergen.model.exception;

import androidx.annotation.NonNull;

public class NoNetworkException extends Exception {

    public NoNetworkException(@NonNull String error) {
        super(error);
    }

}
