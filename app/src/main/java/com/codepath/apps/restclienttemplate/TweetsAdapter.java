package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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




    //Define a ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvScreenName;
        TextView tvBody;
        TextView tvTimeStamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            tvTimeStamp.setText(getTimeDiff(tweet.createdAt));
            Glide.with(context).load(tweet.user.profileImageURL).into(ivProfileImage);
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
