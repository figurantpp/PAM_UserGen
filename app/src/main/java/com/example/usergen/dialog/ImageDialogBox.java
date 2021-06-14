package com.example.usergen.dialog;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.usergen.R;

import java.util.Objects;

public class ImageDialogBox extends DialogBox{

    public ImageDialogBox(@NonNull Context context) {
        super(context, R.layout.dialog_layout);
    }

    @NonNull
    public ImageView getImageView()
    {
        return Objects.requireNonNull(getView().findViewById(R.id.dialogImage));
    }
}
