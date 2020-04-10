package com.example.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.minitwitter.MyTweetRecyclerViewAdapter;
import com.example.minitwitter.common.Constants;
import com.example.minitwitter.common.MyApp;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.retrofit.AuthTwitterClient;
import com.example.minitwitter.retrofit.AuthTwitterService;
import com.example.minitwitter.retrofit.request.RequestCreateTweet;
import com.example.minitwitter.retrofit.response.Like;
import com.example.minitwitter.retrofit.response.Tweet;
import com.example.minitwitter.retrofit.response.TweetDeleted;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    private AuthTwitterService authTwitterService;
    private AuthTwitterClient authTwitterClient;
    private MutableLiveData<List<Tweet>> allTweets;
    private MutableLiveData<List<Tweet>> likedTweets;
    private String username;

    public TweetRepository() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        allTweets = getAllTweets();
        username = SharedPreferencesManager.readStringValue(Constants.PREF_USERNAME);
    }

    public MutableLiveData<List<Tweet>> getAllTweets() {
        if (allTweets == null)
            allTweets = new MutableLiveData<>();

        Call<List<Tweet>> call = authTwitterService.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if (response.isSuccessful())
                    allTweets.setValue(response.body());
                else
                    Toast.makeText(MyApp.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
        return allTweets;
    }

    public void createTweet(String message) {
        RequestCreateTweet createTweet = new RequestCreateTweet(message);
        authTwitterService.createTweet(createTweet).enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if (response.isSuccessful()) {
                    //set all tweets by cloning the list and set the clone, then set the original with the values og the cloned list
                    List<Tweet> clone = new ArrayList<>();
                    clone.add(response.body()); //first we add the new tweet from server

                    try {
                        for ( Tweet tweet : allTweets.getValue()) {
                            clone.add(new Tweet(tweet));
                        }
                        allTweets.setValue(clone);
                    } catch (NullPointerException npe) { }
                }
                else
                    Toast.makeText(MyApp.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void likeTweet(int id) {
        authTwitterService.likeTweet(id).enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if (response.isSuccessful()) {
                    List<Tweet> clone = new ArrayList<>();
                    for (Tweet tweet : allTweets.getValue()) {
                        if (tweet.getId() == id)
                            clone.add(response.body());
                        else
                            clone.add(new Tweet(tweet));
                    }
                    allTweets.setValue(clone);
                    getLikedTweets();
                }
                else
                    Toast.makeText(MyApp.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public MutableLiveData<List<Tweet>> getLikedTweets() {
        if (likedTweets == null)
            likedTweets = new MutableLiveData<>();

        List<Tweet> newLikedTweets = new ArrayList<>();
        try {
            for(Tweet tweet : allTweets.getValue()) {
                boolean found = false;
                int i = 0;
                while (!found && i < tweet.getLikes().size()) {
                    Like like = tweet.getLikes().get(i);
                    if (like.getUsername().equals(username)) {
                        newLikedTweets.add(tweet);
                        found = true;
                    }
                    i++;
                }
            }
        } catch (NullPointerException ignore) {}
        likedTweets.setValue(newLikedTweets);
        return likedTweets;
    }

    public void deleteTweet(int id) {
        authTwitterService.deleteTweet(id).enqueue(new Callback<TweetDeleted>() {
            @Override
            public void onResponse(Call<TweetDeleted> call, Response<TweetDeleted> response) {
                if (response.isSuccessful()) {
                    ArrayList<Tweet> clone = new ArrayList<>();
                    try {
                        for (Tweet tweet : allTweets.getValue()) {
                            if (tweet.getId() != id)
                                clone.add(new Tweet(tweet));
                        }
                        allTweets.setValue(clone);
                        getLikedTweets();
                    } catch (NullPointerException ignored) {}
                }
                else
                    Toast.makeText(MyApp.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<TweetDeleted> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
