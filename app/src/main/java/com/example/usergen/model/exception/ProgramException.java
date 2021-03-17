package com.example.usergen.model.exception;

import androidx.annotation.Nullable;

public class ProgramException extends RuntimeException {

    public ProgramException() {
        super();
    }

    public ProgramException(@Nullable String error) {
        super(error);
    }

    public ProgramException(@Nullable Throwable source) {
        super(source);
    }
}
