package com.example.usergen.view.activity.user_list;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usergen.R;
import com.example.usergen.UsergenApplication;
import com.example.usergen.model.User;
import com.example.usergen.service.generator.RandomModelGenerator;
import com.example.usergen.service.generator.RandomUserGeneratorInput;
import com.example.usergen.service.testing.BooleanIdlingResource;
import com.example.usergen.view.fragment.UserListAdapter;

import java.util.List;
import java.util.Objects;

import static com.example.usergen.service.generator.RandomUserGeneratorResolver.resolveUserGenerator;


public class UserListActivity extends AppCompatActivity {

    public static final String INPUT_EXTRA_KEY = "potato";

    private RecyclerView recyclerView;

    private UserListViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_various_users);

        setupViews();

        setupViewModel();

        viewModel.fetchUsers();
    }

    private void setupViewModel() {

        RandomUserGeneratorInput input = getIntentInput();

        Objects.requireNonNull(input);

        RandomModelGenerator<User> generator = resolveUserGenerator(this, input);

        viewModel = new ViewModelProvider(this,
                UserListViewModel.create(
                        generator,
                        UsergenApplication.from(this).getFavoritesRepository()
                )
        ).get(UserListViewModel.class);

        viewModel.getFetchedUsers().observe(this, this::displayUsers);
    }

    private void displayUsers(List<User> users) {
        recyclerView.setAdapter(new UserListAdapter(users, this, user ->
                viewModel.switchFavorite(user))
        );

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        idleState.setIdle();
    }

    private void setupViews() {
        recyclerView = findViewById(R.id.showManyUsersActivity_usersRecyclerView);
    }

    private RandomUserGeneratorInput getIntentInput() {
        return RandomUserGeneratorInput.fromBundle(getIntent().getBundleExtra(INPUT_EXTRA_KEY));
    }

    @VisibleForTesting
    BooleanIdlingResource idleState = new BooleanIdlingResource();
}