package com.pedrosantos.conversationdisplayer.datasources;

import com.pedrosantos.conversationdisplayer.fragments.callbacks.BaseUICallback;

import android.text.Editable;
import android.text.TextUtils;

/**
 * Datasource to be used by the splash fragment.
 */
public class SplashDataSource extends BaseDataSource<BaseUICallback> {

    public boolean isUsernameValid(Editable username) {
        return !TextUtils.isEmpty(username) && !username.toString().contains(" ");
    }

}
