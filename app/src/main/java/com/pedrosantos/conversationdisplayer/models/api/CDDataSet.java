package com.pedrosantos.conversationdisplayer.models.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model that maps the API response, containing a list of users and a list of messages.
 */
public class CDDataSet {

    @SerializedName("users")
    List<User> mUserList = new ArrayList<>();

    @SerializedName("messages")
    List<Message> mMessageList = new ArrayList<>();


    public List<User> getUserList() {
        return mUserList;
    }

    public List<Message> getMessageList() {
        return mMessageList;
    }
}
