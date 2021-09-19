package com.example.usergen.activity.main;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.usergen.R;
import com.example.usergen.activity.ShowUserActivity;
import com.example.usergen.activity.ShowVariousUsersActivity;
import com.example.usergen.custom.CustomButton;
import com.example.usergen.model.user.generator.RandomUserGeneratorInput;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;

    Button search;

    TextView title;

    TypedArray acronyms;

    RadioButton male, female;

    String gender;

    CustomButton button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();

        setupAnimations();

        acronyms = getResources().obtainTypedArray(R.array.acronym);

        search.setOnClickListener(this::show);
        button.setOnClickListener(view -> this.goToMultiple());
    }

    private void setupViews() {
        setupSpinner();

        search = findViewById(R.id.search_on_api_button);

        title = findViewById(R.id.random_text);

        button = findViewById(R.id.multiple_button);

        female = findViewById(R.id.radioFemale);

        male = findViewById(R.id.radioMale);
    }

    private void setupSpinner() {
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.countries,
                android.R.layout.simple_spinner_dropdown_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setupAnimations() {
        Animation upDown = AnimationUtils.loadAnimation(this, R.anim.up_down);
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade);

        search.setAnimation(upDown);

        title.setAnimation(fade);
    }


    public void show(@NonNull View view) {

        if (!isValid()) return;

        RandomUserGeneratorInput input = new RandomUserGeneratorInput(
                getSpinnerOption(),
                gender
        );

        Intent intent = new Intent(this, ShowUserActivity.class);
        intent.putExtra(ShowUserActivity.INPUT_EXTRA_KEY, input.asBundle());

        startActivity(intent);
    }

    private boolean isValid() {
        if (!female.isChecked() && !male.isChecked()) {
            Snackbar.make(findViewById(android.R.id.content), R.string.error_empty, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (female.isChecked()) {
            gender = "female";
        } else {

            gender = "male";
        }
        return true;
    }

    private String getSpinnerOption() {
        String optionSpinner = acronyms.getString(spinner.getSelectedItemPosition());

        if (optionSpinner.trim().isEmpty()) {
            return null;
        } else {
            return optionSpinner;
        }
    }

    public void goToMultiple() {
        if (!isValid()) {
            return;
        }
        RandomUserGeneratorInput input = new RandomUserGeneratorInput(
                getSpinnerOption(),
                gender
        );

        Intent intent = new Intent(this, ShowVariousUsersActivity.class);

        intent.putExtra(ShowVariousUsersActivity.INPUT_EXTRA_KEY, input.asBundle());

        startActivity(intent);
    }
}