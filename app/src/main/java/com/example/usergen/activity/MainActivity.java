package com.example.usergen.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.usergen.R;
import com.example.usergen.model.user.RandomUserGeneratorInput;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;

    Button search;

    TextView title;

    TypedArray acronymes;

    RadioButton male, female;

    String gender;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        search = findViewById(R.id.search_on_api_button);

        title = findViewById(R.id.random_text);


        Animation updown = AnimationUtils.loadAnimation(this, R.anim.up_down);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.fade);

        search.setAnimation(updown);

        title.setAnimation(animation1);

        acronymes = getResources().obtainTypedArray(R.array.acronym);


        search.setOnClickListener(this::show);

        female = findViewById(R.id.radioFemale);
        male = findViewById(R.id.radioMale);

    }

    public void show(@NonNull View view) {


        if (!female.isChecked() && !male.isChecked()) {
            Snackbar.make(view, R.string.error_empty, Snackbar.LENGTH_SHORT).show();
            return;
        }

            if (female.isChecked()) {
                gender = "female";
            } else {

                gender = "male";
            }

        RandomUserGeneratorInput input = new RandomUserGeneratorInput(optionSpinner, gender);

        Intent intent = new Intent(this, ShowResultsActivity.class);
        intent.putExtra(ShowResultsActivity.INPUT_BUNDLE_KEY, input.asBundle());

        startActivity(intent);
    }

    String optionSpinner;


    @Override
    public void onItemSelected(@Nullable AdapterView<?> adapterView, @Nullable View view, int i, long l) {
        String acronym = acronymes.getString(i).trim();

        if( acronym.isEmpty())
        {
            acronym = null;
        }

        optionSpinner = acronym;



    }

    @Override
    public void onNothingSelected(@Nullable AdapterView<?> adapterView) {

    }
}