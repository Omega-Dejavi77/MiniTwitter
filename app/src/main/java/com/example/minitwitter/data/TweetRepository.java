package com.example.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.minitwitter.MyTweetRecyclerViewAdapter;
import com.example.minitwitter.common.MyApp;
import com.example.minitwitter.retrofit.AuthTwitterClient;
import com.example.minitwitter.retrofit.AuthTwitterService;
import com.example.minitwitter.retrofit.request.RequestCreateTweet;
import com.example.minitwitter.retrofit.response.Tweet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    private AuthTwitterService authTwitterService;
    private AuthTwitterClient authTwitterClient;
    private MutableLiveData<List<Tweet>> allTweets;

    public TweetRepository() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        allTweets = getAllTweets();
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
        Call<Tweet> call = authTwitterService.createTweet(createTweet);
        call.enqueue(new Callback<Tweet>() {
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
                    }
                    catch (NullPointerException npe) {

                    }
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
        Call<Tweet> call = authTwitterService.likeTweet(id);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                List<Tweet> clone = new ArrayList<>();
                for (Tweet tweet : allTweets.getValue()) {
                    if (tweet.getId() == id)
                        clone.add(response.body());
                    else
                        clone.add(new Tweet(tweet));
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {

            }
        });
    }
}
