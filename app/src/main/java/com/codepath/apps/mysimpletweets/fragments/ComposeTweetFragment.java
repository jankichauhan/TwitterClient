package com.codepath.apps.mysimpletweets.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApp;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by janki on 4/10/15.
 */
public class ComposeTweetFragment extends DialogFragment {

    private ComposeFragmentListener listener;
    private User user;
    private ImageView ivProfilePicture;
    private TextView tvUsername;
    private TextView tvScreenname;
    private TextView tvCharacterLeft;
    private Button btnTweet;
    private EditText etTweet;

    public ComposeTweetFragment(){

    }

    public static ComposeTweetFragment newInstance(User user){

        ComposeTweetFragment compose = new ComposeTweetFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        compose.setArguments(args);

        return compose;
    }

    public interface ComposeFragmentListener {
        public void onPostTweet(boolean success, Tweet tweet);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getArguments().getParcelable("user");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.compose_tweet_layout, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {

        listener = (ComposeFragmentListener) getActivity();
        ivProfilePicture = (ImageView) view.findViewById(R.id.ivProfilePicture);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvScreenname = (TextView) view.findViewById(R.id.tvScreenName);
        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        etTweet = (EditText) view.findViewById(R.id.etTweet);
        tvCharacterLeft = (TextView) view.findViewById(R.id.tvCharacterLeft);

        Picasso.with(getActivity().getApplicationContext()).load(user.getProfileImageUrl()).into(ivProfilePicture);
        tvUsername.setText(user.getName());
        tvScreenname.setText("@"+user.getScreenName());

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postTweet();
            }
        });

        tvCharacterLeft.setText(Integer.toString(140));
        etTweet.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int chars_left = 140 - s.length();
                if (chars_left < 0) {
                    tvCharacterLeft.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    tvCharacterLeft.setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
                tvCharacterLeft.setText(Integer.toString(chars_left));
            }
        });
    }

    private void postTweet() {

        String status = etTweet.getText().toString();

        TwitterApp.getRestClient().postUpdate(status, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet tweet = Tweet.fromJSON(response);
                listener.onPostTweet(true, tweet);
                getDialog().dismiss();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                Toast.makeText(getActivity(), "Sorry. Couldn't post. Please try again.", Toast.LENGTH_SHORT).show();
                Log.d("DEBUG", throwable.toString());
                Log.d("DEBUG", error.toString());
            }
        });


    }

}
