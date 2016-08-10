package com.pedrosantos.conversationdisplayer.datasources;

import com.pedrosantos.conversationdisplayer.fragments.callbacks.BaseUICallback;
import com.pedrosantos.conversationdisplayer.models.app.CDError;

/**
 * Base class for all datasources, with common fields and methods.
 */
public abstract class BaseDataSource<T extends BaseUICallback> {

    protected T mUICallback;

    public void attachUICallback(T uiCallback){
        mUICallback = uiCallback;
    }

    public void detachUICallback(){
        mUICallback = null;
    }

    /**
     * Generic method to handle with network errors, invoking ui's error handling.
     */
    protected void onNetworkError(CDError error) {
        if (mUICallback != null) {
            mUICallback.onNetworkError(error);
        }
    }
}
