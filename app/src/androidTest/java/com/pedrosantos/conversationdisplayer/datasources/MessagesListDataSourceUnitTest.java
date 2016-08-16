package com.pedrosantos.conversationdisplayer.datasources;

import com.pedrosantos.conversationdisplayer.models.api.CDDataSet;
import com.pedrosantos.conversationdisplayer.models.app.MessageListItem;
import com.pedrosantos.conversationdisplayer.utils.Constants;
import com.pedrosantos.conversationdisplayer.views.fragments.callbacks.MessagesListUICallback;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Tests on the MessagesListDataSource logic.
 */
public class MessagesListDataSourceUnitTest extends BaseDataSourceUnitTest<MessagesListDataSource> implements MessagesListUICallback {

    private static final String MOCK_DATASETS_FOLDER = "mockdatasets";
    private List<MessageListItem> mItems;

    @Override
    public void onDataSetLoaded(final CDDataSet dataSet) {
        //Empty for now.
    }

    /**
     * For the purpose of this test, we'll always assume that the user that's consulting the info
     * is
     * "billgates".
     */
    @Before
    public void setup() {
        final CDDataSet dataSet = getConvertedObjectFromString(CDDataSet.class, getFileFromAssets(MOCK_DATASETS_FOLDER, "mock_dataset.json"));
        mItems = mDataSource.createMessageListItems(dataSet, "billgates");
    }

    @Test
    public void isFromSelfTest() {

        int selfCount = 0;
        for (final MessageListItem item : mItems) {
            if (item.isFromSelf()) {
                selfCount++;
            }
        }

        assertEquals("There should be 2 messages from self (billgates)", 2, selfCount);
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

        matchingItems = mDataSource.searchInMessages(mItems, "before:2016-07-05");
        assertEquals("There should be only 1 message before 2016-07-05", 1, countMatchingSearchItems(matchingItems));

        matchingItems = mDataSource.searchInMessages(mItems, "before:2016-08-05");
        assertEquals("All messages are from before 2016-8-5", 4, countMatchingSearchItems(matchingItems));

        //Edge case: searching for items before a date where items exist. As search is exclusivé, should return no items.
        matchingItems = mDataSource.searchInMessages(mItems, "before:2016-7-4");
        assertEquals("There should be no messages before 2016-7-4", 0, countMatchingSearchItems(matchingItems));
    }

    @Test
    public void afterSearchTest() {
        List<MessageListItem> matchingItems;
        matchingItems = mDataSource.searchInMessages(mItems, "after:2016-7-3");
        assertEquals("All items are from after 2016-7-3", 4, countMatchingSearchItems(matchingItems));

        matchingItems = mDataSource.searchInMessages(mItems, "after:2016-07-05");
        assertEquals("There should be 3 messages before 2016-07-05", 3, countMatchingSearchItems(matchingItems));

        matchingItems = mDataSource.searchInMessages(mItems, "after:2016-08-05");
        assertEquals("There should be no items after 2016-8-5", 0, countMatchingSearchItems(matchingItems));

        //Edge case: after with day of an existing message. As it's exlusivé, result should be 0
        matchingItems = mDataSource.searchInMessages(mItems, "after:2016-08-04");
        assertEquals("There should be no items after 2016-8-4", 0, countMatchingSearchItems(matchingItems));
    }

    @Test
    public void fromSearchTest() {
        List<MessageListItem> matchingItems;
        matchingItems = mDataSource.searchInMessages(mItems, "from:stevejobs");
        assertEquals("There should be 2 items from stevejobs", 2, countMatchingSearchItems(matchingItems));

        matchingItems = mDataSource.searchInMessages(mItems, "from:STEVEJOBS");
        assertEquals("There should be 2 items from stevejobs (as search it's case insensitive)", 2, countMatchingSearchItems(matchingItems));

        matchingItems = mDataSource.searchInMessages(mItems, "from:sTeVeJoBs");
        assertEquals("There should be 2 items from sTeVeJoBs (as search it's case insensitive)", 2, countMatchingSearchItems(matchingItems));

        matchingItems = mDataSource.searchInMessages(mItems, "from:nonExistingUser");
        assertEquals("There should be no results, as the specified user does not exist", 0, countMatchingSearchItems(matchingItems));
    }

    @Test
    public void freeTextSearch() {
        List<MessageListItem> matchingItems;

        matchingItems = mDataSource.searchInMessages(mItems, "dummy");
        assertEquals("Result should have all items, as they all contain the passed expression and search is case insensitive", 4, countMatchingSearchItems(matchingItems));

        matchingItems = mDataSource.searchInMessages(mItems, "dummy MESSAGE");
        assertEquals("Result should have all items, as they all contain the passed expression and search is case insensitive", 4, countMatchingSearchItems(matchingItems));

        matchingItems = mDataSource.searchInMessages(mItems, "dummi");
        assertEquals("Result should have 0 results, as no item contains this whole expression", 0, countMatchingSearchItems(matchingItems));
    }

    /**
     * This test applies multiple tags and free text to a query and evaluates the result of the
     * intersection of these tags.
     */
    @Test
    public void multipleTagsSearchTest() {
        List<MessageListItem> matchingItems;
        matchingItems = mDataSource.searchInMessages(mItems, "before:2016-7-23 after:2016-7-21");
        assertEquals("There should be 2 items within this time range (before:2016-7-23 after:2016-7-21)", 2, countMatchingSearchItems(matchingItems));

        matchingItems = mDataSource.searchInMessages(mItems, "after:2016-7-23 before:2016-7-21");
        assertEquals("There should be no items items within this time range as it's invalid for an intersection", 0, countMatchingSearchItems(matchingItems));

        //Testing intersection. Before with valid results but after not
        matchingItems = mDataSource.searchInMessages(mItems, "after:2016-7-22 before:2016-7-23");
        assertEquals("There should be no items items within this time range, as their intersection is empty.", 0, countMatchingSearchItems(matchingItems));

        matchingItems = mDataSource.searchInMessages(mItems, "from:stevejobs 04");
        assertEquals("There should be 1 message from stevejobs that contains the expresion '04'", 1, countMatchingSearchItems(matchingItems));

        matchingItems = mDataSource.searchInMessages(mItems, "from:stevejobs 05");
        assertEquals("There should be no messages, as messages from stevejobs don't contain the expression '05'", 0, countMatchingSearchItems(matchingItems));

        matchingItems = mDataSource.searchInMessages(mItems, "from:stevejobs from:billgates");
        assertNull("Result should be null, as there where multiple valid occurrences of from", matchingItems);

        matchingItems = mDataSource.searchInMessages(mItems, "from:stevejobs from billgates");
        assertNotNull("Result should not be null, as there where not multiple valid occurrences of from", matchingItems);
        assertEquals("There should be no results for this query, despite being valid", 0, countMatchingSearchItems(matchingItems));
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
