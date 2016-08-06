package com.pedrosantos.conversationdisplayer.promises;

import com.pedrosantos.conversationdisplayer.models.CDDataSet;
import com.pedrosantos.conversationdisplayer.models.CDNetworkError;
import com.pedrosantos.conversationdisplayer.network.clients.CDNetworkClient;

import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Static class that holds the promises to invoke the methods in the DataSetAPI.
 */
public class DataSetPromises extends BaseAPIPromise {

    public static Promise<CDDataSet, CDNetworkError, Void> getDataSet() {
        final DeferredObject<CDDataSet, CDNetworkError, Void> deferredObject = new DeferredObject<>();

        CDNetworkClient.getInstance().getConversationAPI().getData().enqueue(new Callback<CDDataSet>() {
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
