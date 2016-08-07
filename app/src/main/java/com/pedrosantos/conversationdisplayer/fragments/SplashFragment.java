package com.pedrosantos.conversationdisplayer.fragments;

import com.pedrosantos.conversationdisplayer.R;
import com.pedrosantos.conversationdisplayer.datasources.SplashDataSource;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Fragment that presents to the user the form to input his username before loading the
 * conversation.
 */
public class SplashFragment extends BaseFragment<SplashDataSource> {

    public static final String TAG = "SplashFragment";

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextInputLayout usernameInputFieldLayout = (TextInputLayout) view.findViewById(R.id.splash_username_input_layout);
        final EditText usernameInputField = (EditText) view.findViewById(R.id.splash_username_input);
        final Button submitButton = (Button) view.findViewById(R.id.splash_submit_button);

        usernameInputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
                //Nothing to do here.
            }

            @Override
            public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
                usernameInputFieldLayout.setError(null);
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                //Nothing to do here.
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (mDataSource.isUsernameValid(usernameInputField.getText())) {
                    if (getActivityCallback() != null) {
                        getActivityCallback().replaceFragment(MessageListFragment.newInstance(), MessageListFragment.TAG, true);
                    }
                } else {
                    usernameInputFieldLayout.setError(getString(R.string.please_add_valid, getString(R.string.username)));
                }
            }
        });
    }
}
