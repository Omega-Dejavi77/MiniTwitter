package com.example.minitwitter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.minitwitter.common.Constants;
import com.example.minitwitter.data.TweetViewModel;

import com.example.minitwitter.retrofit.response.Tweet;

import java.util.List;



public class TweetListFragment extends Fragment {

    private int tweetListType = 0;
    private MyTweetRecyclerViewAdapter adapter;
    private List<Tweet> tweetList;
    private TweetViewModel tweetViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TweetListFragment() {
    }

    // TODO: Customize parameter initialization
    public static TweetListFragment newInstance(int tweetListType) {
        TweetListFragment fragment = new TweetListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.TWEET_LIST_TYPE, tweetListType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweetViewModel = new ViewModelProvider(this).get(TweetViewModel.class);

        if (getArguments() != null)
            tweetListType = getArguments().getInt(Constants.TWEET_LIST_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.list);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (tweetListType == Constants.TWEET_LIST_ALL) loadNewData(swipeRefreshLayout);
            else if (tweetListType == Constants.TWEET_LIST_LIKE) loadNewLikeTweetData(swipeRefreshLayout);
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorBlue));

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new MyTweetRecyclerViewAdapter(getActivity(), tweetList);
        recyclerView.setAdapter(adapter);
        if (tweetListType == Constants.TWEET_LIST_ALL)
            loadTweetData();

        else if (tweetListType == Constants.TWEET_LIST_LIKE)
            loadLikeTweetData();
        loadTweetData();
        return view;
    }

    private void loadTweetData() {
        tweetViewModel.getAllTweets().observe(getActivity(), tweets -> {
            tweetList = tweets;
            adapter.setData(tweetList);
        });
    }

    private void loadNewData(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setRefreshing(true);
        tweetViewModel.getNewTweets().observe(getActivity(), new Observer<List<Tweet>>() {
            @Override
            public void onChanged(List<Tweet> tweets) {
                tweetList = tweets;
                swipeRefreshLayout.setRefreshing(false);
                adapter.setData(tweetList);
                tweetViewModel.getNewTweets().removeObserver(this);
            }
        });
    }

    private void loadLikeTweetData() {
    }

    private void loadNewLikeTweetData(SwipeRefreshLayout swipeRefreshLayout) {
    }
}
