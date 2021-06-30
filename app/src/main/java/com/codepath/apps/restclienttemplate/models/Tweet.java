package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel // annotation indicates class is Parcelable
public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public String media;

    public Tweet() {}

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        Log.d("Tweet", jsonObject.toString());

        try {
            tweet.media = jsonObject.getJSONObject("entities").getJSONArray("media")
                    .getJSONObject(0).getString("media_url");
            Log.d("Tweet", jsonObject.getJSONObject("entities").getJSONArray("media")
                    .getJSONObject(0).toString());
            //Update link from http:// to https://
            if (tweet.media.substring(0,7).equals("http://")) {
                tweet.media = tweet.media.substring(0, 4) + "s" + tweet.media.substring(4);
            }
        } catch (Exception e) {
            //Log.e("Tweet", "No media found");
            tweet.media = null;
        }

        return tweet;
    }

    public static List<Tweet> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets  = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJSON(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
}
