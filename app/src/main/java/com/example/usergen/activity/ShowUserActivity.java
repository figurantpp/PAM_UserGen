package com.example.usergen.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.usergen.R;
import com.example.usergen.model.exception.NoNetworkException;
import com.example.usergen.model.exception.SensorNotFoundException;
import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.model.sensor.ProximitySensor;
import com.example.usergen.model.user.NetworkRandomUserGenerator;
import com.example.usergen.model.user.RandomUserGeneratorInput;
import com.example.usergen.model.user.StorageRandomUserGenerator;
import com.example.usergen.model.user.User;
import com.example.usergen.model.user.UserStorage;
import com.example.usergen.model.user.UserViewModel;
import com.example.usergen.util.Tags;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ShowUserActivity extends AppCompatActivity {

    public final static String INPUT_BUNDLE_KEY = "nationality";

    ProximitySensor proximitySensor;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);

        setupSensor();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getDisplayEvent().observe(this, v -> notifyDisplay());

        new Thread(this::loadUser).start();
    }

    private void setupSensor() {
        try {
            proximitySensor = new ProximitySensor(this, this::onProximityUpdate);
        } catch (SensorNotFoundException ex) {
            Log.e(Tags.ERROR, "onCreate: ", ex);
            finish();
        }
    }

    private void onProximityUpdate(boolean isClose) {
        if (isClose) {
            finish();
        }
    }

    private void loadUser() {

        RandomUserGeneratorInput input = getIntentGeneratorInput();

        RandomModelGenerator<User> generator = findUserGenerator(input);

        Future<User> result = generator.nextRandomModel();

        try {
            User user = result.get();

            runOnUiThread(() -> userViewModel.selectUser(user));

            if (generator instanceof NetworkRandomUserGenerator) {
                new UserStorage(this).storeModel(user);
            }

        } catch (ExecutionException | InterruptedException | IOException ex) {
            Log.e(Tags.ERROR, "Fail to get User", ex);
        }
    }

    private RandomModelGenerator<User> findUserGenerator(RandomUserGeneratorInput input) {

        RandomModelGenerator<User> generator;

        try {
            generator = new NetworkRandomUserGenerator(this, input);

        } catch (NoNetworkException ex) {
            Log.e(Tags.ERROR, "Failed to get NetworkAccess", ex);
            generator = new StorageRandomUserGenerator(new UserStorage(this), input);
        }
        return generator;
    }

    private RandomUserGeneratorInput getIntentGeneratorInput() {
        Bundle bundle = getIntent().getBundleExtra(INPUT_BUNDLE_KEY);
        Objects.requireNonNull(bundle);

        return RandomUserGeneratorInput.fromBundle(bundle);
    }

    // testing members

    public void notifyDisplay() {

        if (displayListener != null) {
            displayListener.onDisplay();
        }
    }

    @VisibleForTesting
    @Nullable
    public ProximitySensor getProximitySensor() {
        return proximitySensor;
    }

    @VisibleForTesting()
    @Nullable
    public DisplayListener displayListener;

    @VisibleForTesting()
    public interface DisplayListener {
        void onDisplay();
    }
}