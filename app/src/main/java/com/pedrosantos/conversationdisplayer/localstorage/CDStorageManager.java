package com.pedrosantos.conversationdisplayer.localstorage;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Wrapper around the local storage mechanism, to be accessed from other application components
 * without the need to know which is exactly the mechanism.
 */
public class CDStorageManager {

    private static CDStorageManager mManager;
    private final Realm mRealm;

    private CDStorageManager(final Context context) {
        // Create the Realm configuration
        final RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
        // Open the Realm for the UI thread.
        mRealm = Realm.getInstance(realmConfig);

    }

    public static void init(Context context) {
        mManager = new CDStorageManager(context);
    }

    public static CDStorageManager getInstance() {
        if (mManager == null) {
            throw new NullPointerException("CDStorageManager needs to be initialized on app start. Please call init(Context) first.");
        }
        return mManager;
    }

    /**
     * Stores an object to local storage. If clearOtherRecords is set as true, deletes all other
     * records of the same class before saving the new one.
     */
    public <T extends RealmObject> void storeObject(T objectToSave, boolean clearOtherRecords) {
        mRealm.beginTransaction();
        if (clearOtherRecords) {
            mRealm.delete(objectToSave.getClass());
        }
        mRealm.copyToRealm(objectToSave);
        mRealm.commitTransaction();
    }

    /**
     * Returns the last stored object of type (table) T. If there's none, returns null.
     */
    public <T extends RealmObject> T getLastStoredObjectFromPersistence(Class<T> classToRetrieve) {
        final RealmResults<T> results = mRealm.where(classToRetrieve).findAll();
        if (results != null && results.size() > 0) {
            return results.get(results.size() - 1);
        }
        return null;
    }

}
