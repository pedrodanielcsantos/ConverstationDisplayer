package com.pedrosantos.conversationdisplayer.datasources;

import com.pedrosantos.conversationdisplayer.models.api.CDDataSet;
import com.pedrosantos.conversationdisplayer.models.app.MessageListItem;
import com.pedrosantos.conversationdisplayer.utils.Constants;
import com.pedrosantos.conversationdisplayer.views.fragments.callbacks.MessagesListUICallback;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 *
 */
public class MessagesListDataSourceUnitTest extends BaseDataSourceUnitTest<MessagesListDataSource> implements MessagesListUICallback {

    private static final String MOCK_DATASETS_FOLDER = "mockdatasets";
    private CDDataSet mDataSet;
    private List<MessageListItem> mItems;

    @Override
    public void onDataSetLoaded(final CDDataSet dataSet) {

    }

    @Before
    public void setup() {
        mDataSet = getConvertedObjectFromString(CDDataSet.class, getFileFromAssets(MOCK_DATASETS_FOLDER, "mock_dataset.json"));
        mItems = mDataSource.createMessageListItems(mDataSet, "Katarina");
    }

    @Test
    public void isFromSelfTest() {

        int selfCount = 0;
        for (final MessageListItem item : mItems) {
            if (item.isFromSelf()) {
                selfCount++;
            }
        }

        assertEquals("There should be 2 messages from self (Katarina)", 2, selfCount);
    }

    @Test
    public void indexOfFirstMatchedSearchTest() {
        final int indexOfFirstMatch = 1;

        //default scenario, no item is selected
        assertEquals("No item is selected, so index should be Constants.INVALID", Constants.INVALID, mDataSource.indexOfFirstMatchedSearch(mItems));

        //mark one item as matching search
        mItems.get(indexOfFirstMatch).setMatchesSearch(true);

        assertEquals("No item is selected, so index should be Constants.INVALID", indexOfFirstMatch, mDataSource.indexOfFirstMatchedSearch(mItems));
    }

    @Test
    public void beforeSearchTest() {
        List<MessageListItem> matchingItems;
        matchingItems = mDataSource.searchInMessages(mItems, "before:2016-7-3");
        assertEquals("There should be no messages before 2016-7-3", 0, countMatchingSearchItems(matchingItems));
        mItems = mDataSource.clearSearchResults(mItems);

        matchingItems = mDataSource.searchInMessages(mItems, "before:2016-07-05");
        assertEquals("There should be only 1 message before 2016-07-05", 1, countMatchingSearchItems(matchingItems));
        mItems = mDataSource.clearSearchResults(mItems);

        matchingItems = mDataSource.searchInMessages(mItems, "before:2016-08-05");
        assertEquals("All messages are from before 2016-8-5", 4, countMatchingSearchItems(matchingItems));
    }

    @Test
    public void afterSearchTest() {
        List<MessageListItem> matchingItems;
        matchingItems = mDataSource.searchInMessages(mItems, "after:2016-7-3");
        assertEquals("All items are from after 2016-7-3", 4, countMatchingSearchItems(matchingItems));
        mItems = mDataSource.clearSearchResults(mItems);

        matchingItems = mDataSource.searchInMessages(mItems, "after:2016-07-05");
        assertEquals("There should be 3 messages before 2016-07-05", 3, countMatchingSearchItems(matchingItems));
        mItems = mDataSource.clearSearchResults(mItems);

        matchingItems = mDataSource.searchInMessages(mItems, "after:2016-08-05");
        assertEquals("There should be no items after 2016-8-5", 0, countMatchingSearchItems(matchingItems));
    }

    @Test
    public void beforeAndAfterSearchTest() {
        List<MessageListItem> matchingItems;
        matchingItems = mDataSource.searchInMessages(mItems, "before:2016-7-23 after:2016-7-21");
        assertEquals("There should be 2 items within this time range (before:2016-7-23 after:2016-7-21)", 2, countMatchingSearchItems(matchingItems));
        mItems = mDataSource.clearSearchResults(mItems);

        matchingItems = mDataSource.searchInMessages(mItems, "after:2016-7-23 before:2016-7-21");
        assertEquals("There should be no items items within this time range as it's invalid for an intersection", 0, countMatchingSearchItems(matchingItems));
        mItems = mDataSource.clearSearchResults(mItems);

    }

    /**
     * Helper method that counts how many items are marked as matching a search
     */
    private int countMatchingSearchItems(final List<MessageListItem> matchingItems) {
        int matchCount = 0;
        for (final MessageListItem item : matchingItems) {
            if (item.isMatchesSearch()) {
                matchCount++;
            }
        }
        return matchCount;
    }
}
