package com.pedrosantos.conversationdisplayer.datasources;

import com.pedrosantos.conversationdisplayer.fragments.MessagesListUICallback;
import com.pedrosantos.conversationdisplayer.models.CDDataSet;
import com.pedrosantos.conversationdisplayer.models.CDError;
import com.pedrosantos.conversationdisplayer.promises.CDPromise;
import com.pedrosantos.conversationdisplayer.promises.DataSetPromises;
import com.pedrosantos.conversationdisplayer.utils.Constants;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

/**
 *
 */
public class MessagesListDataSource extends BaseDataSource<MessagesListUICallback> {

    public void loadMessagesList(){
        CDPromise.when(DataSetPromises.getDataSet())
                .done(new DoneCallback<CDDataSet>() {
                    @Override
                    public void onDone(final CDDataSet result) {
                        mUICallback.onMessagesListLoaded(result.getMessageList().size());
                    }
                })
                .fail(new FailCallback<CDError>() {
                    @Override
                    public void onFail(final CDError error) {
                        mUICallback.onMessagesListLoaded(Constants.INVALID);
                    }
                });
    }

}
