package com.pedrosantos.conversationdisplayer.models.app;

import com.google.gson.annotations.SerializedName;

import android.text.SpannableString;

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
    private SpannableString mContent;

    @SerializedName("isFromSelf")
    private boolean mIsFromSelf;

    @SerializedName("matchesSearch")
    private boolean mMatchesSearch;

    @SerializedName("userAvatar")
    private String mUserAvatar;

    @SerializedName("userName")
    private SpannableString mUserName;

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
        return mContent.toString();
    }

    public void setContent(final SpannableString content) {
        mContent = content;
    }

    public SpannableString getContentSpannableString() {
        return mContent;
    }

    public boolean isFromSelf() {
        return mIsFromSelf;
    }

    public void setFromSelf(final boolean fromSelf) {
        mIsFromSelf = fromSelf;
    }

    public boolean isMatchesSearch() {
        return mMatchesSearch;
    }

    public void setMatchesSearch(final boolean matchesSearch) {
        this.mMatchesSearch = matchesSearch;
    }

    public String getUserAvatar() {
        return mUserAvatar;
    }

    public void setUserAvatar(final String userAvatar) {
        mUserAvatar = userAvatar;
    }

    public String getUserName() {
        return mUserName.toString();
    }

    public void setUserName(final SpannableString userName) {
        mUserName = userName;
    }

    public SpannableString getUserNameSpannableString() {
        return mUserName;
    }

    public Date getPostedDate() {
        return mPostedDate;
    }

    public void setPostedDate(final Date postedDate) {
        mPostedDate = postedDate;
    }
}
