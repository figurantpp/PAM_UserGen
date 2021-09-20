package com.example.usergen.view.activity.single_user;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.usergen.model.User;
import com.example.usergen.service.generator.NetworkRandomUserGenerator;
import com.example.usergen.service.generator.RandomModelGenerator;
import com.example.usergen.service.http.OnlineImageResource;
import com.example.usergen.service.storage.UserStorage;
import com.example.usergen.util.ViewModelFactory;

import java.util.function.Supplier;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class SingleUserViewModel extends ViewModel {

    private final MutableLiveData<User> fetchedUser = new MutableLiveData<>();

    private final PublishSubject<Event> events = PublishSubject.create();

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    private final Supplier<UserStorage> storageSupplier;

    private final RandomModelGenerator<User> generator;

    public SingleUserViewModel(
            @NonNull Supplier<UserStorage> storageSupplier,
            @NonNull RandomModelGenerator<User> generator
    ) {
        this.storageSupplier = storageSupplier;
        this.generator = generator;
    }

    @NonNull
    public LiveData<User> getFetchedUser() {
        return fetchedUser;
    }

    @NonNull
    public Observable<Event> getEvents() {
        return events;
    }

    public void fetchUser() {

        Single<User> result = generator.nextRandomModel();

        Disposable subscription =
                result.subscribeOn(Schedulers.io())
                        .doOnSuccess(user -> user.getProfileImage().getBitmapSync())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {

                            if (generator instanceof NetworkRandomUserGenerator) {
                                storageSupplier.get().storeModel(user);
                            }

                            fetchedUser.setValue(user);
                        });


        subscriptions.add(subscription);
    }

    public void notifyDisplayFinished() {
        events.onNext(new DisplayFinishedEvent());
    }

    public void expandUserImageView() {

        User user = fetchedUser.getValue();

        if (user != null) {
            events.onNext(new ShowImageDialogEvent(user.getProfileImage()));
        }
    }

    public void displayUserCountry() {

        User user = fetchedUser.getValue();

        events.onNext(new DisplayCountryEvent(user.getNationality()));
    }


    @NonNull
    public static ViewModelProvider.Factory create(
            @NonNull Supplier<UserStorage> storageSupplier,
            @NonNull RandomModelGenerator<User> generator
    ) {
        return ViewModelFactory.from(SingleUserViewModel.class, () -> new SingleUserViewModel(
                storageSupplier, generator
        ));
    }


    @Override
    protected void onCleared() {
        subscriptions.dispose();
    }

    public interface Event {

        void accept(@NonNull Visitor visitor);

        interface Visitor {
            void visit(@NonNull DisplayFinishedEvent event);
            void visit(@NonNull ShowImageDialogEvent event);
            void visit(@NonNull DisplayCountryEvent event);
        }
    }

    public static class DisplayFinishedEvent implements Event {

        @Override
        public void accept(@NonNull Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static class DisplayCountryEvent implements Event {

        @NonNull
        public String nationality;

        public DisplayCountryEvent(@NonNull String nationality) {
            this.nationality = nationality;
        }

        @Override
        public void accept(@NonNull Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static class ShowImageDialogEvent implements Event {

        @NonNull
        public OnlineImageResource image;

        public ShowImageDialogEvent(@NonNull OnlineImageResource image) {
            this.image = image;
        }

        @Override
        public void accept(@NonNull Visitor visitor) {
            visitor.visit(this);
        }
    }
}
