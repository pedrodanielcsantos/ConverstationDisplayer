package com.pedrosantos.conversationdisplayer.models.app;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Model to be displayed in each line of the recycler view
 */
public class MessageListItem {

    @SerializedName("id")
    private long mMessageId;

    @SerializedName("user_id")
    private long mUserId;

    @SerializedName("content")
    private String mContent;

    @SerializedName("isFromSelf")
    private boolean mIsFromSelf;

    @SerializedName("isExpanded")
    private boolean mIsExpanded;

    @SerializedName("userAvatar")
    private String mUserAvatar;

    @SerializedName("userName")
    private String mUserName;

    @SerializedName("postedDate")
    private Date mPostedDate;

    public long getMessageId() {
        return mMessageId;
    }

    public void setMessageId(final long messageId) {
        mMessageId = messageId;
    }

    public long getUserId() {
        return mUserId;
    }

    public void setUserId(final long userId) {
        mUserId = userId;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(final String content) {
        mContent = content;
    }

    public boolean isFromSelf() {
        return mIsFromSelf;
    }

    public void setFromSelf(final boolean fromSelf) {
        mIsFromSelf = fromSelf;
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    public void setExpanded(final boolean expanded) {
        mIsExpanded = expanded;
    }

    public String getUserAvatar() {
        return mUserAvatar;
    }

    public void setUserAvatar(final String userAvatar) {
        mUserAvatar = userAvatar;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(final String userName) {
        mUserName = userName;
    }

    public Date getPostedDate() {
        return mPostedDate;
    }

    public void setPostedDate(final Date postedDate) {
        mPostedDate = postedDate;
    }
}
