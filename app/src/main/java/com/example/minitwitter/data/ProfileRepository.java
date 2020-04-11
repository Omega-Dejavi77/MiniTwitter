package com.example.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.minitwitter.common.Constants;
import com.example.minitwitter.common.MyApp;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.retrofit.AuthTwitterClient;
import com.example.minitwitter.retrofit.AuthTwitterService;
import com.example.minitwitter.retrofit.request.RequestCreateTweet;
import com.example.minitwitter.retrofit.request.RequestUserProfile;
import com.example.minitwitter.retrofit.response.Like;
import com.example.minitwitter.retrofit.response.ResponseUploadProfilePhoto;
import com.example.minitwitter.retrofit.response.ResponseUserProfile;
import com.example.minitwitter.retrofit.response.Tweet;
import com.example.minitwitter.retrofit.response.TweetDeleted;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    private AuthTwitterService authTwitterService;
    private AuthTwitterClient authTwitterClient;
    private MutableLiveData<ResponseUserProfile> userProfile;
    private MutableLiveData<String> photoProfile;

    public ProfileRepository() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        userProfile = getProfile();
        if (photoProfile == null)
            photoProfile = new MutableLiveData<>();
    }

    public MutableLiveData<String> getPhotoProfile() {
        return photoProfile;
    }

    public MutableLiveData<ResponseUserProfile> getProfile() {
        if (userProfile == null)
            userProfile = new MutableLiveData<>();

        authTwitterService.getProfile().enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if (response.isSuccessful())
                    userProfile.setValue(response.body());
                else
                    Toast.makeText(MyApp.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
        return userProfile;
    }

    public void updateProfile(RequestUserProfile requestUserProfile) {
        authTwitterService.updateProfile(requestUserProfile).enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if (response.isSuccessful())
                    userProfile.setValue(response.body());
                else
                    Toast.makeText(MyApp.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadProfilePhoto(String path) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), new File(path));
        authTwitterService.uploadProfilePhoto(requestBody).enqueue(new Callback<ResponseUploadProfilePhoto>() {
            @Override
            public void onResponse(Call<ResponseUploadProfilePhoto> call, Response<ResponseUploadProfilePhoto> response) {
                if (response.isSuccessful()) {
                    SharedPreferencesManager.writeStringValue(Constants.PREF_PHOTO_URL, response.body().getFilename());
                    photoProfile.setValue(response.body().getFilename());
                }
                else
                    Toast.makeText(MyApp.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseUploadProfilePhoto> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
