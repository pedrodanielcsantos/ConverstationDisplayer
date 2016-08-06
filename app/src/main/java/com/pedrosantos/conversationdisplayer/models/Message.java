package com.pedrosantos.conversationdisplayer.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model that maps the object Message retrieved from the API.
 */
public class Message {

    @SerializedName("id")
    private long mId;

    @SerializedName("user_id")
    private long mUserId;

    @SerializedName("posted_ts")
    private long mPostedTs;

    @SerializedName("content")
    private String mContent;

    public long getId() {
        return mId;
    }

    public long getUserId() {
        return mUserId;
    }

    public long getPostedTs() {
        return mPostedTs;
    }

    public String getContent() {
        return mContent;
    }
}
