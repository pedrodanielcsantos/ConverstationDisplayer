package com.pedrosantos.conversationdisplayer.models.api;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Model that maps the object User retrieved from the API.
 */
public class User extends RealmObject {
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
