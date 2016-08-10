package com.pedrosantos.conversationdisplayer.fragments.callbacks;

import com.pedrosantos.conversationdisplayer.models.app.CDError;

/**
 * Base callback for all datasources to call when they need to communicate with the UI.
 * It has the methods common to all datasources.
 * Needed methods for each specific screen/datasource should be declared in each callback declaration.
 */
public interface BaseUICallback {

    void onNetworkError(CDError error);
}
