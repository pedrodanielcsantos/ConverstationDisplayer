package com.pedrosantos.conversationdisplayer.fragments;

import com.pedrosantos.conversationdisplayer.R;
import com.pedrosantos.conversationdisplayer.datasources.MessagesListDataSource;
import com.pedrosantos.conversationdisplayer.fragments.callbacks.MessagesListUICallback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 *
 */
public class MessageListFragment extends BaseFragment<MessagesListDataSource> implements MessagesListUICallback {

    public static final String TAG = "MessageListFragment";
    private TextView mDummyTextView;

    public static MessageListFragment newInstance() {
        return new MessageListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages_list, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDummyTextView = (TextView) view.findViewById(R.id.dummy_text_view);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDataSource.loadMessagesList();
    }

    @Override
    public void onMessagesListLoaded(final int messagesCount) {
        mDummyTextView.setText(messagesCount + " messages retrieved from server.");
    }
}
