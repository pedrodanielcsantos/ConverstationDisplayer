package com.pedrosantos.conversationdisplayer.activities;

import com.pedrosantos.conversationdisplayer.R;
import com.pedrosantos.conversationdisplayer.fragments.SplashFragment;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //When the activity (re) starts, the first fragment visible is the SplashFragment.
        replaceFragment(SplashFragment.newInstance(), SplashFragment.TAG, true);
    }
}
