package com.codepath.apps.mysimpletweets.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApp;
import com.codepath.apps.mysimpletweets.TwitterCl;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.fragments.ComposeTweetFragment;
import com.codepath.apps.mysimpletweets.fragments.HomeTimeLineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionTimeFragment;
import com.codepath.apps.mysimpletweets.fragments.SearchTweetFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.utils.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity implements ComposeTweetFragment.ComposeFragmentListener {

    private User homeUser;
    private HomeTimeLineFragment homeTimeLineFragment;


    private static final String TAG = TimelineActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetPageAdapater(getSupportFragmentManager()));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff4099ff));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        setupView();


    }

    private void setupView() {

        if (isNetworkAvailable()) {
            TwitterApp.getRestClient().getVerifyCredentials(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    homeUser = User.fromJSON(response);
                         getSupportActionBar().setTitle("   " + homeUser.getScreenName());

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                    Log.d(TAG, throwable.toString());
                    Log.d(TAG, error.toString());
                }
            });
        }
        else{
            Toast.makeText(this, " No network connectivity!", Toast.LENGTH_SHORT).show();
        }
    }

    private void composeTweet() {

        if(isNetworkAvailable()){

            ComposeTweetFragment compose = ComposeTweetFragment.newInstance(homeUser,"", 0);
            compose.show(getFragmentManager(), "compose_tweet_fragment");
        }
        else {
            Toast.makeText(this, " No network connectivity!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String inputQuery) {
                // perform query here
                String query = inputQuery;

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                searchTweet(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_compose:
                composeTweet();
                return true;
            case R.id.action_profile:
                onProfileClick();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void searchTweet(String query) {

        if(isNetworkAvailable()){

            Intent i = new Intent(this, SearchActivity.class);
            i.putExtra("query", query);
            startActivity(i);
        }
        else {
            Toast.makeText(this, " No network connectivity!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPostTweet(boolean success, Tweet tweet) {
        if (success) {
            homeTimeLineFragment.insertAt(tweet, 0);
        }
    }

    public void onProfileClick(){
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("user", homeUser);
        startActivity(i);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public class TweetPageAdapater extends FragmentPagerAdapter{

        private String tabTitle[] ={"Home", "Mention"};

        public TweetPageAdapater(FragmentManager fa){
            super(fa);
        }

        @Override
        public Fragment getItem(int position) {

            if(position == 0){
                return new HomeTimeLineFragment();
            }
            else if(position == 1){
                return new MentionTimeFragment();
            }
            else{
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
           return tabTitle[position];
        }

        @Override
        public int getCount() {
            return tabTitle.length;
        }
    }
}
