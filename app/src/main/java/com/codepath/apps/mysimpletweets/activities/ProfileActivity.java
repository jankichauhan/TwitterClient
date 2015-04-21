package com.codepath.apps.mysimpletweets.activities;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.UserTimeFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends ActionBarActivity {

    private ImageView ivProfilePicture;
    private TextView tvUsername;
    private TextView tvTag;
    private TextView tvFollower;
    private TextView tvFollowing;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = getIntent().getParcelableExtra("user");
        String screenName = user.getScreenName();

        if(savedInstanceState == null) {
            UserTimeFragment userTimeLine = UserTimeFragment.newInstance(screenName);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimeLine);

            ft.commit();
        }
        setupView();
    }

    private void setupView(){

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0x44099FFF));

        getSupportActionBar().setTitle(user.getScreenName());

        ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvTag = (TextView) findViewById(R.id.tvTag);
        tvFollower = (TextView) findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        tvUsername.setText(user.getName());
        tvTag.setText(" @" + user.getScreenName());
        tvFollower.setText(Long.toString(user.getFollowers()) + " followers");
        tvFollowing.setText(Long.toString(user.getFollowing()) + " following");

        Picasso.with(getBaseContext()).load(user.getProfileImageUrl()).into(ivProfilePicture);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
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
}
