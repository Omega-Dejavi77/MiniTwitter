package com.example.minitwitter.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.minitwitter.retrofit.response.Tweet;

import java.util.List;

public class TweetViewModel extends AndroidViewModel {

    private LiveData<List<Tweet>> tweets;
    private TweetRepository repository;

    public TweetViewModel(@NonNull Application application) {
        super(application);
        repository = new TweetRepository();
        tweets = repository.getAllTweets();
    }

    public LiveData<List<Tweet>> getTweets() {
        return tweets;
    }

    public LiveData<List<Tweet>> getLikedTweets() {
        return repository.getLikedTweets();
    }

    public LiveData<List<Tweet>> getNewTweets() {
        tweets = repository.getAllTweets();
        return tweets;
    }

    public LiveData<List<Tweet>> getNewLikedTweets() {
        getNewTweets();
        return getLikedTweets();
    }

    public void createTweet(String message) {
        repository.createTweet(message);
    }

    public void likeTweet(int id) {
        repository.likeTweet(id);
    }
}
