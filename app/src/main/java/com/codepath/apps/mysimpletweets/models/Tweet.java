package com.codepath.apps.mysimpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by janki on 4/2/15.
 */

/*

 */
public class Tweet {

    private String body;
    private long id; // unqiue id for tweet
    private User user;
    private String created;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //Deserialize JSONobject to tweet object
    public static Tweet fromJSON(JSONObject jsonObject){

        Tweet newTweet = new Tweet();
        try {
            newTweet.body = jsonObject.getString("text");
            newTweet.id = jsonObject.getLong("id");
            newTweet.created = jsonObject.getString("created_at");
            newTweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return newTweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray){

        ArrayList<Tweet> tweets = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++){

            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);

                if(tweet != null)
                {
                    tweets.add(tweet);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

        }

        return tweets;

    }
}
