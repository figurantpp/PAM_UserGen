package com.example.usergen.view.activity.favorite_list;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.usergen.model.User;
import com.example.usergen.service.favorite.FavoritesRepository;
import com.example.usergen.util.ViewModelFactory;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritesViewModel extends ViewModel {

    private final MutableLiveData<List<User>> favorites = new MutableLiveData<>();

    @NonNull
    private final FavoritesRepository repository;

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    public FavoritesViewModel(@NonNull FavoritesRepository repository) {
        this.repository = repository;
    }

    @NonNull
    public LiveData<List<User>> getFavorites() {
        return favorites;
    }

    public void fetchFavorites() {

        Disposable subscription = repository.listFavorites()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(users -> {
                    for (User user : users) {
                        user.getProfileImage().getBitmapSync();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favorites::setValue);

        subscriptions.add(subscription);
    }

    public void switchFavorite(@NonNull User user) {

        if (user.isFavorite()) {

            String id = Objects.requireNonNull(user.getApiId());

            repository.deleteFavorite(id)
                    .subscribeOn(Schedulers.io())
                    .subscribe();

            user.setIsFavorite(false);
        } else {
            subscriptions.add(
                    repository.registerFavorite(user)
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
    public static ViewModelProvider.Factory create(@NonNull FavoritesRepository favoritesRepository) {
        return ViewModelFactory.from(
                FavoritesViewModel.class,
                () -> new FavoritesViewModel(favoritesRepository)
        );
    }
}
