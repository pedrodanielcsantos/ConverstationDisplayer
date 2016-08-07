package com.pedrosantos.conversationdisplayer.fragments.callbacks;

/**
 * Methods that can be invoked by the MessagesListDataSource.
 */
public interface MessagesListUICallback extends BaseUICallback {

    /**
     * When a list of messages is loaded and ready to be displayed.
     *
     * @param messagesCount number of messages available.
     */
    void onMessagesListLoaded(int messagesCount);

}
