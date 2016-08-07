package com.pedrosantos.conversationdisplayer.datasources;

import com.pedrosantos.conversationdisplayer.fragments.callbacks.MessagesListUICallback;
import com.pedrosantos.conversationdisplayer.models.CDDataSet;
import com.pedrosantos.conversationdisplayer.models.CDError;
import com.pedrosantos.conversationdisplayer.promises.CDPromise;
import com.pedrosantos.conversationdisplayer.promises.DataSetPromises;
import com.pedrosantos.conversationdisplayer.utils.Constants;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

/**
 * DataSource for the MessagesListFragment.
 */
public class MessagesListDataSource extends BaseDataSource<MessagesListUICallback> {

    /**
     * Loads a list of messages for a given conversation.
     */
    public void loadMessagesList(){
        CDPromise.when(DataSetPromises.getDataSet())
                .done(new DoneCallback<CDDataSet>() {
                    @Override
                    public void onDone(final CDDataSet result) {
                        if (mUICallback != null) {
                            mUICallback.onMessagesListLoaded(result.getMessageList().size());
                        }
                    }
                })
                .fail(new FailCallback<CDError>() {
                    @Override
                    public void onFail(final CDError error) {
                        if (mUICallback != null) {
                            mUICallback.onMessagesListLoaded(Constants.INVALID);
                        }
                        onNetworkError(error);
                    }
                });
    }

}
