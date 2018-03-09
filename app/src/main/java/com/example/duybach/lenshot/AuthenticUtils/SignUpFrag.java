package com.example.duybach.lenshot.AuthenticUtils;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duybach.lenshot.Data.User;
import com.example.duybach.lenshot.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.example.duybach.lenshot.AuthenticActivity.LOGGED_IN_USER;
import static com.example.duybach.lenshot.FirebaseClass.FIREBASE_URL;

/**
 * Created by duybach on 8/25/17.
 */

public class SignUpFrag extends AuthFrag {
    private static final String USERNAME_INPUT = "Username inputted";
    private static final String PASSWORD_INPUT = "Password inputted";
    private static final String PASSWORD_REP_INPUT = "Password rep inputted";
    private static final String CODE_USERNAME_NOT_UNIQUE = "Sorry, this user name is already taken";
    private static final String CODE_PASSWORD_NOT_MATCH = "Please review your passwords, they do not match";

    EditText etUsrName;
    TextInputEditText etPassword, etRepPassword;
    private Firebase mDbUser;
    private ProgressDialog progressDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbUser = new Firebase(FIREBASE_URL).child("users");
    }

    @Override
    public void handleAuthentication() {
        // Check if password matches
        String inPassword = etPassword.getText().toString().trim();
        String inRepPassword = etRepPassword.getText().toString().trim();

        if (inPassword.equals("") || !inPassword.equals(inRepPassword)) {
            handleNegativeAction(CODE_PASSWORD_NOT_MATCH);
            return;
        }

        // Check username valid and not taken
        String inUsername = etUsrName.getText().toString().trim();

        // Call Firebase and add User
        completeRegistration(inUsername, inPassword);
    }

    private void completeRegistration(final String inUsername, final String inPassword) {
        // Save data to database Server

        mDbUser.child(inUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    handleNegativeAction(CODE_USERNAME_NOT_UNIQUE);
                }
                else {
                    signUserUp(inUsername, inPassword);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void signUserUp(String inUsername, String inPassword) {
        User user = new User(inUsername, inPassword);

        // Add profile pic to Firebase Storage
        Uri uri = resourceToUri(getContext(), R.drawable.img_profile_default);
        uploadImage(uri, user);
    }

    private void onImageUploaded(String inUsername, User user) {
        // Add Data to Firebase
        mDbUser.child(inUsername)
                .setValue(user.generateFirebaseStorageItem(), new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {

                    }
                });


        // Log the user in
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor e = preferences.edit();
        e.putString(LOGGED_IN_USER, inUsername);
        e.apply();


        // Toast.makeText(getContext(), "Logged in as: " + inUsername, Toast.LENGTH_SHORT).show();

        startMain();
    }

    private void uploadImage(Uri uri, final User user) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        UploadTask uploadTask = ref.child(
                "images/" + uri.getLastPathSegment()).putFile(uri);

        //creating and showing progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMax(100);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        progressDialog.setCancelable(false);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress =
                        (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.incrementProgressBy((int) progress);
            }
        });

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                user.setImageProfile(taskSnapshot.getDownloadUrl());
                onImageUploaded(user.getUsername(), user);
                progressDialog.dismiss();

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
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        etUsrName = (EditText) v.findViewById(R.id.et_username);
        etPassword = (TextInputEditText) v.findViewById(R.id.et_password);
        etRepPassword = (TextInputEditText) v.findViewById(R.id.et_confirm_password);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(USERNAME_INPUT, etUsrName.getText().toString());
        outState.putString(PASSWORD_INPUT, etPassword.getText().toString());
        outState.putString(PASSWORD_REP_INPUT, etRepPassword.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            etUsrName.setText(savedInstanceState.getString(USERNAME_INPUT));
            etPassword.setText(savedInstanceState.getString(PASSWORD_INPUT));
            etRepPassword.setText(savedInstanceState.getString(PASSWORD_REP_INPUT));
        }
    }

    private Uri resourceToUri(Context context, int resID) {
        Resources resources = context.getResources();
        return new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(resID))
                .appendPath(resources.getResourceTypeName(resID))
                .appendPath(resources.getResourceEntryName(resID))
                .build();
    }
}
