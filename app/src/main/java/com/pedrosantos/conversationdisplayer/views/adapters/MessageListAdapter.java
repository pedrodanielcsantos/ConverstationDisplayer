package com.pedrosantos.conversationdisplayer.views.adapters;

import com.pedrosantos.conversationdisplayer.views.customviews.CDMessageCard;
import com.pedrosantos.conversationdisplayer.models.app.MessageListItem;

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

        public CDMessageCard mCard;

        public MessageListVH(CDMessageCard v) {
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
        return new MessageListVH(new CDMessageCard(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(final MessageListVH holder, final int position) {
        final MessageListItem messageListItem = mItemList.get(position);

        holder.mCard.setContentText(messageListItem.getContent());
        holder.mCard.setFromSelf(messageListItem.isFromSelf());
        holder.mCard.setAuthorName(messageListItem.getUserName());
        holder.mCard.setAvatarImage(messageListItem.getUserAvatar());
    }

    @Override
    public int getItemCount() {
        return (mItemList != null ? mItemList.size() : 0);
    }

    public void setItems(final List<MessageListItem> items) {
        mItemList = items;
    }
}
