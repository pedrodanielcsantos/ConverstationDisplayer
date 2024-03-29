package com.pedrosantos.conversationdisplayer.datasources;

import com.pedrosantos.conversationdisplayer.R;
import com.pedrosantos.conversationdisplayer.localstorage.CDStorageManager;
import com.pedrosantos.conversationdisplayer.models.api.CDDataSet;
import com.pedrosantos.conversationdisplayer.models.api.Message;
import com.pedrosantos.conversationdisplayer.models.api.User;
import com.pedrosantos.conversationdisplayer.models.app.CDError;
import com.pedrosantos.conversationdisplayer.models.app.MessageListItem;
import com.pedrosantos.conversationdisplayer.promises.CDPromise;
import com.pedrosantos.conversationdisplayer.promises.DataSetPromises;
import com.pedrosantos.conversationdisplayer.utils.Constants;
import com.pedrosantos.conversationdisplayer.utils.comparators.MessageDateComparator;
import com.pedrosantos.conversationdisplayer.views.fragments.callbacks.MessagesListUICallback;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Pair;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
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

    public static final String DAY_BEGINING = " 00:00";
    public static final String DAY_END = " 23:59";
    //Static regexes
    private static final String REGEX_YYYY_MM_DD = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";//yyyy-mm-dd
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
            mFromRegex = mFrom + TAG_SEPARATOR + REGEX_STRING_WITHOUT_SPACES;
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
                        //Make message list sorted by date (ascending)
                        Collections.sort(result.getMessageList(), new MessageDateComparator());

                        if (mUICallback != null) {
                            mUICallback.onDataSetLoaded(result);
                        }
                        //When storing this object to persistence, clear all other records to make sure that it only exists 1 CDDataSet stored at once.
                        CDStorageManager.getInstance().storeObject(result, true);
                    }
                })
                .fail(new FailCallback<CDError>() {
                    @Override
                    public void onFail(final CDError error) {

                        onNetworkError(error);
                        if (mUICallback != null) {
                            //Load data from local storage, if any.
                            mUICallback.onDataSetLoaded(CDStorageManager.getInstance().getLastStoredObjectFromPersistence(CDDataSet.class));
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

            item.setContent(new SpannableString(message.getContent()));
            item.setMatchesSearch(false);
            item.setFromSelf(selfUsername.equals(author.getName()));
            item.setUserAvatar(author.getAvatarUrl());
            item.setUserName(new SpannableString(author.getName()));
            item.setMessageId(message.getId());
            item.setUserId(author.getId());

            Date d = new Date(message.getPostedTs() * 1000);
            item.setPostedDate(new SpannableString(Constants.CD_DATE_FORMATTER.format(d)));

            messageListItems.add(item);
        }

        return messageListItems;
    }

    /**
     * Clears the highlight (from the spannable strings) from the searchable fields of the
     * MessageListItem objects.
     */
    public List<MessageListItem> clearSearchResults(final List<MessageListItem> items) {

        //clear matches from past searches.
        for (int i = 0; i < items.size(); i++) {
            items.set(i, clearSearchItem(items.get(i)));
        }

        return items;
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
    public List<MessageListItem> searchInMessages(List<MessageListItem> items, String query) {
        int beforeOccurrences = countOccurrences(query, mBeforeRegex);
        int afterOccurrences = countOccurrences(query, mAfterRegex);
        int fromOccurrences = countOccurrences(query, mFromRegex);

        if (beforeOccurrences > MAX_ALLOWED_OCCURRENCES_PER_TAG ||
                afterOccurrences > MAX_ALLOWED_OCCURRENCES_PER_TAG ||
                fromOccurrences > MAX_ALLOWED_OCCURRENCES_PER_TAG) {
            //Return the error to the UI
            return null;
        }

        items = clearSearchResults(items);

        boolean isFirstFilter = true;

        Pair<String, List<MessageListItem>> result;

        //Applies before filter, if it's present on the query.
        if (beforeOccurrences > 0) {

            result = parseDateBefore(cloneArray(items), query);
            query = result.first;
            items = combineMatchesSearchItems(items, result.second, isFirstFilter);

            isFirstFilter = false;
        }

        //Applies after filter, if it's present on the query.
        if (afterOccurrences > 0) {
            result = parseDateAfter(cloneArray(items), query);
            query = result.first;
            items = combineMatchesSearchItems(items, result.second, isFirstFilter);

            isFirstFilter = false;
        }

        //Applies from filter, if it's present on the query.
        if (fromOccurrences > 0) {
            result = parseFromUser(cloneArray(items), query);
            query = result.first;
            items = combineMatchesSearchItems(items, result.second, isFirstFilter);

            isFirstFilter = false;
        }


        //Applies free filter, with the remaining query, after applying all other existent filters.
        result = parseFreeText(cloneArray(items), query);
        items = combineMatchesSearchItems(items, result.second, isFirstFilter);

        return items;
    }

    /**
     * Returns index of first item that is marked as matching a search or Constants.INVALID if none
     * is found.
     */
    public int indexOfFirstMatchedSearch(final List<MessageListItem> matchedItems) {
        for (int i = 0; i < matchedItems.size(); i++) {
            if (matchedItems.get(i).isMatchesSearch()) {
                return i;
            }
        }
        return Constants.INVALID;
    }

    /**
     * Helper method that creates a copy of the array passed as parameter (and its inner objects),
     * to help avoiding same-reference changes issues.
     */
    private List<MessageListItem> cloneArray(final List<MessageListItem> items) {
        List<MessageListItem> newItems = new ArrayList<>();
        for (final MessageListItem item : items) {
            newItems.add(new MessageListItem(item));
        }
        return newItems;
    }

    /**
     * Resets a MessageListItem - clears all spannable data and flags it as not matching a search.
     */
    private MessageListItem clearSearchItem(final MessageListItem item) {
        item.setMatchesSearch(false);
        item.setContent(new SpannableString(item.getContent()));
        item.setUserName(new SpannableString(item.getUserName()));
        item.setPostedDate(new SpannableString(item.getPostedDate()));

        return item;
    }

    /**
     * Method that makes the intersection of the two arrays passed as parameter, to help the search
     * process, after a certain filter (before/after/from/free text) was applied. If it's the first
     * match process, just returns the newly selected items. If not, per each pair of items, checks
     * if both match the search and, if so, it adds the newlySelectedItem to the array, because it
     * has the same state as in previouslySelectedItems, with the updates of the applied filter. If
     * one of them doesn't match the search, the intersection of the filters fails and the item's
     * state is reset before it's added.
     *
     * @param previouslySelectedItems array of items in the state (spannable strings, matches
     *                                search
     *                                or not, etc) before applying the filter.
     * @param newlySelectedItems      array of items in the new state (same as previouslySelectedItems,
     *                                but with the fields associated to the applied filter
     *                                updated),
     *                                after applying the filter.
     */
    private List<MessageListItem> combineMatchesSearchItems(final List<MessageListItem> previouslySelectedItems, final List<MessageListItem> newlySelectedItems, final boolean isFirstMatch) {
        if (isFirstMatch) {

            return newlySelectedItems;

        } else {
            final List<MessageListItem> matchedItems = new ArrayList<>();

            for (int i = 0; i < previouslySelectedItems.size(); i++) {

                if (previouslySelectedItems.get(i).isMatchesSearch() && newlySelectedItems.get(i).isMatchesSearch()) {
                    matchedItems.add(newlySelectedItems.get(i));
                } else {
                    final MessageListItem item = clearSearchItem(newlySelectedItems.get(i));
                    matchedItems.add(item);
                }
            }
            return matchedItems;
        }
    }

    private Pair<String, List<MessageListItem>> parseDateBefore(List<MessageListItem> items, String query) {
        return parseDateTag(items, query, true);
    }

    private Pair<String, List<MessageListItem>> parseDateAfter(final List<MessageListItem> items, final String query) {
        return parseDateTag(items, query, false);
    }

    /**
     * Parses the "before/after:date" tag - which are the items that are after/before the date string, using Date's native before/after methods.
     *
     * @param items    Original item list
     * @param query    Current query, including all search criteria
     * @param isBefore true if we're checking items before the specified date, false if we're
     *                 checking items after the specified date.
     * @return pair with updated query (without before/after date tag) and a list of items that
     * matched this filter.
     */
    private Pair<String, List<MessageListItem>> parseDateTag(List<MessageListItem> items, String query, boolean isBefore) {
        Pattern pattern = Pattern.compile(isBefore ? mBeforeRegex : mAfterRegex);
        Matcher matcher = pattern.matcher(query);
        String dateWithTag;

        if (matcher.find()) {
            dateWithTag = matcher.group();
            String dateWithoutTag = dateWithTag.replace((isBefore ? mBefore : mAfter) + TAG_SEPARATOR, "");

            Date queryDate = null;

            //Adding hours to make search query more specific and coherent.
            //This way after/before are excluding the day passed as parameter.
            if (isBefore) {
                dateWithoutTag += DAY_BEGINING;
            } else {
                dateWithoutTag += DAY_END;
            }

            //Try to create a valid date from the extracted date.
            try {
                queryDate = Constants.CD_DATE_FORMATTER.parse(dateWithoutTag);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (queryDate != null) {
                //Check matching items, depending on type of verification we're doing here - before or after
                for (final MessageListItem item : items) {
                    if (item.getPostedDate() != null) {
                        if (isBefore) {
                            try {
                                if (Constants.CD_DATE_FORMATTER.parse(item.getPostedDate()).before(queryDate)) {
                                    item.setMatchesSearch(true);
                                    item.setPostedDate(getSpannableString(item.getPostedDate(), item.getPostedDate()));
                                } else {
                                    item.setMatchesSearch(false);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                if (Constants.CD_DATE_FORMATTER.parse(item.getPostedDate()).after(queryDate)) {
                                    item.setMatchesSearch(true);
                                    item.setPostedDate(getSpannableString(item.getPostedDate(), item.getPostedDate()));
                                } else {
                                    item.setMatchesSearch(false);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }

            //update query by removing current tag (after/before) and respective date from it
            query = query.replace(dateWithTag, "");
        }

        return new Pair<>(query.trim(), items);
    }

    /**
     * Parses the "from:username" tag - which are the items that are from the user with username (case insensitive) "username".
     */
    private Pair<String, List<MessageListItem>> parseFromUser(final List<MessageListItem> items, String query) {
        Pattern pattern = Pattern.compile(mFromRegex);
        Matcher matcher = pattern.matcher(query);
        String fromWithTag;

        if (matcher.find()) {
            fromWithTag = matcher.group();

            String fromWithoutTag = fromWithTag.replace(mFrom + TAG_SEPARATOR, "");


            //Check matching items, depending on type of verification we're doing here - username is equal (ignoring case) to the one passed on the query
            for (final MessageListItem item : items) {
                if (item.getUserName() != null && item.getUserName().equalsIgnoreCase(fromWithoutTag)) {
                    item.setMatchesSearch(true);
                    item.setUserName(getSpannableString(item.getUserName(), item.getUserName()));
                } else {
                    item.setMatchesSearch(false);
                    item.setUserName(new SpannableString(item.getUserName()));
                }
            }

            //update query by removing current tag (after/before) and respective date from it
            query = query.replace(fromWithTag, "");
        }

        return new Pair<>(query.trim(), items);
    }

    /**
     * Identifies which of the items have in their content the whole query passed as parameter (case
     * insensitive).
     *
     * @return a pair with the query (unaltered) in the first parameter and the items that match
     * that query in the second parameter
     */
    private Pair<String, List<MessageListItem>> parseFreeText(final List<MessageListItem> items, final String query) {
        if (!TextUtils.isEmpty(query)) {
            for (final MessageListItem item : items) {
                if (item.getContent() != null && item.getContent().toLowerCase().contains(query.toLowerCase())) {
                    item.setMatchesSearch(true);
                    item.setContent(getSpannableString(item.getContent(), query));
                } else {
                    item.setMatchesSearch(false);
                    item.setContent(new SpannableString(item.getContent()));
                }
            }
        }
        return new Pair<>(query, items);
    }

    /**
     * Helper method to return a spannable string with all occurrences textToSpan in bold inside of
     * originalText
     */
    private SpannableString getSpannableString(final String originalText, final String textToSpan) {
        // make text bold
        final StyleSpan boldStyleSpan = new StyleSpan(Typeface.BOLD);
        SpannableString styledString = new SpannableString(originalText);

        if (!TextUtils.isEmpty(originalText) && !TextUtils.isEmpty(textToSpan)) {
            //Make texts lower case to make this highlighting case insensitive
            final String originalTextLowerCase = originalText.toLowerCase();
            final String textToSpanLowerCase = textToSpan.toLowerCase();

            //find index of first occurrence to start loop
            int indexOfOccurrence = originalTextLowerCase.indexOf(textToSpanLowerCase);

            while (indexOfOccurrence >= 0) {
                styledString.setSpan(new StyleSpan(Typeface.BOLD), indexOfOccurrence, indexOfOccurrence + textToSpan.length(), 0);
                //Look for index of next occurrence by looking on the substring after the current occurrence
                indexOfOccurrence = originalText.toLowerCase().indexOf(textToSpan.toLowerCase(), indexOfOccurrence + textToSpan.length());
            }
        }
        return styledString;
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
}
