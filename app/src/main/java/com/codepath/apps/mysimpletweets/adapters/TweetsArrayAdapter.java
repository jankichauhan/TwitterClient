package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.fragments.ComposeTweetFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
/**
 * Created by janki on 4/2/15.
 */
//Taking the tweet object and turing to views
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Tweet tweet = getItem(position);

        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.ivReply = (ImageView) convertView.findViewById(R.id.ivReply);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvbody);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvScreenname = (TextView) convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvCreated = (TextView) convertView.findViewById(R.id.tvRelativeDate);
            viewHolder.tvFavCount = (TextView) convertView.findViewById(R.id.tvFavCount);
            viewHolder.tvRetweetCount = (TextView) convertView.findViewById(R.id.tvRetweetCount);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvUsername.setText(tweet.getUser().getName());
        viewHolder.tvScreenname.setText("  @" + tweet.getUser().getScreenName());
        viewHolder.tvCreated.setText(getRelativeTimeAgo(tweet.getCreated()));
        viewHolder.tvFavCount.setText(Integer.toString(tweet.getFavCount()));
        viewHolder.tvRetweetCount.setText(Integer.toString(tweet.getRetweetCount()));

        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.ivProfileImage);

        final User user = tweet.getUser();
        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("user", user);
                getContext().startActivity(i);
            }
        });

        final long uid = tweet.getUId();
        viewHolder.ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String screenName = tweet.getUser().getScreenName() + " ";
                ComposeTweetFragment composeFragment = ComposeTweetFragment.newInstance(tweet.getUser(), screenName, uid);
                FragmentActivity activity = (FragmentActivity)getContext();
                composeFragment.show(activity.getFragmentManager(), "compose_fragment");
            }
        });

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
            String[] relativeTime = relativeDate.split(" ");
            if (relativeTime.length == 3) {
                relativeDate = relativeTime[0] + " " +relativeTime[1].substring(0, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    private static class ViewHolder {
        ImageView ivProfileImage;
        ImageView ivReply;
        TextView tvUsername;
        TextView tvScreenname;
        TextView tvBody;
        TextView tvCreated;
        TextView tvFavCount;
        TextView tvRetweetCount;

    }
}
