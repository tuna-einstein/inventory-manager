package com.usp.inventory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by umasankar on 11/16/15.
 */
public class Item implements Parcelable {

    private String id;
    private String name;
    private String description1;
    private String ownerId;
    private int units;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description1);
        dest.writeString(this.ownerId);
        dest.writeInt(this.units);
    }

    public Item() {
    }

    private Item(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.description1 = in.readString();
        this.ownerId = in.readString();
        this.units = in.readInt();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
