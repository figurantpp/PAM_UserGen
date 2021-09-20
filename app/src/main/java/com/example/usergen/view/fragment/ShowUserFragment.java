package com.example.usergen.view.fragment;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
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

public class ShowUserFragment extends Fragment {
    public ShowUserFragment() {
        super(R.layout.fragment_show_user);
    }

    TextView firstTitleTextView;
    TextView genderTextView;
    TextView emailTextView;
    TextView birthTextView;
    TextView nationalityTextView;
    TextView titleTextView;
    TextView ageTextView;
    TextView idTextView;

    CircleImageView pictureImageView;

    private SingleUserViewModel userViewModel;


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
        firstTitleTextView = source.findViewById(R.id.personFirstandSecondName);
        genderTextView = source.findViewById(R.id.personGender);
        emailTextView = source.findViewById(R.id.personEmail);
        birthTextView = source.findViewById(R.id.personDayOfBirth);
        titleTextView = source.findViewById(R.id.personTitle);
        ageTextView = source.findViewById(R.id.personAge);
        idTextView = source.findViewById(R.id.personID);

        nationalityTextView = source.findViewById(R.id.personNationality);

        nationalityTextView.setOnClickListener(
                v -> userViewModel.displayUserCountry()
        );

        pictureImageView = source.findViewById(R.id.personPicture);

        pictureImageView.setOnClickListener(v ->
                userViewModel.expandUserImageView()
        );
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
