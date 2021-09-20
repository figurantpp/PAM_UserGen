package com.example.usergen.activity.login;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.usergen.service.AuthRepository;
import com.example.usergen.util.ViewModelFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class LoginViewModel extends ViewModel {

    private CompositeDisposable subscriptions = new CompositeDisposable();

    private final Subject<@Event Integer> events = PublishSubject.create();

    @NonNull
    private final AuthRepository repository;


    private LoginViewModel(@NonNull AuthRepository repository) {
        this.repository = repository;
    }

    @NonNull
    public Observable<@Event Integer> getEvents() {
        return events;
    }

    public void login(@NonNull String username, @Nullable String password) {

        if (username.trim().isEmpty()) {
            events.onNext(MISSING_USERNAME_ERROR);
            return;
        }

        if (password.trim().isEmpty()) {
            events.onNext(MISSING_PASSWORD_ERROR);
            return;
        }

        Disposable subscription = repository.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {

                    if (success) {
                        events.onNext(LOGIN_SUCCESS_EVENT);
                    } else {
                        events.onNext(LOGIN_FAILED_EVENT);
                    }

                });

        subscriptions.add(subscription);
    }

    public void requestRegister() {

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        subscriptions.clear();
    }

    @NonNull
    public static ViewModelProvider.Factory create(@NonNull AuthRepository repository) {
        return ViewModelFactory.from(
                LoginViewModel.class,
                () -> new LoginViewModel(repository)
        );
    }

    public static final int MISSING_USERNAME_ERROR = 2;
    public static final int MISSING_PASSWORD_ERROR = 3;
    public static final int LOGIN_SUCCESS_EVENT = 5;
    public static final int LOGIN_FAILED_EVENT = 8;
    public static final int START_REGISTER_EVENT = 13;

    @IntDef({MISSING_USERNAME_ERROR,
            MISSING_PASSWORD_ERROR,
            LOGIN_SUCCESS_EVENT,
            LOGIN_FAILED_EVENT,
            START_REGISTER_EVENT})
    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.TYPE_USE})
    public @interface Event {
    }
}
