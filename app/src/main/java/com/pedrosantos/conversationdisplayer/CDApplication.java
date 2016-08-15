package com.pedrosantos.conversationdisplayer;

import com.pedrosantos.conversationdisplayer.localstorage.CDStorageManager;

import android.app.Application;

public class CDApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CDStorageManager.init(this);
    }

}
