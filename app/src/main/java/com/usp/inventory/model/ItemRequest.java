package com.usp.inventory.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by umasankar on 11/16/15.
 */
public class ItemRequest implements Parcelable {

    private String id;

    private String requesterId;

    private String itemId;

    private String itemOwnerId;


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
        dest.writeInt(this.status);
        dest.writeInt(this.units);
    }

    public ItemRequest() {
    }

    private ItemRequest(Parcel in) {
        this.id = in.readString();
        this.requesterId = in.readString();
        this.itemId = in.readString();
        this.itemOwnerId = in.readString();
        this.status = in.readInt();
        this.units = in.readInt();
    }

    public static final Parcelable.Creator<ItemRequest> CREATOR = new Parcelable.Creator<ItemRequest>() {
        public ItemRequest createFromParcel(Parcel source) {
            return new ItemRequest(source);
        }

        public ItemRequest[] newArray(int size) {
            return new ItemRequest[size];
        }
    };
}
