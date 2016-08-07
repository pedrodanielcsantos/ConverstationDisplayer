package com.pedrosantos.conversationdisplayer.fragments;

import com.pedrosantos.conversationdisplayer.datasources.BaseDataSource;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.lang.reflect.ParameterizedType;

/**
 * Class that acts as the base for all fragments, containing common fields and methods.
 */
public abstract class BaseFragment<DS extends BaseDataSource> extends Fragment implements BaseUICallback {

    protected DS mDataSource;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        final Class<DS> dataSourceType = (Class<DS>) superClass.getActualTypeArguments()[0];
        try {
            mDataSource = dataSourceType.newInstance();
            mDataSource.attachUICallback(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
