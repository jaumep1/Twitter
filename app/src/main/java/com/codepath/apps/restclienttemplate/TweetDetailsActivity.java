package com.codepath.apps.restclienttemplate;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetDetailsBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class TweetDetailsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 20;
    private static final String TAG = "TweetDetailsActivity";

    ImageView ivProfileImage;
    TextView tvName;
    TextView tvScreenName;
    TextView tvTimeStamp;
    TextView tvBody;
    ToggleButton tbReply;
    ImageView ivMedia;
    ToggleButton tbFav;
    ToggleButton tbRetweet;

    Tweet tweet;

    TwitterClient client;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        ActivityTweetDetailsBinding binding =
                ActivityTweetDetailsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        client = TwitterApplication.getRestClient(this);

        ivProfileImage = binding.ivProfileImage;
        tvName = binding.tvName;
        tvScreenName = binding.tvScreenName;
        tvTimeStamp = binding.tvTimeStamp;
        tvBody = binding.tvBody;
        ivMedia = binding.ivMedia;
        tbReply = binding.tbReply;
        tbFav = binding.tbFav;
        tbRetweet = binding.tbRetweet;

        tvName.setText(tweet.user.name);
        tvScreenName.setText(tweet.user.screenName);
        tvTimeStamp.setText(tweet.getTimeDiff());
        tvBody.setText(tweet.body);

        Glide.with(binding.getRoot()).load(tweet.user.profileImageURL)
                .centerCrop()
                .transform(new RoundedCornersTransformation(30, 10))
                .into(ivProfileImage);
        if (tweet.media != null) {
            Log.d("Adapter", tweet.media + ", " + tweet.body);
            Glide.with(binding.getRoot()).load(tweet.media).into(ivMedia);
            ivMedia.setVisibility(View.VISIBLE);
        } else {
            ivMedia.setVisibility(View.GONE);
        }

        tbReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Reply clicked " + tweet.id);
                Intent intent = new Intent(TweetDetailsActivity.this, ComposeActivity.class);
                intent.putExtra("baseString", tweet.user.screenName);
                intent.putExtra("id", tweet.id);
                TweetDetailsActivity.this.startActivityForResult(intent, REQUEST_CODE);
            }
        });

        tbFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Favorite clicked " + tweet.id);
                client.favorite(tweet.id, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i("TAG", "onSuccess to publish retweet");
                        Toast.makeText(TweetDetailsActivity.this,
                                "Added to favroites!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish retweet", throwable);
                    }
                });
            }
        });

        tbRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Retweet clicked " + tweet.id);
                client.retweet(tweet.id, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i("TAG", "onSuccess to publish retweet");
                        Toast.makeText(TweetDetailsActivity.this,
                                "Retweeted!", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish retweet", throwable);
                    }
                });
            }
        });

    }
}