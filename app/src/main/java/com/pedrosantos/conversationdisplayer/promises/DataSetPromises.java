package com.pedrosantos.conversationdisplayer.promises;

import com.pedrosantos.conversationdisplayer.models.api.CDDataSet;
import com.pedrosantos.conversationdisplayer.models.app.CDError;
import com.pedrosantos.conversationdisplayer.network.clients.CDNetworkClient;

import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Static class that holds the promises to invoke the methods in the DataSetAPI.
 */
public class DataSetPromises extends BasePromise {

    public static Promise<CDDataSet, CDError, Void> getDataSet() {
        final DeferredObject<CDDataSet, CDError, Void> deferredObject = new DeferredObject<>();

        CDNetworkClient.getInstance().getConversationAPI().getDataSet().enqueue(new Callback<CDDataSet>() {
            @Override
            public void onResponse(final Call<CDDataSet> call, final Response<CDDataSet> response) {
                handleOnResponse(deferredObject, response);
            }

            @Override
            public void onFailure(final Call<CDDataSet> call, final Throwable t) {
                handleOnFailure(deferredObject, t);
            }
        });

        return deferredObject.promise();
    }

}
