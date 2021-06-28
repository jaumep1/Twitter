package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    String name;
    String screenName;
    String profileImageURL;

    public static User fromJSON(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screenName");
        user.profileImageURL = jsonObject.getString("profile_image_url_https");


        return user;
    }
}
