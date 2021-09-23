package com.example.usergen.view.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.usergen.R;
import com.example.usergen.UsergenApplication;
import com.example.usergen.view.activity.main.MainActivity;
import com.example.usergen.view.activity.register.RegisterActivity;
import com.example.usergen.view.custom.CustomButton;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.disposables.Disposable;

import static com.example.usergen.view.activity.login.AuthViewModel.API_UNAVAILABLE;
import static com.example.usergen.view.activity.login.AuthViewModel.DISPLAY_CONTENT_NO_COMEBACK;

public class LoginActivity extends AppCompatActivity {

    private AuthViewModel viewModel;

    private EditText usernameEditText;
    private EditText passwordEditText;

    private Disposable subscription;

    private CustomButton loginButton;

    private TextView unregisteredTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.loginActivity_usernameEditText);
        passwordEditText = findViewById(R.id.loginActivity_passwordEditText);
        loginButton = findViewById(R.id.loginActivity_loginButton);
        unregisteredTextView = findViewById(R.id.loginActivity_notRegisteredView);

        viewModel = new ViewModelProvider(this,
                AuthViewModel.create(
                        UsergenApplication.from(this) .getAuthRepository()
                )
        ).get(AuthViewModel.class);

        viewModel.checkAlreadyLogged();
    }

    @Override
    protected void onStart() {
        super.onStart();

        subscription = viewModel.getEvents().subscribe(event -> {
            switch (event) {
                case AuthViewModel.MISSING_USERNAME:
                    onMissingUsername();
                    break;

                case AuthViewModel.MISSING_PASSWORD:
                    onMissingPassword();
                    break;

                case AuthViewModel.DISPLAY_CONTENT:
                    startContentActivity();
                    break;

                case AuthViewModel.LOGIN_FAILED:
                    onLoginFailed();
                    break;

                case AuthViewModel.DISPLAY_REGISTER_SCREEN:
                    startRegisterActivity();
                    break;

                case DISPLAY_CONTENT_NO_COMEBACK:
                    startContentActivityClearingStack();
                    break;
                    
                case API_UNAVAILABLE:
                    reportUnavailableApi();
            }
        });
    }

    private void reportUnavailableApi() {
        usernameEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        loginButton.getButton().setEnabled(false);
        unregisteredTextView.setEnabled(false);

        Snackbar.make(
                findViewById(android.R.id.content),
                "The api is unavailable please try again later",
                Snackbar.LENGTH_LONG
        ).show();
    }

    private void onMissingPassword() {
        passwordEditText.setError(getString(R.string.error_missingPassword));
    }

    private void onMissingUsername() {
        usernameEditText.setError(getString(R.string.error_missingUsername));
    }

    private void onLoginFailed() {

        Snackbar.make(
                findViewById(android.R.id.content),
                "Invalid credentials",
                Snackbar.LENGTH_LONG
        ).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscription.dispose();
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);

        startActivity(intent);
    }

    private void startContentActivity() {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    public void startContentActivityClearingStack() {

        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }


    public void onRegisterClick(@Nullable View view) {
        viewModel.requestRegisterScreen();
    }

    public void onLoginClick(@Nullable View view) {

        viewModel.login(
                usernameEditText.getText().toString(),
                passwordEditText.getText().toString()
        );

    }
}