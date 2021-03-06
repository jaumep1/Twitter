package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel // annotation indicates class is Parcelable
public class User {

    public String name;
    public String screenName;
    public String profileImageURL;

    public User() {}

    public static User fromJSON(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = String.format("@%s", jsonObject.getString("screen_name"));
        user.profileImageURL = jsonObject.getString("profile_image_url_https");

        return user;
    }
}