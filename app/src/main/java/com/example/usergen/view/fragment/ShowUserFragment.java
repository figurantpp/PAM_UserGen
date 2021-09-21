package com.example.usergen.view.fragment;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.usergen.R;
import com.example.usergen.model.User;
import com.example.usergen.view.activity.single_user.SingleUserViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.disposables.Disposable;

public class ShowUserFragment extends Fragment {
    public ShowUserFragment() {
        super(R.layout.fragment_show_user);
    }

    private TextView firstTitleTextView;
    private TextView genderTextView;
    private TextView emailTextView;
    private TextView birthTextView;
    private TextView nationalityTextView;
    private TextView titleTextView;
    private TextView ageTextView;
    private TextView idTextView;

    private CircleImageView pictureImageView;

    private SingleUserViewModel userViewModel;

    private ImageView favoriteImageView;

    private Disposable disposable;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews(view);

        setupViewModel();
    }

    private void setupViewModel() {
        userViewModel = new ViewModelProvider(requireActivity()).get(SingleUserViewModel.class);

        userViewModel.getFetchedUser().observe(getViewLifecycleOwner(), this::displayUser);
    }

    private void setupViews(View source) {
        firstTitleTextView = source.findViewById(R.id.showUserFragment_firstTitleTextView);
        genderTextView = source.findViewById(R.id.showUserFragment_genderTextView);
        emailTextView = source.findViewById(R.id.showUserFragment_emailTextView);
        birthTextView = source.findViewById(R.id.showUserFragment_birthDateTextView);
        titleTextView = source.findViewById(R.id.showUserFragment_titleTextView);
        ageTextView = source.findViewById(R.id.showUserFragment_ageTextView);
        idTextView = source.findViewById(R.id.showUserFragment_idTextView);

        nationalityTextView = source.findViewById(R.id.personNationality);

        nationalityTextView.setOnClickListener(
                v -> userViewModel.displayUserCountry()
        );

        pictureImageView = source.findViewById(R.id.personPicture);

        pictureImageView.setOnClickListener(v ->
                userViewModel.expandUserImageView()
        );

        favoriteImageView = source.findViewById(R.id.showUserFragment_favoriteImageView);

        favoriteImageView.setOnClickListener(f -> userViewModel.toggleFavorite());
    }

    @Override
    public void onStart() {
        super.onStart();
        disposable = userViewModel.getEvents()
                .filter(it -> it instanceof SingleUserViewModel.DisplayUserFavoriteEvent)
                .map(it -> (SingleUserViewModel.DisplayUserFavoriteEvent) it)
                .subscribe(event -> {
                    boolean isFavorite = event.isFavorite;

                    favoriteImageView.setImageResource(
                            isFavorite
                                    ? R.drawable.ic_heart_filled
                                    : R.drawable.ic_heart_not_filled
                    );
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.dispose();
    }

    private void displayUser(User user) {

        String nationalityText = getNationalityText(user);

        Bitmap bitmap = user.getProfileImage().requireBitmap();

        pictureImageView.setImageBitmap(bitmap);

        firstTitleTextView.setText(user.getName());
        titleTextView.setText(user.getTitle());
        genderTextView.setText(user.getGender());
        emailTextView.setText(user.getEmail());
        nationalityTextView.setText(nationalityText);
        ageTextView.setText(String.valueOf(user.getAge()));
        idTextView.setText(user.getSourceId());
        birthTextView.setText(formatDate(user.getBirthDate()));

        userViewModel.notifyDisplayFinished();
    }

    private String formatDate(Date birthDate) {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        return dateFormat.format(birthDate);
    }

    private String getNationalityText(User user) {
        String nationalityName = findNationalityName(user.getNationality());

        return nationalityName != null ? nationalityName : user.getNationality();
    }

    @Nullable
    private String findNationalityName(@NonNull String nationality) {

        TypedArray acronyms = getResources().obtainTypedArray(R.array.acronyms);

        TypedArray names = getResources().obtainTypedArray(R.array.countries);

        String result = null;

        for (int i = 0; i < acronyms.length(); i++) {

            if (acronyms.getString(i).trim().equals(nationality.trim())) {
                result = names.getString(i);
            }
        }

        names.recycle();

        acronyms.recycle();

        return result;
    }


}
