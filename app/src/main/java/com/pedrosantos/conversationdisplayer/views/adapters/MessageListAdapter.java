package com.pedrosantos.conversationdisplayer.views.adapters;

import com.pedrosantos.conversationdisplayer.R;
import com.pedrosantos.conversationdisplayer.models.app.MessageListItem;
import com.pedrosantos.conversationdisplayer.utils.Constants;
import com.pedrosantos.conversationdisplayer.views.customviews.CDMessageCardView;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Adapter for the messages list recycler view.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListVH> {

    /**
     * View holder for each item.
     */
    public static class MessageListVH extends RecyclerView.ViewHolder {

        public CDMessageCardView mCard;

        public MessageListVH(CDMessageCardView v) {
            super(v);
            mCard = v;
        }
    }

    private List<MessageListItem> mItemList;

    public MessageListAdapter(List<MessageListItem> items) {
        super();
        mItemList = items;
    }

    @Override
    public MessageListVH onCreateViewHolder(final ViewGroup parent, final int viewType) {

        // create a new view
        return new MessageListVH(new CDMessageCardView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(final MessageListVH holder, final int position) {
        final MessageListItem messageListItem = mItemList.get(position);

        holder.mCard.setContentText(messageListItem.getContentSpannableString());
        holder.mCard.setFromSelf(messageListItem.isFromSelf());
        holder.mCard.setAuthorName(messageListItem.getUserNameSpannableString());
        holder.mCard.setAvatarImage(messageListItem.getUserAvatar());
        holder.mCard.setDate(messageListItem.getPostedDateSpannableString());

    }

    @Override
    public int getItemCount() {
        return (mItemList != null ? mItemList.size() : 0);
    }

    public List<MessageListItem> getItems() {
        return mItemList;
    }

    /**
     * Sets a new list of items for this adapter.
     */
    public void setItems(final List<MessageListItem> items) {
        mItemList = items;
    }
}
