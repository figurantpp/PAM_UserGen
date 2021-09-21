package com.example.usergen.view.activity.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.usergen.model.Settings;
import com.example.usergen.service.generator.RandomUserGeneratorInput;
import com.example.usergen.service.settings.SettingsRepository;
import com.example.usergen.util.ViewModelFactory;

import java.util.function.Function;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MainActivityViewModel extends ViewModel {

    private final PublishSubject<Event> events = PublishSubject.create();

    @NonNull
    private final SettingsRepository settingsRepository;

    @NonNull
    private final CompositeDisposable subscriptions = new CompositeDisposable();

    @NonNull
    private final MutableLiveData<Settings> fetchedSettings = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull SettingsRepository repository) {
        this.settingsRepository = repository;
    }

    @NonNull
    public Observable<Event> getEvents() {
        return events;
    }

    @NonNull
    public LiveData<Settings> getFetchedSettings() {
        return fetchedSettings;
    }

    public void fetchSettings() {

        Disposable subscription = settingsRepository.getSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fetchedSettings::setValue);

        subscriptions.add(subscription);
    }

    public void startSingleUserQuery(@NonNull Input input) {
        startQuery(input, GenerateSingleUserEvent::new);
    }

    public void startMultipleUserQuery(@NonNull Input input) {
        startQuery(input, GenerateManyUsersEvent::new);
    }

    private void startQuery(
            Input input,
            Function<RandomUserGeneratorInput, Event> eventSupplier
    ) {

        RandomUserGeneratorInput generatorInput = validateInput(input);

        if (generatorInput != null) {
            settingsRepository.saveSettings(
                    new Settings(
                            generatorInput.getGender(),
                            generatorInput.getNationality()
                    )
            ).subscribeOn(Schedulers.io())
            .subscribe();

            events.onNext(eventSupplier.apply(generatorInput));
        }
    }

    @Nullable
    private RandomUserGeneratorInput validateInput(Input input) {

        if (!input.queryMale && !input.queryFemale) {

            events.onNext(new UncheckedSexError());

            return null;
        } else {
            String queryGender = input.queryFemale ? "female" : "male";

            String nationality = input.nationality;

            if (input.nationality.trim().isEmpty()) {
                nationality = null;
            }

            return new RandomUserGeneratorInput(
                    nationality,
                    queryGender
            );
        }

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        subscriptions.clear();
    }

    @NonNull
    public static ViewModelProvider.Factory create(@NonNull SettingsRepository repository) {

        return ViewModelFactory.from(
                MainActivityViewModel.class,
                () -> new MainActivityViewModel(repository)
        );

    }

    public static class Input {
        public boolean queryFemale;
        public boolean queryMale;

        @NonNull
        public String nationality;

        public Input(boolean queryFemale, boolean queryMale, @NonNull String nationality) {
            this.queryFemale = queryFemale;
            this.queryMale = queryMale;
            this.nationality = nationality;
        }
    }


    public interface Event {
        void accept(@NonNull Visitor visitor);

        interface Visitor {
            void visit(@NonNull UncheckedSexError error);

            void visit(@NonNull GenerateSingleUserEvent event);

            void visit(@NonNull GenerateManyUsersEvent event);
        }
    }

    static class UncheckedSexError implements Event {

        @Override
        public void accept(@NonNull Visitor visitor) {
            visitor.visit(this);
        }
    }

    static class GenerateSingleUserEvent implements Event {

        @NonNull
        public final RandomUserGeneratorInput input;

        public GenerateSingleUserEvent(@NonNull RandomUserGeneratorInput input) {
            this.input = input;
        }

        @Override
        public void accept(@NonNull Visitor visitor) {
            visitor.visit(this);
        }
    }

    static class GenerateManyUsersEvent implements Event {

        @NonNull
        public final RandomUserGeneratorInput input;

        public GenerateManyUsersEvent(@NonNull RandomUserGeneratorInput input) {
            this.input = input;
        }

        @Override
        public void accept(@NonNull Visitor visitor) {
            visitor.visit(this);
        }
    }

}
