package com.pedrosantos.conversationdisplayer.promises;

import com.pedrosantos.conversationdisplayer.models.CDError;
import com.pedrosantos.conversationdisplayer.utils.Constants;

import org.jdeferred.impl.DeferredObject;

import retrofit2.Response;

/**
 * Base class for all Promises holder classes, with handling for error and success scenarios.
 */
public class BasePromise {

    protected static <Result, Progress> void handleOnResponse(DeferredObject<Result, CDError, Progress> deferredObject, Response<Result> result) {
        //Check if the response was in fact successfull
        if (result.isSuccessful()) {
            deferredObject.resolve(result.body());
        } else {
            deferredObject.reject(new CDError(result.code(), result.message()));
        }
    }

    protected static <Result, Progress> void handleOnFailure(final DeferredObject<Result, CDError, Progress> deferredObject, final Throwable t) {
        deferredObject.reject(new CDError(Constants.INVALID, t.getMessage()));
    }

}
