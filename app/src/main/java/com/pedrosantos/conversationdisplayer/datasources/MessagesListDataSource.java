package com.pedrosantos.conversationdisplayer.datasources;

import com.pedrosantos.conversationdisplayer.models.api.CDDataSet;
import com.pedrosantos.conversationdisplayer.models.api.Message;
import com.pedrosantos.conversationdisplayer.models.api.User;
import com.pedrosantos.conversationdisplayer.models.app.CDError;
import com.pedrosantos.conversationdisplayer.models.app.MessageListItem;
import com.pedrosantos.conversationdisplayer.promises.CDPromise;
import com.pedrosantos.conversationdisplayer.promises.DataSetPromises;
import com.pedrosantos.conversationdisplayer.views.fragments.callbacks.MessagesListUICallback;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataSource for the MessagesListFragment.
 */
public class MessagesListDataSource extends BaseDataSource<MessagesListUICallback> {

    /**
     * Loads a list of messages for a given conversation.
     */
    public void loadMessagesList() {
        CDPromise.when(DataSetPromises.getDataSet())
                .done(new DoneCallback<CDDataSet>() {
                    @Override
                    public void onDone(final CDDataSet result) {
                        if (mUICallback != null) {
                            mUICallback.onDataSetLoaded(result);
                        }
                        //save result to database
                    }
                })
                .fail(new FailCallback<CDError>() {
                    @Override
                    public void onFail(final CDError error) {
                        //fetch results from database
                        onNetworkError(error);
                        if (mUICallback != null) {
                            mUICallback.onDataSetLoaded(null);
                        }
                    }
                });
    }

    public List<MessageListItem> createMessageListItems(final CDDataSet dataSet, final String selfUsername) {
        Map<Long, User> userMap = new HashMap<>();

        for (final User user : dataSet.getUserList()) {
            userMap.put(user.getId(), user);
        }

        List<MessageListItem> messageListItems = new ArrayList<>();

        for (final Message message : dataSet.getMessageList()) {
            final MessageListItem item = new MessageListItem();
            final User author = userMap.get(message.getUserId());

            item.setContent(message.getContent());
            item.setExpanded(false);
            item.setFromSelf(selfUsername.equals(author.getName()));
            item.setUserAvatar(author.getAvatarUrl());
            item.setUserName(author.getName());
            item.setMessageId(message.getId());

            messageListItems.add(item);
        }

        return messageListItems;
    }
}
