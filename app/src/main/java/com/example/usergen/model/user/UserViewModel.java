package com.example.usergen.model.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.usergen.util.SingleLiveEvent;

public class UserViewModel extends ViewModel {

    private final MutableLiveData<User> selectedUser = new MutableLiveData<>();

    private final SingleLiveEvent<Void> displayEvent = new SingleLiveEvent<>();

    public void selectUser(@Nullable User user) {
        selectedUser.setValue(user);
    }

    @NonNull
    public LiveData<User> getSelectedUser() {
        return selectedUser;
    }

    @NonNull
    public LiveData<Void> getDisplayEvent() {
        return displayEvent;
    }

    public void fireDisplayEvent() {
       displayEvent.setValue(null);
    }
}
