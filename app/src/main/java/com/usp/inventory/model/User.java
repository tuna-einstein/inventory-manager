package com.usp.inventory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by umasankar on 11/14/15.
 */
public class User implements Parcelable {
    private String displayName;

    private String email;

    private String uid;

    private String provider;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.displayName);
        dest.writeString(this.email);
        dest.writeString(this.uid);
        dest.writeString(this.provider);
    }

    public User() {
    }

    private User(Parcel in) {
        this.displayName = in.readString();
        this.email = in.readString();
        this.uid = in.readString();
        this.provider = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
