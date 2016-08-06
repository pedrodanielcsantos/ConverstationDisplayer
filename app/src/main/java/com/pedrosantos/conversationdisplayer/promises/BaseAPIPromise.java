package com.pedrosantos.conversationdisplayer.promises;

import com.pedrosantos.conversationdisplayer.models.CDNetworkError;
import com.pedrosantos.conversationdisplayer.utils.Constants;

import org.jdeferred.impl.DeferredObject;

import retrofit2.Response;

/**
 * Base class for all Promises holder classes, with handling for error and success scenarios.
 */
public class BaseAPIPromise {

    protected static <Result, Progress> void handleOnResponse(DeferredObject<Result, CDNetworkError, Progress> deferredObject, Response<Result> result) {
        if (result.isSuccessful()) {
            deferredObject.resolve(result.body());
        } else {
            deferredObject.reject(new CDNetworkError(result.code(), result.message()));
        }
    }

    protected static <Result, Progress> void handleOnFailure(final DeferredObject<Result, CDNetworkError, Progress> deferredObject, final Throwable t) {
        deferredObject.reject(new CDNetworkError(Constants.INVALID, t.getMessage()));
    }

}
