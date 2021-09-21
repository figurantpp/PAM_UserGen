package com.example.usergen.util;

import androidx.annotation.NonNull;

public final class Util {
    private Util() {

    }

    @NonNull
    public static String toPascal(@NonNull String text) {
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
