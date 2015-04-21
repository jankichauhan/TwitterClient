package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by janki on 4/17/15.
 */
public class MentionTimeFragment extends TweetsListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void fetchUsingApi() {

        if (tweets.size() > 0) {
            max_id = tweets.get(tweets.size() - 1).getId() - 1;
        }

        client.getMentionTimeLine(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                Log.d("DEBUG", response.toString());
                addAll(Tweet.fromJSONArray(response));
                Log.d("DEBUG", aTweets.toString());
                for (Tweet tweet : tweets) {
                    tweet.saveTweet();
                }
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                Log.d("DEBUG", error.toString());
            }
        });
    }

}
