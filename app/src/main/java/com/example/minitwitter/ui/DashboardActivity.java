package com.example.minitwitter.ui;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.minitwitter.R;
import com.example.minitwitter.ui.tweets.TweetListFragment;
import com.example.minitwitter.common.Constants;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.ui.tweets.NewTweetDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class DashboardActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private ImageView ivAvatar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = TweetListFragment.newInstance(Constants.TWEET_LIST_ALL);
                fab.show();
                break;
            case R.id.navigation_tweets_like:
                fragment = TweetListFragment.newInstance(Constants.TWEET_LIST_LIKE);
                fab.hide();
                break;
            case R.id.navigation_profile:
                fab.hide();
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        fab = findViewById(R.id.fab);
        ivAvatar = findViewById(R.id.imageViewToolBarAvatar);

        getSupportActionBar().hide();

        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, TweetListFragment.newInstance(Constants.TWEET_LIST_ALL))
                .commit();

        fab.setOnClickListener(v -> {
            NewTweetDialogFragment dialogFragment = new NewTweetDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "NewTweetDialogFragment");
        });

        String photoUrl = SharedPreferencesManager.readStringValue(Constants.PREF_PHOTO_URL);
        if (!photoUrl.isEmpty())
            Glide.with(this).load(Constants.PREF_PHOTO_URL + photoUrl).into(ivAvatar);

    }

}
