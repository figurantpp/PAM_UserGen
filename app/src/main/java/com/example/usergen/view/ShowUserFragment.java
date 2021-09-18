package com.example.usergen.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.usergen.R;
import com.example.usergen.dialog.ImageDialogBox;
import com.example.usergen.model.user.User;
import com.example.usergen.model.user.UserViewModel;
import com.example.usergen.util.ApiInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.util.Objects.requireNonNull;

public class ShowUserFragment extends Fragment {
    public ShowUserFragment() {
        super(R.layout.fragment_show_user);
    }

    TextView firstTitleTextView,
            genderTextView,
            emailTextView,
            birthTextView,
            nationalityTextView,
            titleTextView,
            ageTextView,
            idTextView;

    CircleImageView pictureImageView;


    private UserViewModel userViewModel;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews(view);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getSelectedUser().observe(getViewLifecycleOwner(), this::displayUser);
    }

    private void setupViews(View source) {
        firstTitleTextView = source.findViewById(R.id.personFirstandSecondName);
        genderTextView = source.findViewById(R.id.personGender);
        emailTextView = source.findViewById(R.id.personEmail);
        birthTextView = source.findViewById(R.id.personDayOfBirth);
        nationalityTextView = source.findViewById(R.id.personNationality);
        titleTextView = source.findViewById(R.id.personTitle);
        ageTextView = source.findViewById(R.id.personAge);
        idTextView = source.findViewById(R.id.personID);

        nationalityTextView.setOnClickListener(v -> onNationalityClick());

        pictureImageView = source.findViewById(R.id.personPicture);

        pictureImageView.setOnClickListener(view -> {

            ImageDialogBox dialogBox = new ImageDialogBox(requireContext());
            ImageView dialogImageView = dialogBox.getImageView();

            dialogImageView.setImageBitmap(
                    requireNonNull(userViewModel.getSelectedUser().getValue())
                            .getProfileImage().requireBitmap()
            );

            dialogBox.show();

        });
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void onNationalityClick() {

        if (userViewModel != null) {
            User user = requireNonNull(userViewModel.getSelectedUser().getValue());

            String country = getCountryName(user.getNationality());

            Uri mapUri = Uri.parse("geo:0,0?q=" + country);

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);

            mapIntent.setPackage("com.google.android.apps.maps");

            startActivity(mapIntent);
        }
    }

    private String getCountryName(String acronym) {

        return ApiInfo.NATIONALITY_NAMES.getOrDefault(acronym, acronym);

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
        idTextView.setText(user.getId());
        birthTextView.setText(formatDate(user.getBirthDate()));

        notifyDisplay();
    }

    private String formatDate(Date birthDate) {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        return dateFormat.format(birthDate);
    }

    private void notifyDisplay() {
        userViewModel.fireDisplayEvent();
    }

    private String getNationalityText(User user) {
        String nationalityName = findNationalityName(user.getNationality());

        return nationalityName != null ? nationalityName : user.getNationality();
    }

    @Nullable
    private String findNationalityName(@NonNull String nationality) {

        TypedArray acronyms = getResources().obtainTypedArray(R.array.acronym);

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
