package com.pedrosantos.conversationdisplayer.models.app;

/**
 * Helper model, to map a network error, when performing api requests.
 */
public class CDError {
    private final int mCode;
    private final String mMessage;

    public CDError(final int code, final String message) {
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
