package com.example.minitwitter.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.minitwitter.retrofit.request.RequestUserProfile;
import com.example.minitwitter.retrofit.response.ResponseUserProfile;


public class ProfileViewModel extends AndroidViewModel {

    private ProfileRepository profileRepository;
    private LiveData<ResponseUserProfile> responseUserProfileLiveData;
    private LiveData<String> photoProfile;

    public ProfileViewModel(@NonNull Application app) {
        super(app);
        profileRepository = new ProfileRepository();
        responseUserProfileLiveData = profileRepository.getProfile();
    }

    public ProfileRepository getProfileRepository() {
        return profileRepository;
    }

    public LiveData<ResponseUserProfile> getResponseUserProfileLiveData() {
        return responseUserProfileLiveData;
    }

    public void updateProfile(RequestUserProfile requestUserProfile) {
        profileRepository.updateProfile(requestUserProfile);
    }

    public void uploadProfilePhoto(String path) {
        profileRepository.uploadProfilePhoto(path);
    }

    public LiveData<String> getPhotoProfile() {
        return profileRepository.getPhotoProfile();
    }

    public void setPhotoProfile(LiveData<String> photoProfile) {
        this.photoProfile = photoProfile;
    }
}
