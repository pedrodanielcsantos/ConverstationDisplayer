package com.pedrosantos.conversationdisplayer.datasources;

import com.pedrosantos.conversationdisplayer.R;
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
import android.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DataSource for the MessagesListFragment.
 */
public class MessagesListDataSource extends BaseDataSource<MessagesListUICallback> {

    //Static regexes
    private static final String REGEX_YYYY_MM_DD = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";//yyyy-mm-dd
    private static final String YYYY_MM_DD_FORMAT = "yyyy-mm-dd";
    private static final String REGEX_STRING_WITHOUT_SPACES = "([^\\s]+)";
    //Standard constants
    private static final int MAX_ALLOWED_OCCURRENCES_PER_TAG = 1;
    private static final String TAG_SEPARATOR = ":";

    //dynamic regexes and strings
    private String mBeforeRegex;
    private String mAfterRegex;
    private String mFromRegex;

    private String mBefore;
    private String mAfter;
    private String mFrom;

    @Override
    public void attachUICallback(final MessagesListUICallback uiCallback) {
        super.attachUICallback(uiCallback);
        if (mUICallback != null) {
            mBefore = mUICallback.getTranslatedString(R.string.before);
            mBeforeRegex = mBefore + TAG_SEPARATOR + REGEX_YYYY_MM_DD;

            mAfter = mUICallback.getTranslatedString(R.string.after);
            mAfterRegex = mAfter + TAG_SEPARATOR + REGEX_YYYY_MM_DD;

            mFrom = mUICallback.getTranslatedString(R.string.from);
            mFromRegex = mFrom + TAG_SEPARATOR + REGEX_YYYY_MM_DD;
        }
    }

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

            Date d = new Date(message.getPostedTs() * 1000);
            item.setPostedDate(d);

            messageListItems.add(item);
        }

        return messageListItems;
    }

    /**
     * Filters the items list passed as parameter with the query content.
     * Currently supported filters:
     * . before:yyyy-MM-dd - items before the indicated date, exclusivé
     * . after:yyyy-MM-dd - items before the indicated date, exclusivé
     * . from:username - items whose author's username is specified by the "username" value
     * . free text - all keywords that don't fit in the above categories.
     *
     * All the supported tags are translated to the application's language.
     */
    public void searchInMessages(List<MessageListItem> items, String query) {
        int beforeOccurrences = countOccurrences(query, mBeforeRegex);
        int afterOccurrences = countOccurrences(query, mAfterRegex);

        Log.d("MessagesListDataSource", "Occurrences of before: " + beforeOccurrences);
        Log.d("MessagesListDataSource", "Occurrences of after: " + afterOccurrences);


        if (beforeOccurrences > MAX_ALLOWED_OCCURRENCES_PER_TAG) {
            //Return the error to the UI
            return;
        }

        Pair<String, List<MessageListItem>> result = parseDateBefore(items, query);
        query = result.first;
        Log.d("MessagesListDataSource", "Query after 'before' parse: " + query + " | Matching items: " + result.second.size());

        result = parseDateAfter(items, query);
        query = result.first;
        Log.d("MessagesListDataSource", "Query after 'after' parse: " + query + " | Matching items: " + result.second.size());
    }

    private Pair<String, List<MessageListItem>> parseDateBefore(List<MessageListItem> items, String query) {
        return parseDateTag(items, query, true);
    }

    private Pair<String, List<MessageListItem>> parseDateAfter(final List<MessageListItem> items, final String query) {
        return parseDateTag(items, query, false);
    }

    /**
     * @param items    Original item list
     * @param query    Current query, including all search criteria
     * @param isBefore true if we're checking items before the specified date, false if we're
     *                 checking items after the specified date.
     * @return pair with updated query (without before/after date tag) and a list of items that
     * matched this filter.
     */
    private Pair<String, List<MessageListItem>> parseDateTag(List<MessageListItem> items, String query, boolean isBefore) {
        List<MessageListItem> matchingItems = new ArrayList<>();
        Pattern pattern = Pattern.compile(isBefore ? mBeforeRegex : mAfterRegex);
        Matcher matcher = pattern.matcher(query);
        String dateWithTag;

        if (matcher.find()) {
            dateWithTag = matcher.group();
            String dateWithoutTag = dateWithTag.replace((isBefore ? mBefore : mAfter) + TAG_SEPARATOR, "");

            SimpleDateFormat formatter = new SimpleDateFormat(YYYY_MM_DD_FORMAT);
            Date queryDate = null;

            //Try to create a valid date from the extracted date.
            try {
                queryDate = formatter.parse(dateWithoutTag);
                Log.d("MessagesListDataSource", "Created date from tag: " + queryDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (queryDate != null) {
                //Check matching items, depending on type of verification we're doing here - before or after
                for (final MessageListItem item : items) {
                    if (isBefore) {
                        if (item.getPostedDate().before(queryDate)) {
                            matchingItems.add(item);
                        }
                    } else {
                        if (item.getPostedDate().after(queryDate)) {
                            matchingItems.add(item);
                        }
                    }

                }
            }

            //update query by removing current tag (after/before) and respective date from it
            query = query.replace(dateWithTag, "");
        }

        return new Pair<>(query.trim(), matchingItems);
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
        //TODO
    }

    private void parseFreeText(final List<MessageListItem> items, final String query) {
        //TODO
    }
}
