package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by janki on 4/17/15.
 */
public class UserTimeFragment extends TweetsListFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public static UserTimeFragment newInstance(String screeName) {
        UserTimeFragment userFragment = new UserTimeFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screeName);
        userFragment.setArguments(args);
        return userFragment;
    }

    @Override
    protected void fetchUsingApi() {

        String screenName = getArguments().getString("screen_name");
        if (tweets.size() > 0) {
            max_id = tweets.get(tweets.size() - 1).getId() - 1;
        }

        client.getUserTimeLine(screenName, new JsonHttpResponseHandler() {

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
