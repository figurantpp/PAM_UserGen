package com.example.usergen.view.activity.register;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.usergen.R;
import com.example.usergen.UsergenApplication;
import com.example.usergen.view.activity.login.AuthViewModel;

import io.reactivex.rxjava3.disposables.Disposable;

public class RegisterActivity extends AppCompatActivity {

    Disposable subscription;

    private AuthViewModel viewModel;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmationEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupViews();

        setupViewModel();
    }

    private void setupViews() {
        usernameEditText = findViewById(R.id.registerActivity_usernameEditText);
        passwordEditText = findViewById(R.id.registerActivity_passwordEditText);
        passwordConfirmationEditText = findViewById(R.id.registerActivity_confirmPasswordEditText);

        findViewById(R.id.registerActivity_backButton)
            .setOnClickListener(v -> finish());

        findViewById(R.id.registerActivity_registerButton)
                .setOnClickListener(this::onButtonClick);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(
                this,
                AuthViewModel.create(UsergenApplication.from(this).getAuthRepository())
        ).get(AuthViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // todo: add message mapping please

        subscription = viewModel.getEvents().subscribe(event -> {

            switch (event) {

                case AuthViewModel.MISSING_USERNAME:
                    usernameEditText.setError(getString(R.string.error_missingUsername));
                    break;

                case AuthViewModel.MISSING_PASSWORD:
                    passwordEditText.setError(getString(R.string.error_missingPassword));
                    break;

                case AuthViewModel.MISSING_PASSWORD_CONFIRMATION_ERROR:
                    passwordConfirmationEditText.setError(getString(R.string.error_missingPasswordConfirmation));
                    break;

                case AuthViewModel.PASSWORD_NOT_EQUALS_CONFIRMATION:
                    passwordConfirmationEditText.setError(getString(R.string.error_unmatchedPasswordConfirmation));
                    break;

                case AuthViewModel.FINISH_REGISTER:
                    finish();
                    break;

                case AuthViewModel.USERNAME_TAKEN:
                    usernameEditText.setError(getString(R.string.error_takenUsername));
                    break;

            }

        });
    }

    @Override
    protected void onStop() {
        subscription.dispose();
        super.onStop();
    }

    private void onButtonClick(View v) {
        viewModel.register(
                usernameEditText.getText().toString(),
                passwordEditText.getText().toString(),
                passwordConfirmationEditText.getText().toString()
        );
    }
}