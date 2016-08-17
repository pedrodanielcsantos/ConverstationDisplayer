package com.pedrosantos.conversationdisplayer.datasources;

import com.google.gson.Gson;

import com.pedrosantos.conversationdisplayer.R;
import com.pedrosantos.conversationdisplayer.models.app.CDError;
import com.pedrosantos.conversationdisplayer.views.fragments.callbacks.BaseUICallback;

import org.junit.runner.RunWith;

import android.support.test.runner.AndroidJUnit4;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;

/**
 * Base class for all unit tests on data sources
 */
@RunWith(AndroidJUnit4.class)
public abstract class BaseDataSourceUnitTest<DS extends BaseDataSource> implements BaseUICallback {
    protected DS mDataSource;
    private Gson mGsonConverter;

    /**
     * Constructor. Tries to intialize the datasource and attach the callback to it.
     */
    public BaseDataSourceUnitTest() {
        final ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        final Class<DS> dataSourceType = (Class<DS>) superClass.getActualTypeArguments()[0];
        try {
            mDataSource = dataSourceType.newInstance();
            mDataSource.attachUICallback(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mGsonConverter = new Gson();
    }

    @Override
    public void onNetworkError(final CDError error) {
        //Not used at the moment.
    }

    /**
     * Dummy get string. Returning defaults in english.
     */
    @Override
    public String getTranslatedString(final int stringResourceId) {
        switch (stringResourceId) {
            case R.string.before:
                return "before";
            case R.string.after:
                return "after";
            case R.string.from:
                return "from";
            default:
                return null;
        }
    }

    /**
     * Converts a JSON string to an object of type T.
     */
    public <T> T getConvertedObjectFromString(Class<T> classToRetrieve, String jsonObject) {
        return mGsonConverter.fromJson(jsonObject, classToRetrieve);
    }

    /**
     * Returns a string with the content of the file with fileName placed inside folder with name
     * folder, the instrumentation test resources package.
     */
    protected String getFileFromAssets(final String folder, final String fileName) {
        try {
            String file = folder + "/" + fileName;
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream(file);

            int count;
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(stream.available());

            while (true) {
                count = stream.read(buffer);
                if (count <= 0) {
                    break;
                }
                byteStream.write(buffer, 0, count);
            }

            return byteStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
