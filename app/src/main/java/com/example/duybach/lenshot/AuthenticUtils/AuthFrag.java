package com.example.duybach.lenshot.AuthenticUtils;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.duybach.lenshot.MainActivity;

import static com.example.duybach.lenshot.AuthenticActivity.LOGGED_IN_USER;

/**
 * Created by duybach on 8/25/17.
 */

public abstract class AuthFrag extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext())
                .getString(LOGGED_IN_USER, "null")
                .equals("null")) {
            // LOGGED in already
            startMain();
        }
    }

    // Called when user press next button on the AuthenticationScreen
    public abstract void handleAuthentication();

    // Called when successful authentication
    public void handlePositiveAction() {
        getActivity().finish();
    }

    // Called when failed authentication
    public abstract void handleNegativeAction(String actionCode);

    protected void startMain() {
        Intent mainActivity = new Intent(getContext(), MainActivity.class);
        startActivity(mainActivity);
        handlePositiveAction();
    }
}
