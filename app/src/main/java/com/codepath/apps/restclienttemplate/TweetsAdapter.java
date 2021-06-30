package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;
    //Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    //For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet,
                parent, false);
        return new ViewHolder(view);
    }

    //Bind values based on the position of the element
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get the data at position
        Tweet tweet = tweets.get(position);
        //Bind the tweet at the ViewHolder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    //Define a ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        private final int REQUEST_CODE = 20;

        ImageView ivProfileImage;
        ImageView ivMedia;
        TextView tvScreenName;
        TextView tvBody;
        TextView tvTimeStamp;
        TextView tvName;
        ItemTweetBinding binding;
        ImageView ivReply;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemTweetBinding.bind(this.itemView);
            ivProfileImage = binding.ivProfileImage;
            tvName = binding.tvName;
            tvScreenName = binding.tvScreenName;
            tvBody = binding.tvBody;
            tvTimeStamp = binding.tvTimeStamp;
            ivMedia = binding.ivMedia;
            ivReply = binding.ivReply;
            ivMedia.setVisibility(View.INVISIBLE);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvName.setText(tweet.user.name);
            tvScreenName.setText(tweet.user.screenName);
            tvTimeStamp.setText(getTimeDiff(tweet.createdAt));
            Glide.with(context).load(tweet.user.profileImageURL).into(ivProfileImage);
            if (tweet.media != null) {
                Log.d("Adapter", tweet.media + ", " + tweet.body);
                Glide.with(context).load(tweet.media).into(ivMedia);
                ivMedia.setVisibility(View.VISIBLE);
            } else {
                ivMedia.setVisibility(View.GONE);
            }

            ivReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TweetsAdapter", "Reply clicked " + tweet.id);
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context, ComposeActivity.class);
                    intent.putExtra("baseString", tweets.get(position).user.screenName);
                    intent.putExtra("id", tweets.get(position).id);
                    ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private String getTimeDiff(String then) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE LLL dd HH:mm:ss xxxx yyyy");
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime oldDate = ZonedDateTime.parse(then, dtf);

            Duration diff = Duration.between(now, oldDate).abs();

            if (diff.getSeconds() < 60) {
                return String.format("%ds", diff.getSeconds());
            } else if (diff.toMinutes() < 60) {
                return String.format("%dm", diff.toMinutes());
            } else if (diff.toHours() < 24) {
                return String.format("%dh", diff.toHours());
            } else if (diff.toDays() < 31) {
                return String.format("%dd", diff.toDays());
            } else if ((int) (diff.toDays() / 7) < 52) {
                return String.format("%dw", (int) (diff.toDays() / 7));
            } else if ((int) (diff.toDays() / 31) < 12) {
                return String.format("%dm", (int) (diff.toDays() / 31));
            } else {
                return String.format("%dy", (int) (diff.toDays() / 365));
            }
        }
    }
}
