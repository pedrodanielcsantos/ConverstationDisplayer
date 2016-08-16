package com.pedrosantos.conversationdisplayer.datasources;

import com.pedrosantos.conversationdisplayer.views.fragments.callbacks.BaseUICallback;

import android.text.TextUtils;

/**
 * Datasource to be used by the splash fragment.
 */
public class SplashDataSource extends BaseDataSource<BaseUICallback> {

    public boolean isUsernameValid(String username) {
        return !TextUtils.isEmpty(username) && !username.contains(" ");
    }

}
