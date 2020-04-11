package com.example.minitwitter.ui.tweets;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constants;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.data.TweetViewModel;
import com.example.minitwitter.retrofit.response.Like;
import com.example.minitwitter.retrofit.response.Tweet;

import java.util.List;


public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private List<Tweet> mValues;
    private Context context;
    private TweetViewModel tweetViewModel;

    public MyTweetRecyclerViewAdapter(Context context, List<Tweet> items) {
        mValues = items;
        this.context = context;
        tweetViewModel = new ViewModelProvider((FragmentActivity)context).get(TweetViewModel.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tweet_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mValues == null) return;

        String currentUser = SharedPreferencesManager.readStringValue(Constants.PREF_USERNAME);

        holder.mItem = mValues.get(position);
        holder.tvUsername.setText(String.format("@%s", holder.mItem.getUser().getUsername()));
        holder.tvMessage.setText(holder.mItem.getMessage());
        holder.tvLikesCount.setText(String.valueOf(holder.mItem.getLikes().size()));

        String photo = holder.mItem.getUser().getPhotoUrl();
        if (!photo.equals("")) {
            Glide.with(context)
                    .load(Constants.API_FILES_URL + photo)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .into(holder.ivAvatar);
        }

        //Reset the like icons to fix rapid scroll view
        Glide.with(context)
                .load(R.drawable.ic_like)
                .into(holder.ivLike);
        holder.tvLikesCount.setTextColor(context.getResources().getColor(android.R.color.black));
        holder.tvLikesCount.setTypeface(null, Typeface.NORMAL);

        holder.ivLike.setOnClickListener(v -> tweetViewModel.likeTweet(holder.mItem.getId()));

        holder.ivShowMenu.setVisibility(View.GONE);
        if (holder.mItem.getUser().getUsername().equals(currentUser))
            holder.ivShowMenu.setVisibility(View.VISIBLE);
        holder.ivShowMenu.setOnClickListener(v -> tweetViewModel.openDialogTweetMenu(context, holder.mItem.getId()));


        for (Like like : holder.mItem.getLikes()) {
            if (like.getUsername().equals(currentUser)) {
                Glide.with(context)
                        .load(R.drawable.ic_like_pink)
                        .into(holder.ivLike);
                holder.tvLikesCount.setTextColor(context.getResources().getColor(R.color.colorPink));
                holder.tvLikesCount.setTypeface(null, Typeface.BOLD);
                break;
            }
        }
    }

    public void setData(List<Tweet> tweets) {
        mValues = tweets;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView ivAvatar;
        public final ImageView ivLike, ivShowMenu;
        public final TextView tvUsername;
        public final TextView tvMessage;
        public final TextView tvLikesCount;
        public Tweet mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivAvatar = view.findViewById(R.id.imageViewAvatar);
            ivLike = view.findViewById(R.id.imageViewLike);
            ivShowMenu = view.findViewById(R.id.imageViewShowMenu);
            tvUsername = view.findViewById(R.id.textViewUsername);
            tvMessage = view.findViewById(R.id.textViewMessage);
            tvLikesCount = view.findViewById(R.id.textViewLikes);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvUsername.getText() + "'";
        }
    }
}
