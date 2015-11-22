package com.usp.inventory.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

import com.firebase.client.ServerValue;

/**
 * Created by umasankar on 11/16/15.
 */
public class ItemRequest implements Parcelable {

    private String id;

    private String requesterId;

    private String itemId;

    private String itemOwnerId;

    private long timeStamp = System.currentTimeMillis();


    // 0: Pending, 1: Accepted 2 : Rejected
    private int status = 0;

    private int units;

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemOwnerId() {
        return itemOwnerId;
    }

    public void setItemOwnerId(String itemOwnerId) {
        this.itemOwnerId = itemOwnerId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ItemRequest() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.requesterId);
        dest.writeString(this.itemId);
        dest.writeString(this.itemOwnerId);
        dest.writeLong(this.timeStamp);
        dest.writeInt(this.status);
        dest.writeInt(this.units);
    }

    private ItemRequest(Parcel in) {
        this.id = in.readString();
        this.requesterId = in.readString();
        this.itemId = in.readString();
        this.itemOwnerId = in.readString();
        this.timeStamp = in.readLong();
        this.status = in.readInt();
        this.units = in.readInt();
    }

    public static final Creator<ItemRequest> CREATOR = new Creator<ItemRequest>() {
        public ItemRequest createFromParcel(Parcel source) {
            return new ItemRequest(source);
        }

        public ItemRequest[] newArray(int size) {
            return new ItemRequest[size];
        }
    };
}
