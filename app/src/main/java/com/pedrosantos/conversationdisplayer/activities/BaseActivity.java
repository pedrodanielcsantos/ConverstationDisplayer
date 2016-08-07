package com.pedrosantos.conversationdisplayer.activities;

import com.pedrosantos.conversationdisplayer.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 *
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Adds a fragment to the current
     */
    public void addFragment(final Fragment fragment, final String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            if (fragment != null && tag != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragments_container, fragment, tag)
                        .addToBackStack(tag)
                        .commit();
            }
        }
    }

    /**
     * Method that replaces the current fragment for a new one.
     *
     * @param fragment       new fragment
     * @param tag            tag of new fragment
     * @param clearBackStack if the back stack should be cleared or not.
     */
    public void replaceFragment(final Fragment fragment, final String tag, final boolean clearBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            if (clearBackStack) {
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            fragmentManager.beginTransaction()
                    .replace(R.id.fragments_container, fragment, tag)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (!getSupportFragmentManager().popBackStackImmediate()) {
            supportFinishAfterTransition();
        }
    }
}
