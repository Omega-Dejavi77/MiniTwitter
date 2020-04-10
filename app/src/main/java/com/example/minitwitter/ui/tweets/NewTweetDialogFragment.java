package com.example.minitwitter.ui.tweets;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constants;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.data.TweetViewModel;

public class NewTweetDialogFragment extends DialogFragment {

    private ImageView ivClose, ivAvatar;
    private Button btnCreateTweet;
    private EditText etMessage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.new_tweet_full_diaog, container, false);

        ivClose = view.findViewById(R.id.imageViewClose);
        ivAvatar = view.findViewById(R.id.imageViewAvatar);
        btnCreateTweet = view.findViewById(R.id.buttonCreateTweet);
        etMessage = view.findViewById(R.id.editTextTweet);

        String photoUrl = SharedPreferencesManager.readStringValue(Constants.PREF_PHOTO_URL);
        if (!photoUrl.isEmpty())
            Glide.with(getActivity()).load(Constants.PREF_PHOTO_URL + photoUrl).into(ivAvatar);


        ivClose.setOnClickListener(v -> {
            String message = etMessage.getText().toString();
            if (!message.isEmpty())
                showDialog();
            else
                getDialog().dismiss();
        });

        btnCreateTweet.setOnClickListener(v -> {
            String message = etMessage.getText().toString();
            if (message.isEmpty()) {
                Toast.makeText(getActivity(), "Please you must type something", Toast.LENGTH_SHORT).show();
            }
            else {
                TweetViewModel tweetViewModel = new ViewModelProvider(this).get(TweetViewModel.class);
                tweetViewModel.createTweet(message);
                getDialog().dismiss();
            }
        });
        return view;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setMessage("Are you sure you want cancel tweet? The message will lose")
                .setTitle("Cancel tweet");

        builder.setPositiveButton("Delete", (dialog, id) -> {
            dialog.dismiss();
            getDialog().dismiss();
        });

        builder.setNegativeButton("Cancel", ((dialog, id) -> dialog.dismiss()));
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
