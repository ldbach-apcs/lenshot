package com.example.duybach.lenshot.AuthenticUtils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duybach.lenshot.MainActivity;
import com.example.duybach.lenshot.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import static com.example.duybach.lenshot.AuthenticActivity.LOGGED_IN_USER;
import static com.example.duybach.lenshot.FirebaseClass.FIREBASE_URL;

/**
 * Created by duybach on 8/25/17.
 */

public class SignInFrag extends AuthFrag {
    private static final String USERNAME_INPUT = "Username inputted";
    private static final String PASSWORD_INPUT = "Password inputted";
    EditText etUsrName;
    TextInputEditText etPassword;
    TextView tvForgetPassword;

    private static final String CODE_WRONG_INFO = "Sorry, invalid username or password";

    @Override
    public void handleAuthentication() {
        final String inUsername = etUsrName.getText().toString();
        final String inPassword = etPassword.getText().toString();
        Firebase mDbUser = new Firebase(FIREBASE_URL).child("users");

        mDbUser.child(inUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists() ||
                        !inPassword.equals(dataSnapshot.child("password").getValue().toString())) {
                    handleNegativeAction(CODE_WRONG_INFO);
                }
                else {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                    SharedPreferences.Editor e = preferences.edit();
                    e.putString(LOGGED_IN_USER, inUsername);
                    e.apply();
                    startMain();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void handleNegativeAction(String actionCode) {
        Toast.makeText(getContext(), actionCode, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_sign_in, container, false);
        etUsrName = (EditText) v.findViewById(R.id.et_username);
        etPassword = (TextInputEditText) v.findViewById(R.id.et_password);
        tvForgetPassword = (TextView) v.findViewById(R.id.tv_forget);

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do something
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(USERNAME_INPUT, etUsrName.getText().toString());
        outState.putString(PASSWORD_INPUT, etPassword.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            etUsrName.setText(savedInstanceState.getString(USERNAME_INPUT));
            etPassword.setText(savedInstanceState.getString(PASSWORD_INPUT));
        }
    }
}
