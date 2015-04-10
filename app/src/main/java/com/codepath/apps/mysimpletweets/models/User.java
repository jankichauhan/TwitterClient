package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by janki on 4/2/15.
 */
public class User {

    private String name;
    private String screenName;
    private long id;
    private String profileImageUrl;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public static User fromJSON(JSONObject user){

        User newUser = new User();

        try {
            newUser.name = user.getString("name");
            newUser.screenName = user.getString("screen_name");
            newUser.profileImageUrl = user.getString("profile_image_url");
            newUser.id = user.getLong("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newUser;
    }
}
