package com.example.minitwitter.ui;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.minitwitter.R;
import com.example.minitwitter.TweetListFragment;
import com.example.minitwitter.common.Constants;
import com.example.minitwitter.common.SharedPreferencesManager;
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
                break;
            case R.id.navigation_tweets_like:
                fragment = TweetListFragment.newInstance(Constants.TWEET_LIST_LIKE);
                break;
            case R.id.navigation_profile:
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


        /*BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_tweets_like, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);*/

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
