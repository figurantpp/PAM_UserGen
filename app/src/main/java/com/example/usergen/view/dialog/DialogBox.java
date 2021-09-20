package com.example.usergen.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public abstract class DialogBox {

    private final View view;

    private final Context context;

    private final AlertDialog dialog;

    protected DialogBox(@NonNull Context context, @LayoutRes int layout) {

        this.context = context;

        this.view = LayoutInflater.from(context).inflate(layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        this.dialog = builder.create();
    }

    public void show() {

        dialog.show();

    }

    public void cancel() {

        dialog.cancel();

    }

    protected @NonNull
    Context getContext() {
        return context;
    }

    @NonNull
    protected View getView() {
        return view;
    }
}