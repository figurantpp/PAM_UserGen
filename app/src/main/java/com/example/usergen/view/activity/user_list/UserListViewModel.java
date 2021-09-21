package com.example.usergen.view.activity.user_list;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.usergen.model.User;
import com.example.usergen.service.favorite.FavoritesRepository;
import com.example.usergen.service.generator.RandomModelGenerator;
import com.example.usergen.util.ViewModelFactory;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserListViewModel extends ViewModel {

    private final MutableLiveData<List<User>> fetchedUsers = new MutableLiveData<>();

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    @NonNull
    private final RandomModelGenerator<User> generator;

    @NonNull
    private final FavoritesRepository favoritesRepository;

    public UserListViewModel(
            @NonNull RandomModelGenerator<User> generator,
            @NonNull FavoritesRepository favoritesRepository
    ) {
        this.generator = generator;
        this.favoritesRepository = favoritesRepository;
    }

    @NonNull
    public LiveData<List<User>> getFetchedUsers() {
        return fetchedUsers;
    }

    public void fetchUsers() {

        Single<List<User>> result = generator.nextModels(10);

        Disposable subscription = result.subscribeOn(Schedulers.io())
                .doOnSuccess(users -> {
                    for (User user : users) {
                        user.getProfileImage().getBitmapSync();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(fetchedUsers::setValue);

        subscriptions.add(subscription);
    }

    public void switchFavorite(@NonNull User user) {

        if (user.isFavorite()) {
            favoritesRepository.deleteFavorite(user.getApiId())
                    .subscribeOn(Schedulers.io())
                    .subscribe();

            user.setIsFavorite(false);
        } else {
            subscriptions.add(
                    favoritesRepository.registerFavorite(user)
                            .subscribeOn(Schedulers.io())
                            .subscribe(user::setApiId)
            );

            user.setIsFavorite(true);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        subscriptions.clear();
    }

    @NonNull
    public static ViewModelProvider.Factory create(
            @NonNull RandomModelGenerator<User> generator,
            @NonNull FavoritesRepository repository
    ) {
        return ViewModelFactory.from(
                UserListViewModel.class,
                () -> new UserListViewModel(generator, repository)

        );
    }

}
