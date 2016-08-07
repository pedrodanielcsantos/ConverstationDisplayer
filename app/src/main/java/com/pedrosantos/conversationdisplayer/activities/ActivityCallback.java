package com.pedrosantos.conversationdisplayer.activities;

import android.support.v4.app.Fragment;

/**
 * Callback to be implemented by application's activities that enumerates callable actions from the
 * fragments on the parent activity, to avoid constant casts.
 */
public interface ActivityCallback {

    /**
     * Adds a fragment to the current container with tag passed as parameter
     */
    void addFragment(final Fragment fragment, final String tag);

    /**
     * Method that replaces the current fragment for a new one.
     *
     * @param fragment       new fragment
     * @param tag            tag of new fragment
     * @param clearBackStack if the back stack should be cleared or not.
     */
    void replaceFragment(final Fragment fragment, final String tag, final boolean clearBackStack);

}
