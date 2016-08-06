package com.pedrosantos.conversationdisplayer.models;

/**
 * Helper model, to map a network error, when performing api requests.
 */
public class CDNetworkError {
    private final int mCode;
    private final String mMessage;

    public CDNetworkError(final int code, final String message) {
        mCode = code;
        mMessage = message;
    }

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }
}
