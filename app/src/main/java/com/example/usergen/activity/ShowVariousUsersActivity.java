package com.example.usergen.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usergen.R;
import com.example.usergen.model.BooleanIdlingResource;
import com.example.usergen.model.exception.NoNetworkException;
import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.model.user.NetworkRandomUserGenerator;
import com.example.usergen.model.user.RandomUserGeneratorInput;
import com.example.usergen.model.user.User;
import com.example.usergen.model.view.UserListAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class ShowVariousUsersActivity extends AppCompatActivity {

    public static final String INPUT_EXTRA_KEY = "potato";

    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_various_users);


        RandomUserGeneratorInput input
                = RandomUserGeneratorInput.fromBundle(getIntent().getBundleExtra(INPUT_EXTRA_KEY));

        Objects.requireNonNull(input);

        recyclerView = findViewById(R.id.recycler_users);

        new Thread(() -> {

            try {
                getUsers(input);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }

        }).start();




        
    }

    private void getUsers(RandomUserGeneratorInput input)
            throws InterruptedException, ExecutionException, NoNetworkException, IOException {

        RandomModelGenerator<User> generator = new NetworkRandomUserGenerator(this, input);

        Future<List<User>> future = generator.nextModels(10);

        List<User> users = future.get();

        for (User user : users) {
            user.getProfileImage().getBitmap();
        }

        runOnUiThread(() -> {

            recyclerView.setAdapter(new UserListAdapter(users));

            recyclerView.setHasFixedSize(true);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            runOnUiThread(idleState::setIdle);
        });
    }

    @VisibleForTesting
    BooleanIdlingResource idleState = new BooleanIdlingResource();
}