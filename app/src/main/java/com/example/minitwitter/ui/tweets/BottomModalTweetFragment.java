package com.example.minitwitter.ui.tweets;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.minitwitter.R;
import com.example.minitwitter.common.Constants;
import com.example.minitwitter.data.TweetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

public class BottomModalTweetFragment extends BottomSheetDialogFragment {

    private TweetViewModel tweetViewModel;
    private int idTweetToDelete;

    public static BottomModalTweetFragment newInstance(int id) {
         BottomModalTweetFragment bottomModalTweetFragment = new BottomModalTweetFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_TWEET_ID, id);
        bottomModalTweetFragment.setArguments(args);
        return bottomModalTweetFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            idTweetToDelete = getArguments().getInt(Constants.ARG_TWEET_ID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_modal_tweet_fragment, container, false);
        final NavigationView navigationView = view.findViewById(R.id.navigation_view_bottom_tweet);
        navigationView.setNavigationItemSelectedListener( menuItem -> {
            if (menuItem.getItemId() == R.id.action_delete_tweet) {
                tweetViewModel.deleteTweet(idTweetToDelete);
                getDialog().dismiss();
                return true;
            }
            return false;
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tweetViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);
    }

}
