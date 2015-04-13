package com.codepath.apps.mysimpletweets.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TweetActivity extends ActionBarActivity {

    private TextView tvUserName;
    private TextView tvScreenName;
    private TextView tvBody;
    private TextView tvTweetTime;
    private ImageView ivProfilePicture;
    private Tweet tweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        tvUserName = (TextView) findViewById(R.id.tvUsername);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvTweetTime = (TextView) findViewById(R.id.tvTweetTime);
        ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);

        tweet = getIntent().getParcelableExtra("tweet");
        tvUserName.setText(tweet.getUser().getName());
        tvScreenName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvTweetTime.setText(getFormattedCreatedAt(tweet.getCreated()));
        Picasso.with(getBaseContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfilePicture);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getFormattedCreatedAt(String createdAt) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        String formattedCreatedAt = "";
        try {
            long dateMillis = sf.parse(createdAt).getTime();
            sf = new SimpleDateFormat("M/d/yy, K:ss a", Locale.ENGLISH);
            formattedCreatedAt = sf.format(dateMillis);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedCreatedAt;
    }
}
