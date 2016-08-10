package com.pedrosantos.conversationdisplayer.views.fragments;

import com.pedrosantos.conversationdisplayer.R;
import com.pedrosantos.conversationdisplayer.activities.ActivityCallback;
import com.pedrosantos.conversationdisplayer.datasources.BaseDataSource;
import com.pedrosantos.conversationdisplayer.views.fragments.callbacks.BaseUICallback;
import com.pedrosantos.conversationdisplayer.models.app.CDError;
import com.pedrosantos.conversationdisplayer.utils.Constants;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDataSource.detachUICallback();
        mDataSource = null;
    }

    public ActivityCallback getActivityCallback() {
        if (getActivity() != null) {
            return ((ActivityCallback) getActivity());
        }
        return null;
    }

    @Override
    public void onNetworkError(final CDError error) {
        if (error != null && getView() != null) {
            if (error.getCode() == Constants.NETWORK_ERROR) {
                Snackbar.make(getView(), getString(R.string.check_your_internet_connection), Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(getView(), getString(R.string.we_had_a_problem_please_try_again, error.getCode(), error.getMessage()), Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
