package com.example.usergen.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.usergen.R;
import com.example.usergen.model.exception.SensorNotFoundException;
import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.model.sensor.ProximitySensor;
import com.example.usergen.model.user.User;
import com.example.usergen.model.user.UserStorage;
import com.example.usergen.model.user.UserViewModel;
import com.example.usergen.model.user.generator.NetworkRandomUserGenerator;
import com.example.usergen.model.user.generator.RandomUserGeneratorInput;
import com.example.usergen.util.Tags;
import com.example.usergen.view.ShowUserFragment;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.example.usergen.model.user.generator.RandomUserGeneratorResolver.resolveUserGenerator;

public class ShowUserActivity extends AppCompatActivity {

    public final static String INPUT_EXTRA_KEY = "nationality";

    ProximitySensor proximitySensor;

    private UserViewModel userViewModel;

    private CompositeDisposable subscriptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);

        subscriptions = new CompositeDisposable();

        setupUserViewModel();

        setupSensor();

        new Thread(this::loadUser).start();
    }

    private void setupUserViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getDisplayEvent().observe(this, v -> notifyDisplay());

        userViewModel.getSelectedUser().observe(this, user -> displayUserFragment());
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

    private void displayUserFragment() {

        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view, ShowUserFragment.class, null)
                .commit();
    }

    private void loadUser() {

        RandomUserGeneratorInput input = getIntentGeneratorInput();

        RandomModelGenerator<User> generator = findUserGenerator(input);

        Single<User> result = generator.nextRandomModel();

        Disposable subscription =
                result.subscribeOn(Schedulers.io())
                .doOnSuccess(user -> user.getProfileImage().getBitmapSync())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {

                    if (generator instanceof NetworkRandomUserGenerator) {
                        new UserStorage(this).storeModel(user);
                    }

                    userViewModel.selectUser(user);
                });

        subscriptions.add(subscription);
    }

    private RandomModelGenerator<User> findUserGenerator(RandomUserGeneratorInput input) {

        RandomModelGenerator<User> generator = resolveUserGenerator(this, input);

        if (!(generator instanceof NetworkRandomUserGenerator)) {
            Log.e(Tags.ERROR, "Failed to get network access");
        }

        return generator;
    }

    private RandomUserGeneratorInput getIntentGeneratorInput() {
        Bundle bundle = getIntent().getBundleExtra(INPUT_EXTRA_KEY);
        Objects.requireNonNull(bundle);

        return RandomUserGeneratorInput.fromBundle(bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.clear();
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