package com.pedrosantos.conversationdisplayer.views.fragments;

import com.pedrosantos.conversationdisplayer.R;
import com.pedrosantos.conversationdisplayer.datasources.MessagesListDataSource;
import com.pedrosantos.conversationdisplayer.models.api.CDDataSet;
import com.pedrosantos.conversationdisplayer.models.app.MessageListItem;
import com.pedrosantos.conversationdisplayer.views.adapters.MessageListAdapter;
import com.pedrosantos.conversationdisplayer.views.fragments.callbacks.MessagesListUICallback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Fragment that displays a list of messages that compose the conversation.
 */
public class MessageListFragment extends BaseFragment<MessagesListDataSource> implements MessagesListUICallback, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "MessageListFragment";
    private static final String SELF_USERNAME_KEY = "selfUsernameKey";
    private View mFullScreenProgress;
    private RecyclerView mMessagesRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MessageListAdapter mAdapter;

    public static MessageListFragment newInstance(final String selfUsername) {
        Bundle arguments = new Bundle();
        MessageListFragment fragment = new MessageListFragment();
        arguments.putString(SELF_USERNAME_KEY, selfUsername);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages_list, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFullScreenProgress = view.findViewById(R.id.full_screen_progress);
        mMessagesRecyclerView = (RecyclerView) view.findViewById(R.id.messages_list_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.messages_list_swipe_to_refresh);
        mMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDataSource.loadMessagesList();
    }

    @Override
    public void onDataSetLoaded(final CDDataSet dataSet) {
        if (dataSet != null) {
            List<MessageListItem> messageListItems = mDataSource.createMessageListItems(dataSet, getSelfUsername());
            mAdapter = new MessageListAdapter(messageListItems);

            mMessagesRecyclerView.setAdapter(mAdapter);
        }

        mFullScreenProgress.setVisibility(View.GONE);

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onRefresh() {
        mDataSource.loadMessagesList();
    }

    private String getSelfUsername() {
        return getArguments().getString(SELF_USERNAME_KEY);
    }
}
