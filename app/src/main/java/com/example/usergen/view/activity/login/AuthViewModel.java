package com.example.usergen.view.activity.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.usergen.service.auth.AuthRepository;
import com.example.usergen.util.ViewModelFactory;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class AuthViewModel extends ViewModel {

    // todo: bind logout textView on mainActivity

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    private final Subject<Integer> events = PublishSubject.create();

    @NonNull
    private final AuthRepository repository;


    private AuthViewModel(@NonNull AuthRepository repository) {
        this.repository = repository;
    }

    @NonNull
    public Observable<Integer> getEvents() {
        return events;
    }

    public void login(@NonNull String username, @NonNull String password) {

        username = username.trim();

        if (username.isEmpty()) {
            events.onNext(MISSING_USERNAME);
            return;
        }

        if (password.isEmpty()) {
            events.onNext(MISSING_PASSWORD);
            return;
        }

        Disposable subscription = repository.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {

                    if (success) {
                        events.onNext(DISPLAY_CONTENT);
                    } else {
                        events.onNext(LOGIN_FAILED);
                    }

                });

        subscriptions.add(subscription);
    }

    public void register(
            @NonNull String username,
            @NonNull String password,
            @NonNull String passwordConfirmation
    ) {
        username = username.trim();

        if (username.isEmpty()) {
            events.onNext(MISSING_USERNAME);
            return;
        }

        if (password.isEmpty()) {
            events.onNext(MISSING_PASSWORD);
            return;
        }

        if (passwordConfirmation.isEmpty()) {
            events.onNext(MISSING_PASSWORD_CONFIRMATION_ERROR);
            return;
        }

        if (!password.equals(passwordConfirmation)) {
            events.onNext(PASSWORD_NOT_EQUALS_CONFIRMATION);
            return;
        }

        Disposable subscription = repository.register(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {

                    if (success) {
                        events.onNext(FINISH_REGISTER);
                    } else {
                        events.onNext(USERNAME_TAKEN);
                    }

                });

        subscriptions.add(subscription);
    }

    public void checkAlreadyLogged() {

        Disposable subscription = repository
                .isAlreadyLogged()
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleLoginStatus, this::handleError);

        subscriptions.add(
                subscription
        );
    }


    public void requestRegisterScreen() {
        events.onNext(DISPLAY_REGISTER_SCREEN);
    }

    public void requestQuitRegister() {
        events.onNext(FINISH_REGISTER);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        subscriptions.clear();
    }

    private void handleLoginStatus(Boolean isLogged) {
        if (isLogged) {
            events.onNext(DISPLAY_CONTENT_NO_COMEBACK);
        }
    }

    private void handleError(Throwable error) {

    }

    @NonNull
    public static ViewModelProvider.Factory create(@NonNull AuthRepository repository) {
        return ViewModelFactory.from(
                AuthViewModel.class,
                () -> new AuthViewModel(repository)
        );
    }

    public static final int MISSING_USERNAME = 1;
    public static final int MISSING_PASSWORD = 2;
    public static final int MISSING_PASSWORD_CONFIRMATION_ERROR = 3;
    public static final int PASSWORD_NOT_EQUALS_CONFIRMATION = 5;
    public static final int DISPLAY_CONTENT = 8;
    public static final int DISPLAY_CONTENT_NO_COMEBACK = 13;
    public static final int LOGIN_FAILED = 21;
    public static final int DISPLAY_REGISTER_SCREEN = 34;
    public static final int FINISH_REGISTER = 55;
    public static final int USERNAME_TAKEN = 89;

}
