package com.example.usergen.view.activity.favorite_list;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usergen.R;
import com.example.usergen.UsergenApplication;
import com.example.usergen.model.User;
import com.example.usergen.view.fragment.UserListAdapter;

import java.util.List;

public class FavoriteListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private FavoritesViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        setupViews();

        setupViewModel();

        viewModel.fetchFavorites();
    }

    private void setupViewModel() {

        viewModel = new ViewModelProvider(this,
                FavoritesViewModel.create(
                        UsergenApplication.from(this).getFavoritesRepository()
                )
        ).get(FavoritesViewModel.class);

        viewModel.getFavorites().observe(this, this::displayUsers);
    }

    private void displayUsers(List<User> users) {
        recyclerView.setAdapter(new UserListAdapter(users, this, user ->
                viewModel.switchFavorite(user))
        );

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupViews() {
        recyclerView = findViewById(R.id.favoriteListActivity_usersRecyclerView);
    }
}