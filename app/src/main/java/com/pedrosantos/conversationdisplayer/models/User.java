package com.pedrosantos.conversationdisplayer.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model that maps the object User retrieved from the API.
 */
public class User {
    @SerializedName("id")
    private long mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("avatar_url")
    private String mAvatarUrl;

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }
}
