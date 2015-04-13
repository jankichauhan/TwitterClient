package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by janki on 4/2/15.
 */

@Table(name = "user")
public class User extends Model implements Parcelable {

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_screen_name")
    private String screenName;

    @Column(name = "user_id")
    private long id;

    @Column(name = "user_profile_image")
    private String profileImageUrl;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public long getUId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public static User fromJSON(JSONObject user){

        User newUser = new User();

        try {
            newUser.name = user.getString("name");
            newUser.screenName = user.getString("screen_name");
            newUser.profileImageUrl = user.getString("profile_image_url");
            newUser.id = user.getLong("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.screenName);
        dest.writeLong(this.id);
        dest.writeString(this.profileImageUrl);
    }

    public User() {
    }

    private User(Parcel in) {
        this.name = in.readString();
        this.screenName = in.readString();
        this.id = in.readLong();
        this.profileImageUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
