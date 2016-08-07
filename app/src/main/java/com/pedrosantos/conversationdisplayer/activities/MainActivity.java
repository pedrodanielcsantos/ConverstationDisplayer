package com.pedrosantos.conversationdisplayer.activities;

import com.pedrosantos.conversationdisplayer.R;
import com.pedrosantos.conversationdisplayer.fragments.MessageListFragment;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(MessageListFragment.newInstance(), MessageListFragment.TAG, true);
    }
}
