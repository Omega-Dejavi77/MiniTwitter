package com.example.minitwitter.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.minitwitter.retrofit.response.Tweet;

import java.util.List;

public class TweetViewModel extends AndroidViewModel {

    private TweetRepository repository;

    public TweetViewModel(@NonNull Application application) {
        super(application);
        repository = new TweetRepository();
    }

    public LiveData<List<Tweet>> getAllTweets() {
        return repository.getAllTweets();
    }
}
