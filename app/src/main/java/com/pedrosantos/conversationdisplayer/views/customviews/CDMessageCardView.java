package com.pedrosantos.conversationdisplayer.views.customviews;

import com.bumptech.glide.Glide;
import com.pedrosantos.conversationdisplayer.R;

import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Custom view to visually represent and handle a message item.
 */
public class CDMessageCardView extends LinearLayout {

    private static final int AVATAR_WIDTH = 80;
    private TextView mContentText;
    private ImageView mAvatarImageView;
    private TextView mAuthorName;
    private LinearLayout mAuthorInfoContainer;
    private LinearLayout mOuterContainer;
    private TextView mDate;

    public CDMessageCardView(final Context context) {
        super(context);
        init(context);
    }

    public CDMessageCardView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CDMessageCardView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Sets the message content view.
     */
    public void setContentText(final SpannableString contentText) {
        mContentText.setText(contentText);
    }

    /**
     * Sets the author's name label.
     */
    public void setAuthorName(final SpannableString authorName) {
        mAuthorName.setText(authorName);
    }

    /**
     * Sets the avatar image for this view
     *
     * @param imageUrl url of the user's avatar.
     */
    public void setAvatarImage(final String imageUrl) {
        //Load with glide
        Glide.with(getContext())
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.image_placeholder)
                .crossFade()
                .animate(android.R.anim.fade_in)
                .into(mAvatarImageView);
    }

    /**
     * Sets the Date label with the value passed as parameter.
     */
    public void setDate(final SpannableString date) {
        mDate.setText(date);
    }

    /**
     * Adjusts the UI based on the message being from self or not (aligns everything to the right
     * or
     * to the left, respectively, by adjusting views' layout parameters).
     */
    public void setFromSelf(final boolean isFromSelf) {

        //Layout parameters for the whole view
        LinearLayout.LayoutParams globalLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //Message content
        RelativeLayout.LayoutParams contentLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //Author Container - avatar and image - layout parameters.
        RelativeLayout.LayoutParams authorInfoContainerLayoutParams = new RelativeLayout.LayoutParams(dpToPx(AVATAR_WIDTH), ViewGroup.LayoutParams.WRAP_CONTENT);

        //Date layout params
        RelativeLayout.LayoutParams dateLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        if (isFromSelf) {
            mDate.setGravity(Gravity.LEFT | Gravity.BOTTOM);

            dateLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            globalLayoutParams.gravity = Gravity.RIGHT;
            mOuterContainer.setGravity(Gravity.RIGHT);
            mContentText.setTextAlignment(TEXT_ALIGNMENT_VIEW_END);
            authorInfoContainerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            contentLayoutParams.addRule(RelativeLayout.LEFT_OF, R.id.cd_message_card_author_info_container);
        } else {
            mDate.setGravity(Gravity.RIGHT | Gravity.BOTTOM);

            dateLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            globalLayoutParams.gravity = Gravity.LEFT;
            mOuterContainer.setGravity(Gravity.LEFT);
            mContentText.setTextAlignment(TEXT_ALIGNMENT_VIEW_START);
            authorInfoContainerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            contentLayoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.cd_message_card_author_info_container);
        }

        dateLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        dateLayoutParams.addRule(RelativeLayout.BELOW, R.id.cd_message_card_content);

        //General rule, independent of being from self or not
        contentLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

        setLayoutParams(globalLayoutParams);
        mContentText.setLayoutParams(contentLayoutParams);
        mAuthorInfoContainer.setLayoutParams(authorInfoContainerLayoutParams);
        mDate.setLayoutParams(dateLayoutParams);

    }

    /**
     * Auxiliary method to convert dp to pixels, based on the screen density.
     */
    private int dpToPx(final int dp) {
        final DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_cd_message_card, this, true);

        mContentText = (TextView) findViewById(R.id.cd_message_card_content);
        mAuthorName = (TextView) findViewById(R.id.cd_message_card_author);
        mDate = (TextView) findViewById(R.id.cd_message_card_date);
        mAvatarImageView = (ImageView) findViewById(R.id.cd_message_card_avatar);
        mAuthorInfoContainer = (LinearLayout) findViewById(R.id.cd_message_card_author_info_container);
        mOuterContainer = (LinearLayout) findViewById(R.id.cd_message_card_main_container);
    }
}
