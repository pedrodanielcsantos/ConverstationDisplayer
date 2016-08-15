package com.pedrosantos.conversationdisplayer.models.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Model that maps the API response, containing a list of users and a list of messages.
 */
public class CDDataSet extends RealmObject {

    @SerializedName("users")
    RealmList<User> mUserList = new RealmList<>();

    @SerializedName("messages")
    RealmList<Message> mMessageList = new RealmList<>();


    public List<User> getUserList() {
        return mUserList;
    }

    public List<Message> getMessageList() {
        return mMessageList;
    }
}
