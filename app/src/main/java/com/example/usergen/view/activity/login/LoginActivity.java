package com.example.usergen.view.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.usergen.R;
import com.example.usergen.UsergenApplication;
import com.example.usergen.view.activity.main.MainActivity;
import com.example.usergen.view.activity.register.RegisterActivity;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.rxjava3.disposables.Disposable;

public class LoginActivity extends AppCompatActivity {

    private AuthViewModel viewModel;

    private EditText usernameEditText;
    private EditText passwordEditText;

    private Disposable subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.loginActivity_username);
        passwordEditText = findViewById(R.id.loginActivity_password);

        viewModel = new ViewModelProvider(this,
                AuthViewModel.create(
                        UsergenApplication.from(this) .getAuthRepository()
                )
        ).get(AuthViewModel.class);

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
            }
        });
    }

    private void onMissingPassword() {
        passwordEditText.setError("The password is mandatory");
    }

    private void onMissingUsername() {
        usernameEditText.setError("The username is mandatory");
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