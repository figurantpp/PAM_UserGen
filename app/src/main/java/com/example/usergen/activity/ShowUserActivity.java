package com.example.usergen.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.usergen.util.ApiInfo;
import com.example.usergen.util.Tags;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowUserActivity extends AppCompatActivity {

    public final static String INPUT_BUNDLE_KEY = "nationality";


    CircleImageView pictureImageView;

    TextView firstTitleTextView,
            genderTextView,
            emailTextView,
            birthTextView,
            nationalityTextView,
            titleTextView,
            ageTextView,
            idTextView;


    UserStorage userStorage;

    ProximitySensor proximitySensor;

    private User displayedUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);

        setupViews();

        setupSensor();

        new Thread(this::loadUser).start();
    }

    private void setupViews() {
        firstTitleTextView = findViewById(R.id.personFirstandSecondName);
        genderTextView = findViewById(R.id.personGender);
        emailTextView = findViewById(R.id.personEmail);
        birthTextView = findViewById(R.id.personDayOfBirth);
        nationalityTextView = findViewById(R.id.personNationality);
        titleTextView = findViewById(R.id.personTitle);
        ageTextView = findViewById(R.id.personAge);
        idTextView = findViewById(R.id.personID);

        nationalityTextView.setOnClickListener(v -> onNationalityClick());

        pictureImageView = findViewById(R.id.personPicture);
    }

    private void setupSensor() {
        try {
            proximitySensor = new ProximitySensor(this, this::onProximityUpdate);
        } catch (SensorNotFoundException ex) {
            Log.e(Tags.ERROR, "onCreate: ", ex);
            finish();
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void onNationalityClick() {

        if (displayedUser != null) {

            String country = getCountryName(displayedUser.getNationality());

            Uri mapUri = Uri.parse("geo:0,0?q=" + country);

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);

            mapIntent.setPackage("com.google.android.apps.maps");

            startActivity(mapIntent);
        }
    }

    private String getCountryName(String acronym) {

        return ApiInfo.NATIONALITY_NAMES.getOrDefault(acronym, acronym);

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

            displayUser(user);

            if (generator instanceof NetworkRandomUserGenerator) {
                new UserStorage(this).storeModel(user);
            }

        } catch (ExecutionException | InterruptedException | IOException ex) {
            Log.e(Tags.ERROR, "Fail to get User", ex);
        }
    }

    private void displayUser(User user) throws IOException {

        String nationalityText = getNationalityText(user);

        Bitmap bitmap = user.getProfileImage().getBitmap();

        runOnUiThread(
                () -> {

                    pictureImageView.setImageBitmap(bitmap);

                    firstTitleTextView.setText(user.getName());
                    titleTextView.setText(user.getTitle());
                    genderTextView.setText(user.getGender());
                    emailTextView.setText(user.getEmail());
                    nationalityTextView.setText(nationalityText);
                    ageTextView.setText(String.valueOf(user.getAge()));
                    idTextView.setText(user.getId());
                    birthTextView.setText(formatDate(user.getBirthDate()));

                    if (displayListener != null) {
                        displayListener.onDisplay();
                    }

                    displayedUser = user;
                }
        );
    }

    private String formatDate(Date birthDate) {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        return dateFormat.format(birthDate);
    }

    private RandomModelGenerator<User> findUserGenerator(RandomUserGeneratorInput input) {
        RandomModelGenerator<User> generator;
        try {
            generator = new NetworkRandomUserGenerator(this, input);

        } catch (NoNetworkException ex) {
            Log.e(Tags.ERROR, "Failed to get NetworkAccess", ex);
            generator = new StorageRandomUserGenerator(userStorage, input);
        }
        return generator;
    }

    private RandomUserGeneratorInput getIntentGeneratorInput() {
        Bundle bundle = getIntent().getBundleExtra(INPUT_BUNDLE_KEY);
        Objects.requireNonNull(bundle);

        return RandomUserGeneratorInput.fromBundle(bundle);
    }

    private String getNationalityText(User user) {
        String nationalityName = findNationalityName(user.getNationality());

        return nationalityName != null ? nationalityName : user.getNationality();
    }

    @Nullable
    private String findNationalityName(@NonNull String nationality) {

        TypedArray acronyms = getResources().obtainTypedArray(R.array.acronym);

        TypedArray names = getResources().obtainTypedArray(R.array.countries);

        String result = null;

        for (int i = 0; i < acronyms.length(); i++) {

            if (acronyms.getString(i).trim().equals(nationality.trim())) {
                result = names.getString(i);
            }
        }

        names.recycle();

        acronyms.recycle();

        return result;
    }

    // testing members

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