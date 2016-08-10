package com.pedrosantos.conversationdisplayer.fragments.callbacks;

import com.pedrosantos.conversationdisplayer.models.api.CDDataSet;

/**
 * Methods that can be invoked by the MessagesListDataSource.
 */
public interface MessagesListUICallback extends BaseUICallback {

    /**
     * When a list of messages is loaded and ready to be displayed.
     *
     * @param dataSet dataSet containing users and messages of the conversation.
     */
    void onDataSetLoaded(CDDataSet dataSet);

}
