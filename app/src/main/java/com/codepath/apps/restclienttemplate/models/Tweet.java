package com.codepath.apps.restclienttemplate.models;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Parcel // annotation indicates class is Parcelable
public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public String media;
    public long id;

    public Tweet() {}

    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        //Log.d("CHIRP", jsonObject.toString());

        if(jsonObject.has("full_text")) {
            tweet.body = jsonObject.getString("full_text");
        } else {
            tweet.body = jsonObject.getString("text");
        }
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.id = Long.parseLong(jsonObject.getString("id_str"));

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTimeDiff() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE LLL dd HH:mm:ss xxxx yyyy");
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime oldDate = ZonedDateTime.parse(createdAt, dtf);

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
