package com.codepath.apps.mysimpletweets.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApp;
import com.codepath.apps.mysimpletweets.TwitterCl;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.fragments.ComposeTweetFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.utils.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity  implements ComposeTweetFragment.ComposeFragmentListener{

    private TwitterCl client;
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    private long max_id;
    private User homeUser;
    SharedPreferences mSettings;

    private static final String TAG = TimelineActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        max_id = 0;
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);
        client = TwitterApp.getRestClient();
        setupView();

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                populateTimeLine();
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

        populateTimeLine();
    }

    private void setupView() {
       // getSupportActionBar().setTitle("");

        TwitterApp.getRestClient().getVerifyCredentials(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                homeUser = User.fromJSON(response);
           //     getSupportActionBar().setTitle(homeUser.getScreenName());
               // SharedPreferences.Editor editor = mSettings.edit();
               // editor.putLong("user_id", homeUser.getId());
                //editor.commit();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,Throwable throwable, JSONObject error) {
                Log.d(TAG, throwable.toString());
                Log.d(TAG, error.toString());
            }
        });


    }

    private void populateTimeLine() {

        if (tweets.size() > 0) {
            max_id = tweets.get(tweets.size() - 1).getId() - 1;
        }

        client.getHomeTimeLine(max_id, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                Log.d("DEBUG", response.toString());
                aTweets.addAll(Tweet.fromJSONArray(response));
                Log.d("DEBUG", aTweets.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                Log.d("DEBUG", error.toString());
            }
        });
    }

    private void composeTweet(){
        ComposeTweetFragment compose = ComposeTweetFragment.newInstance(homeUser);
        compose.show(getFragmentManager(), "compose_tweet_fragment");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_compose:
                composeTweet();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onPostTweet(boolean success, Tweet tweet) {
        if(success) {
            aTweets.insert(tweet, 0);
        }
    }
}
