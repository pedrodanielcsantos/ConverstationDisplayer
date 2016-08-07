package com.pedrosantos.conversationdisplayer.datasources;

import com.pedrosantos.conversationdisplayer.fragments.BaseUICallback;

/**
 * Created by Asus on 07/08/2016.
 */
public abstract class BaseDataSource<T extends BaseUICallback> {

    protected T mUICallback;

    public void attachUICallback(T uiCallback){
        mUICallback = uiCallback;
    }

    public void detachUICallback(){
        mUICallback = null;
    }
}
