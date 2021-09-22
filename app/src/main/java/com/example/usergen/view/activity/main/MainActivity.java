package com.example.usergen.view.activity.main;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.usergen.R;
import com.example.usergen.UsergenApplication;
import com.example.usergen.model.Settings;
import com.example.usergen.view.activity.favorite_list.FavoriteListActivity;
import com.example.usergen.view.activity.login.LoginActivity;
import com.example.usergen.view.activity.single_user.ShowUserActivity;
import com.example.usergen.view.activity.user_list.UserListActivity;
import com.example.usergen.view.custom.CustomButton;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity implements MainActivityViewModel.Event.Visitor {

    Spinner spinner;

    View searchSingleButton;

    TextView titleTextView;

    TypedArray acronymArray;

    RadioButton maleRadioButton, femaleRadioButton;

    CustomButton multipleUsersButton;

    View logOutView;

    CustomButton favoritesButton;

    MainActivityViewModel viewModel;

    CompositeDisposable subscriptions = new CompositeDisposable();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setupUi();
        setupViewModel();

        viewModel.fetchSettings();
    }

    private void setupViewModel() {

        viewModel = new ViewModelProvider(
                this,
                MainActivityViewModel.create(
                        UsergenApplication.from(this).getSettingsRepository(),
                        UsergenApplication.from(this).getAuthRepository()
                )
        ).get(MainActivityViewModel.class);

        viewModel.getFetchedSettings().observe(this, this::displaySettings);
    }


    @Override
    protected void onStart() {
        super.onStart();

        subscriptions.add(
                viewModel.getEvents().subscribe(
                        event -> event.accept(this)
                )
        );
    }

    @Override
    protected void onStop() {
        super.onStop();

        subscriptions.clear();
    }


    private void setupUi() {

        setupViews();

        setupAnimations();

        acronymArray = getResources().obtainTypedArray(R.array.acronyms);

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

        favoritesButton.setOnClickListener(v ->
                viewModel.startFavorites()
        );

        logOutView.setOnClickListener(v ->
                viewModel.requestLogOut());

    }

    private void setupViews() {
        setupSpinner();

        searchSingleButton = findViewById(R.id.mainActivity_searchButton);

        titleTextView = findViewById(R.id.textView0);

        multipleUsersButton = findViewById(R.id.mainActivity_multipleUsersButton);

        femaleRadioButton = findViewById(R.id.radioFemale);

        maleRadioButton = findViewById(R.id.radioMale);

        favoritesButton = findViewById(R.id.mainActivity_favoritesButton);

        logOutView = findViewById(R.id.mainActivity_logOutButton);
    }

    private void setupSpinner() {
        spinner = findViewById(R.id.mainActivity_nationalitySpinner);

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

    private void displaySettings(Settings settings) {
        if (settings.getSexQuery().equals("female")) {
            femaleRadioButton.setChecked(true);
        } else {
            maleRadioButton.setChecked(true);
        }

        for (int i = 0; i < acronymArray.length(); ++i) {
            if (acronymArray.getString(i).equals(settings.getNationalityQuery())) {
                spinner.setSelection(i);
            }
        }
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
    public void visit(@NonNull MainActivityViewModel.StartSingleUserEvent event) {

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
    public void visit(@NonNull MainActivityViewModel.StartMultipleUsersEvent event) {

        Intent intent = new Intent(
                this,
                UserListActivity.class
        );

        intent.putExtra(
                UserListActivity.INPUT_EXTRA_KEY,
                event.input.asBundle()
        );

        startActivity(intent);
    }

    @Override
    public void visit(@NonNull MainActivityViewModel.StartLoginEvent event) {

        Intent intent = new Intent(
                this,
                LoginActivity.class
        );

        startActivity(intent);
    }

    @Override
    public void visit(@NonNull MainActivityViewModel.StartFavoritesEvent event) {

        Intent intent = new Intent(
                this,
                FavoriteListActivity.class
        );

        startActivity(intent);
    }

}