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

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DataSource for the MessagesListFragment.
 */
public class MessagesListDataSource extends BaseDataSource<MessagesListUICallback> {

    private static final String YYYY_MM_DD = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";//yyyy-mm-dd
    private static final String REGEX_BEFORE = "before:" + YYYY_MM_DD;
    private static final String REGEX_AFTER = "after:" + YYYY_MM_DD;
    private static final String REGEX_FROM = "from:([^\\s]+)";


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

    /**
     * Creates a list of MessageListItems based on the dataset passed as parameter, identifying
     * messages that are from the current user (identified by selfUsername parameter).
     */
    public List<MessageListItem> createMessageListItems(final CDDataSet dataSet, final String selfUsername) {
        Map<Long, User> userMap = new HashMap<>();

        //Create map with users info, indexed by userId
        for (final User user : dataSet.getUserList()) {
            userMap.put(user.getId(), user);
        }

        List<MessageListItem> messageListItems = new ArrayList<>();

        //Create list of MessageListItems, joining user and message info.
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


    public void searchInMessages(final List<MessageListItem> items, final String query) {
        int beforeOccurrences = countOccurrences(query, REGEX_BEFORE);
        int afterOccurrences = countOccurrences(query, REGEX_AFTER);
        int fromOccurrences = countOccurrences(query, REGEX_FROM);

        Log.d("MessagesListDataSource", "Occurrences of before: " + beforeOccurrences);
        Log.d("MessagesListDataSource", "Occurrences of after: " + afterOccurrences);
        Log.d("MessagesListDataSource", "Occurrences of from: " + fromOccurrences);

        parseDateBefore(items, query);
        parseDateAfter(items, query);
        parseFreeText(items, query);
        parseFromUser(items, query);
    }

    /**
     * Counts the number of occurrences of regex on query
     */
    private int countOccurrences(final String query, final String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(query);
        int occurrencesFound = 0;
        while (matcher.find()) {
            occurrencesFound++;
        }

        return occurrencesFound;
    }

    private void parseFromUser(final List<MessageListItem> items, final String query) {

    }



    private void parseFreeText(final List<MessageListItem> items, final String query) {
    }

    private void parseDateAfter(final List<MessageListItem> items, final String query) {

    }

    private void parseDateBefore(final List<MessageListItem> items, final String query) {

    }
}
