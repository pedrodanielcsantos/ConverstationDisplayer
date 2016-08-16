package com.pedrosantos.conversationdisplayer.datasources;

import com.pedrosantos.conversationdisplayer.models.app.CDError;
import com.pedrosantos.conversationdisplayer.views.fragments.callbacks.BaseUICallback;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import android.text.TextUtils;

import java.lang.reflect.ParameterizedType;

import static org.mockito.Matchers.any;

/**
 * Base class for all unit tests on data sources
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(TextUtils.class)
public abstract class BaseDataSourceUnitTest<DS extends BaseDataSource> implements BaseUICallback {
    protected DS mDataSource;

    public BaseDataSourceUnitTest() {
        final ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        final Class<DS> dataSourceType = (Class<DS>) superClass.getActualTypeArguments()[0];
        try {
            mDataSource = dataSourceType.newInstance();
            mDataSource.attachUICallback(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setup() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.when(TextUtils.isEmpty(any(CharSequence.class))).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                CharSequence a = (CharSequence) invocation.getArguments()[0];
                return !(a != null && a.length() > 0);
            }
        });
    }

    @Override
    public void onNetworkError(final CDError error) {
        //TODO
    }

    @Override
    public String getTranslatedString(final int stringResourceId) {
        //TODO
        return null;
    }
}
