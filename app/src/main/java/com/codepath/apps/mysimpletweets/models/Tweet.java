package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by janki on 4/2/15.
 */

@Table(name = "tweet")
public class Tweet extends Model implements Parcelable {

    @Column(name = "tweet_body")
    private String body;

    @Column(name = "tweet_id", unique = true,  onUniqueConflict = Column.ConflictAction.REPLACE)
    private long id; // unqiue id for tweet

    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;

    @Column(name = "created_time")
    private String created;

    @Column(name = "fav_count")
    private int favCount;

    @Column(name = "retweet_count")
    private int retweetCount;

    public int getFavCount() {
        return favCount;
    }

    public void setFavCount(int favCount) {
        this.favCount = favCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public Tweet(){
        super();
    }
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getUId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    //Deserialize JSONobject to tweet object
    public static Tweet fromJSON(JSONObject jsonObject){

        Tweet newTweet = new Tweet();
        try {
            newTweet.body = jsonObject.getString("text");
            newTweet.id = jsonObject.getLong("id");
            newTweet.created = jsonObject.getString("created_at");
            newTweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            newTweet.retweetCount = jsonObject.getInt("retweet_count");
            newTweet.favCount = jsonObject.getInt("favourites_count");

            newTweet.save();

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return newTweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray){

        ArrayList<Tweet> tweets = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++){

            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);

                if(tweet != null)
                {
                    tweets.add(tweet);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

        }

        return tweets;

    }
    public void saveTweet(){
        user.save();
        this.save();
    }

    public static List<Tweet> getAll() {
        return new Select().from(Tweet.class).orderBy("tweet_id DESC").execute();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeLong(this.id);
        dest.writeParcelable(this.user, 0);
        dest.writeString(this.created);
       // dest.writeInt(this.favCount);
       // dest.writeInt(this.retweetCount);
    }


    private Tweet(Parcel in) {
        this.body = in.readString();
        this.id = in.readLong();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.created = in.readString();
        //this.favCount = in.readInt();
        //this.retweetCount = in.readInt();
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
