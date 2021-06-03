package com.example.usergen.activity;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.example.usergen.R;
import com.example.usergen.model.OnlineImageResource;
import com.example.usergen.model.exception.NoNetworkException;
import com.example.usergen.model.exception.SensorNotFoundException;
import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.model.sensor.ProximitySensor;
import com.example.usergen.model.user.NetworkRandomUserGenerator;
import com.example.usergen.model.user.RandomUserGeneratorInput;
import com.example.usergen.model.user.StorageRandomUserGenerator;
import com.example.usergen.model.user.User;
import com.example.usergen.model.user.UserStorage;
import com.example.usergen.util.Tags;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowResultsActivity extends AppCompatActivity {

    public final static String INPUT_BUNDLE_KEY = "nationality";

    RandomModelGenerator<User> generator;

    CircleImageView profilePicture;

    TextView firstTitle, gender, email, birth, nationality, title, age, id;


    Date dayofbirth;

    UserStorage userStorage;

    ProximitySensor proximitySensor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);


        firstTitle = findViewById(R.id.personFirstandSecondName);
        gender = findViewById(R.id.personGender);
        email = findViewById(R.id.personEmail);
        birth = findViewById(R.id.personDayOfBirth);
        nationality = findViewById(R.id.personNacionality);
        title = findViewById(R.id.personTitle);
        age = findViewById(R.id.personAge);
        id = findViewById(R.id.personID);

        profilePicture = findViewById(R.id.personPicture);

        try {
            proximitySensor = new ProximitySensor(this, this::onProximityUpdate);
        }
        catch (SensorNotFoundException ex) {
            Log.e(Tags.ERROR, "onCreate: ", ex);
            finish();
        }


        new Thread(this::loadUser).start();


    }


    private void onProximityUpdate(boolean close) {
        if (close) {
            finish();
        }
    }

    @Nullable
    private String getNationalityName(@NonNull String nationality) {

        TypedArray acronyms = getResources().obtainTypedArray(R.array.acronym);

        TypedArray names = getResources().obtainTypedArray(R.array.countries);

        String result = null;

        for (int i = 0; i < acronyms.length(); i++) {

            if (acronyms.getString(i).trim().equals(nationality.trim()))
            {
                result = names.getString(i);
            }
        }

        names.recycle();

        acronyms.recycle();

        return result;
    }

    private void loadUser() {


        Bundle bundle = getIntent().getBundleExtra(INPUT_BUNDLE_KEY);
        Objects.requireNonNull(bundle);

        RandomUserGeneratorInput input = RandomUserGeneratorInput.fromBundle(bundle);

        userStorage = new UserStorage(this);

        try {
            generator = new NetworkRandomUserGenerator(this, input);

        } catch (NoNetworkException ex) {
            Log.e(Tags.ERROR, "Failed to get NetworkAccess", ex);
            generator = new StorageRandomUserGenerator(userStorage);
        }

        Future<User> result = generator.nextRandomModel();

        try {
            User user = result.get();

            String nationalityName = getNationalityName(user.getNationality());

            if (nationalityName != null) {
                user.setNationality(nationalityName);
            }

            if (generator.getClass() == NetworkRandomUserGenerator.class) {
                userStorage.storeModel(user);
            }
            OnlineImageResource resource = user.getProfileImage();
            Bitmap bitmap = resource.getBitmap();


            runOnUiThread(
                    () -> {
                        dayofbirth = user.getBirthDate();

                        profilePicture.setImageBitmap(bitmap);

                        firstTitle.setText(user.getName());
                        title.setText(user.getTitle());
                        gender.setText(user.getGender());
                        email.setText(user.getEmail());
                        nationality.setText(user.getNationality());
                        age.setText(String.valueOf(user.getAge()));
                        id.setText(user.getId());

                        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        birth.setText(dateFormat.format(dayofbirth));

                        if (listener != null) {
                            listener.onDisplay();
                        }
                    }
            );


        } catch (ExecutionException | InterruptedException | IOException e) {
            Log.e(Tags.ERROR, "Fail to get User", e);
        }
    }

    @VisibleForTesting
    @Nullable
    public ProximitySensor getProximitySensor() {
        return proximitySensor;
    }

    @VisibleForTesting()
    @Nullable
    public DisplayListener listener;

    @VisibleForTesting()
    public interface DisplayListener {
        void onDisplay();
    }
}