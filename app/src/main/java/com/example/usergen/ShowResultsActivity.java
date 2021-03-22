package com.example.usergen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.usergen.model.OnlineImageResource;
import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.model.user.NetworkRandomUserGenerator;
import com.example.usergen.model.user.RandomUserGeneratorInput;
import com.example.usergen.model.user.User;
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

    String personTitle, Name, Gender, Email, Birth, Nationality, Age, ID;

    Date dayofbirth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);



        firstTitle =  findViewById(R.id.personFirstandSecondName);
        gender = findViewById(R.id.personGender);
        email =  findViewById(R.id.personEmail);
        birth =  findViewById(R.id.personDayOfBirth);
        nationality = findViewById(R.id.personNacionality);
        title = findViewById(R.id.personTitle);
        age =  findViewById(R.id.personAge);
        id = findViewById(R.id.personID);

        profilePicture = (CircleImageView) findViewById(R.id.personPicture);

        new Thread(this::loadUser).start();


    }

    private void loadUser()
    {
        Bundle bundle  = getIntent().getBundleExtra(INPUT_BUNDLE_KEY);
        Objects.requireNonNull(bundle);

        RandomUserGeneratorInput input = RandomUserGeneratorInput.fromBundle(bundle);

        generator = new NetworkRandomUserGenerator(this, input);

        Future<User> result = generator.nextRandomModel();

        try {
            User user = result.get();

            Name = user.getName();
            personTitle = user.getTitle();
            Gender = user.getGender();
            Email = user.getEmail();
            dayofbirth = user.getBirthDate();
            Nationality = user.getNationality();
            Age = String.valueOf(user.getAge());
            ID = user.getId();

            OnlineImageResource resource = user.getProfileImage();
            Bitmap bitmap = resource.getBitmap();

            profilePicture.setImageBitmap(bitmap);
            firstTitle.setText(Name);
            title.setText(personTitle);
            gender.setText(Gender);
            email.setText(Email);
            nationality.setText(Nationality);
            age.setText(Age);
            id.setText(ID);

            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            birth.setText(dateFormat.format(dayofbirth));






        } catch (ExecutionException | InterruptedException | IOException e) {
            Log.e(Tags.ERROR,"Fail to get User", e);
        }
    }
}