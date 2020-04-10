package com.example.minitwitter.ui.profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constants;
import com.example.minitwitter.data.ProfileViewModel;
import com.example.minitwitter.retrofit.response.ResponseUserProfile;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private ImageView ivAvatar;
    private EditText etUsername, etEmail, etPassword, etWebsite, etDescription;
    private Button btnSave, btnChangePassword;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        ivAvatar = view.findViewById(R.id.imageViewAvatar);
        etUsername = view.findViewById(R.id.editTextUsername);
        etEmail = view.findViewById(R.id.editTextEmail);
        etPassword = view.findViewById(R.id.editTextPassword);
        etWebsite = view.findViewById(R.id.editTextUserWebsite);
        etDescription = view.findViewById(R.id.editTextUserDescription);
        btnSave = view.findViewById(R.id.buttonSave);
        btnChangePassword = view.findViewById(R.id.buttonChangePassword);

        profileViewModel.getResponseUserProfileLiveData().observe(getActivity(), responseUserProfile -> {
            etUsername.setText(responseUserProfile.getUsername());
            etEmail.setText(responseUserProfile.getEmail());
            etWebsite.setText(responseUserProfile.getWebsite());
            etDescription.setText(responseUserProfile.getDescription());
            if (!responseUserProfile.getPhotoUrl().isEmpty()) {
                Glide.with(getActivity())
                        .load(Constants.API_FILES_URL + responseUserProfile.getPhotoUrl())
                        .into(ivAvatar);
            }
        });
        return view;
    }
}