package com.example.usergen.activity.main;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.usergen.R;
import com.example.usergen.activity.ShowVariousUsersActivity;
import com.example.usergen.activity.single_user.ShowUserActivity;
import com.example.usergen.custom.CustomButton;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity implements MainActivityViewModel.Event.Visitor {

    Spinner spinner;

    Button searchSingleButton;

    TextView titleTextView;

    TypedArray acronymArray;

    RadioButton maleRadioButton, femaleRadioButton;

    CustomButton multipleUsersButton;

    MainActivityViewModel viewModel;

    CompositeDisposable subscriptions;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subscriptions = new CompositeDisposable();

        setupUi();
        setupViewModel();
    }

    private void setupViewModel() {

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        setupViewModelEvents();
    }

    private void setupViewModelEvents() {

        subscriptions.add(
                viewModel.getEvents().subscribe(
                        event -> event.accept(this)
                )
        );
    }


    private void setupUi() {

        setupViews();

        setupAnimations();

        acronymArray = getResources().obtainTypedArray(R.array.acronym);

        setupUiEvents();
    }

    private void setupUiEvents() {
        searchSingleButton.setOnClickListener(v ->
                viewModel.startSingleUserQuery(new MainActivityViewModel.Input(
                femaleRadioButton.isChecked(),
                maleRadioButton.isChecked(),
                getSpinnerOption()
        )));

        multipleUsersButton.setOnClickListener(v ->
                viewModel.startMultipleUserQuery(new MainActivityViewModel.Input(
                femaleRadioButton.isChecked(),
                maleRadioButton.isChecked(),
                getSpinnerOption()
        )));
    }

    private void setupViews() {
        setupSpinner();

        searchSingleButton = findViewById(R.id.search_on_api_button);

        titleTextView = findViewById(R.id.random_text);

        multipleUsersButton = findViewById(R.id.multiple_button);

        femaleRadioButton = findViewById(R.id.radioFemale);

        maleRadioButton = findViewById(R.id.radioMale);
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

        searchSingleButton.setAnimation(upDown);

        titleTextView.setAnimation(fade);
    }


    private String getSpinnerOption() {
        return acronymArray.getString(spinner.getSelectedItemPosition());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        subscriptions.dispose();
    }

    @Override
    public void visit(@NonNull MainActivityViewModel.UncheckedSexError error) {

        Snackbar.make(
                findViewById(android.R.id.content),
                R.string.error_empty,
                Snackbar.LENGTH_SHORT
        ).show();
    }

    @Override
    public void visit(@NonNull MainActivityViewModel.GenerateSingleUserEvent event) {

        Intent intent = new Intent(
                this,
                ShowUserActivity.class
        );

        intent.putExtra(
                ShowUserActivity.INPUT_EXTRA_KEY,
                event.input.asBundle()
        );

        startActivity(intent);
    }

    @Override
    public void visit(@NonNull MainActivityViewModel.GenerateManyUsersEvent event) {

        Intent intent = new Intent(
                this,
                ShowVariousUsersActivity.class
        );

        intent.putExtra(
                ShowVariousUsersActivity.INPUT_EXTRA_KEY,
                event.input.asBundle()
        );

        startActivity(intent);
    }

}