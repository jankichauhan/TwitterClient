package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by janki on 4/2/15.
 */
//Taking the tweet object and turing to views
public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{

    public TweetsArrayAdapter(Context context,List<Tweet> tweets) {
        super(context,0, tweets);
    }

    //Viewholder pattern
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Tweet tweet = getItem(position);

        if(convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent,false);
        }

        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvbody);
        TextView tvUser = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvRelativeDate = (TextView) convertView.findViewById(R.id.tvRelativeDate);

        tvBody.setText(tweet.getBody());
        tvUser.setText(tweet.getUser().getName() + ": @" + tweet.getUser().getScreenName());

        String[] relativeTime = getRelativeTimeAgo(tweet.getCreated()).split(" ");
        String time = relativeTime[0].concat(String.valueOf(relativeTime[1].charAt(0)));
        tvRelativeDate.setText(time);

        ivImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivImage);
        return convertView;
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
