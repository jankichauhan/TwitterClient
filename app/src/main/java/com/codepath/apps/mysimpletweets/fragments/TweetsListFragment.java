package com.codepath.apps.mysimpletweets.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApp;
import com.codepath.apps.mysimpletweets.TwitterCl;
import com.codepath.apps.mysimpletweets.activities.TimelineActivity;
import com.codepath.apps.mysimpletweets.activities.TweetActivity;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.utils.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by janki on 4/17/15.
 */
public abstract class TweetsListFragment extends Fragment {

    protected TweetsArrayAdapter aTweets;
    protected ArrayList<Tweet> tweets;
    protected ListView lvTweets;
    protected TwitterCl client;
    protected long max_id;
    protected SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
        max_id = 0;
        client = TwitterApp.getRestClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,   @Nullable ViewGroup container,   @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list,container, false);

        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!isNetworkAvailable()) {
                    return;
                }
                populateTimeLine();
            }
        });

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer_tweet);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                aTweets.clear();
                populateTimeLine();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getActivity(), TweetActivity.class);
                Tweet tweet = tweets.get(position);
                i.putExtra("tweet", tweet);
                startActivity(i);
            }
        });

        setupView();

        return v;
    }

    private void setupView() {

        populateTimeLine();

    }

    private void populateTimeLine() {

        if (isNetworkAvailable()) {

            fetchUsingApi();
        } else {

            fetchUsingDB();
        }


    }
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

    private void fetchUsingDB() {

        aTweets.clear();
        aTweets.addAll(Tweet.getAll());

    }

    public void addAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
    }

    public void insertAt(Tweet tweet, int position){
        aTweets.insert(tweet,position);
    }

    protected Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
