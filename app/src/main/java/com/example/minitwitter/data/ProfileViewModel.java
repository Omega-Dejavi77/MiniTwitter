package com.example.minitwitter.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.minitwitter.retrofit.response.ResponseUserProfile;


public class ProfileViewModel extends AndroidViewModel {

    private ProfileRepository profileRepository;
    private LiveData<ResponseUserProfile> responseUserProfileLiveData;

    public ProfileViewModel(Application app) {
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
}
