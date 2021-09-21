package com.example.usergen.view.activity.single_user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.usergen.R;
import com.example.usergen.service.generator.RandomUserGeneratorInput;
import com.example.usergen.service.sensor.ProximitySensor;
import com.example.usergen.service.sensor.SensorNotFoundException;
import com.example.usergen.service.storage.UserStorage;
import com.example.usergen.util.RandomApiInfo;
import com.example.usergen.util.Tags;
import com.example.usergen.view.dialog.ImageDialogBox;
import com.example.usergen.view.fragment.ShowUserFragment;

import java.util.Objects;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

import static com.example.usergen.service.generator.RandomUserGeneratorResolver.resolveUserGenerator;

public class ShowUserActivity extends AppCompatActivity implements SingleUserViewModel.Event.Visitor {

    public final static String INPUT_EXTRA_KEY = "some_extra_key";

    ProximitySensor proximitySensor;

    private CompositeDisposable subscriptions;

    private SingleUserViewModel userViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);

        subscriptions = new CompositeDisposable();

        setupUserViewModel();

        setupSensor();

        userViewModel.fetchUser();
    }

    private void setupUserViewModel() {

        userViewModel = new ViewModelProvider(
                this,
                SingleUserViewModel.create(
                        () -> new UserStorage(this),
                        resolveUserGenerator(this, getIntentGeneratorInput())
                )
        ).get(SingleUserViewModel.class);

        userViewModel.getFetchedUser().observe(this, user -> startUserFragment());
    }

    @Override
    protected void onStart() {
        super.onStart();

        Disposable subscription = userViewModel
                .getEvents()
                .subscribe(event -> event.accept(this));
        subscriptions.add(subscription);
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscriptions.clear();
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

    private void startUserFragment() {

        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.showUserActivity_fragmentContainerView, ShowUserFragment.class, null)
                .commit();
    }

    private RandomUserGeneratorInput getIntentGeneratorInput() {

        Bundle bundle = getIntent().getBundleExtra(INPUT_EXTRA_KEY);

        Objects.requireNonNull(bundle);

        return RandomUserGeneratorInput.fromBundle(bundle);
    }

    @Override
    public void visit(@NonNull SingleUserViewModel.DisplayFinishedEvent event) {
        notifyDisplayFinished();
    }

    @Override
    public void visit(@NonNull SingleUserViewModel.ShowImageDialogEvent event) {

        ImageDialogBox dialogBox = new ImageDialogBox(this);
        ImageView dialogImageView = dialogBox.getImageView();

        dialogImageView.setImageBitmap(event.image.requireBitmap());

        dialogBox.show();
    }

    @Override
    public void visit(@NonNull SingleUserViewModel.DisplayCountryEvent event) {

        String country = getCountryName(event.nationality);

        Uri mapUri = Uri.parse("geo:0,0?q=" + country);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);

        mapIntent.setPackage("com.google.android.apps.maps");

        startActivity(mapIntent);

    }

    private String getCountryName(String acronym) {

        return RandomApiInfo.NATIONALITY_NAMES.getOrDefault(acronym, acronym);

    }

    // unit testing members, don't touch

    public void notifyDisplayFinished() {

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