package com.example.minitwitter.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.minitwitter.R;
import com.example.minitwitter.data.ProfileViewModel;
import com.example.minitwitter.ui.profile.ProfileFragment;
import com.example.minitwitter.ui.tweets.TweetListFragment;
import com.example.minitwitter.common.Constants;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.ui.tweets.NewTweetDialogFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


public class DashboardActivity extends AppCompatActivity implements PermissionListener {

    private FloatingActionButton fab;
    private ImageView ivAvatar;
    private ProfileViewModel profileViewModel;

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
                fragment = new ProfileFragment();
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

        MobileAds.initialize(this, initializationStatus -> {
        });
        AdView banner = findViewById(R.id.adViewBanner);
        banner.loadAd(new AdRequest.Builder().build());

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

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
        if (!photoUrl.isEmpty()) {
            Glide.with(this)
                    .load(Constants.PREF_PHOTO_URL + photoUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .into(ivAvatar);
        }

        profileViewModel.getPhotoProfile().observe(this, photoString -> {
            Glide.with(this)
                    .load(Constants.PREF_PHOTO_URL + photoString)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .into(ivAvatar);
        });
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
        Intent selectPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(selectPhoto, Constants.SELECT_PHOTO_GALLERY);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
        Toast.makeText(this, "It can not select the photo", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_CANCELED) return;

        if (requestCode == Constants.SELECT_PHOTO_GALLERY) {
            if (data != null) {
                Uri photo = data.getData(); // value://gallery//photos//...
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(photo, filePathColumn, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(filePathColumn[0]);
                    String photoPath = cursor.getString(idx);
                    profileViewModel.uploadProfilePhoto(photoPath);
                    cursor.close();
                }
            }
        }
    }
}
