package com.pedrosantos.conversationdisplayer.utils.comparators;

import com.pedrosantos.conversationdisplayer.models.api.Message;

import java.util.Comparator;

/**
 * Compares two Message models, sorting them by date posted (ascending).
 */
public class MessageDateComparator implements Comparator<Message> {
    @Override
    public int compare(final Message first, final Message second) {

        return Long.compare(first.getPostedTs(), second.getPostedTs());
    }
}
