package com.codepath.apps.mysimpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.TweetActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.utils.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by janki on 4/17/15.
 */
public class HomeTimeLineFragment extends TweetsListFragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // populateTimeLine();
    }

    @Override
    protected void fetchUsingApi() {

        if (tweets.size() > 0) {
            max_id = tweets.get(tweets.size() - 1).getId() - 1;
        }

        client.getHomeTimeLine(max_id, new JsonHttpResponseHandler() {

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
