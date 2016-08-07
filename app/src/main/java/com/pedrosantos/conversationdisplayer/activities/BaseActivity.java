package com.pedrosantos.conversationdisplayer.activities;

import com.pedrosantos.conversationdisplayer.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 *
 */
public abstract class BaseActivity extends AppCompatActivity implements ActivityCallback {

    @Override
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

    @Override
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
