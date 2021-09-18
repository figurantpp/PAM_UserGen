package com.example.usergen.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usergen.R;
import com.example.usergen.model.BooleanIdlingResource;
import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.model.user.User;
import com.example.usergen.model.user.generator.RandomUserGeneratorInput;
import com.example.usergen.model.view.UserListAdapter;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.example.usergen.model.user.generator.RandomUserGeneratorResolver.resolveUserGenerator;


public class ShowVariousUsersActivity extends AppCompatActivity {

    public static final String INPUT_EXTRA_KEY = "potato";

    RecyclerView recyclerView;

    private CompositeDisposable subscriptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_various_users);

        subscriptions = new CompositeDisposable();

        RandomUserGeneratorInput input
                = RandomUserGeneratorInput.fromBundle(getIntent().getBundleExtra(INPUT_EXTRA_KEY));

        Objects.requireNonNull(input);

        recyclerView = findViewById(R.id.recycler_users);

        try {
            getUsers(input);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void getUsers(RandomUserGeneratorInput input) {

        RandomModelGenerator<User> generator = resolveUserGenerator(this, input);

        Single<List<User>> result = generator.nextModels(10);

        Disposable subscription = result.subscribeOn(Schedulers.io())
                .doOnSuccess(users ->  {
                    for (User user : users) {
                        user.getProfileImage().getBitmapSync();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {

                    recyclerView.setAdapter(new UserListAdapter(users, this));

                    recyclerView.setHasFixedSize(true);

                    recyclerView.setLayoutManager(new LinearLayoutManager(this));

                    runOnUiThread(idleState::setIdle);
                });

        subscriptions.add(subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.dispose();
    }

    @VisibleForTesting
    BooleanIdlingResource idleState = new BooleanIdlingResource();
}