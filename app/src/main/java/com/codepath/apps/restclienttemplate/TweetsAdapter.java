package com.codepath.apps.restclienttemplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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

    //Define a ViewHolder to hold a tweet
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
            itemView.setOnClickListener(this);
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

        //Bind information to tweet items
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvName.setText(tweet.user.name);
            tvScreenName.setText(tweet.user.screenName);
            tvTimeStamp.setText(tweet.getTimeDiff());
            Glide.with(binding.getRoot()).load(tweet.user.profileImageURL)
                    .centerCrop()
                    .transform(new RoundedCornersTransformation(30, 10))
                    .into(ivProfileImage);
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
                    Intent intent = new Intent(context, ComposeActivity.class);
                    intent.putExtra("baseString", tweet.user.screenName);
                    intent.putExtra("id", tweet.id);
                    ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
                }
            });
        }

        @Override
        public void onClick(View v) {
            Log.d("TweetsAdapter", "Click recognized.");

            //Get position
            int position = getAdapterPosition();

            //Validate position
            if (position != RecyclerView.NO_POSITION) {
                Tweet tweet = tweets.get(position);

                //Create intent
                Intent intent = new Intent(context, TweetDetailsActivity.class);
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));

                //Show activity
                context.startActivity(intent);
            }
        }
    }
}