package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by janki on 4/22/15.
 */
public class SearchTweetFragment extends TweetsListFragment {
    String mQuery;

    public static SearchTweetFragment newInstance(String query){
        SearchTweetFragment searchFragment= new SearchTweetFragment();
        Bundle args = new Bundle();
        args.putString("query",query);
        searchFragment.setArguments(args);
        return searchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuery = getArguments().getString("query");
    }

    @Override
    protected void fetchUsingApi() {
        if (tweets.size() > 0){
            max_id = tweets.get(tweets.size() - 1).getId() - 1 ;
        }

        client.getSearchTweets(mQuery, max_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (!response.isNull("statuses")){
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray("statuses");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    addAll(Tweet.fromJSONArray(jsonArray));
                    for (Tweet tweet : tweets) {
                        tweet.saveTweet();
                    }
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
