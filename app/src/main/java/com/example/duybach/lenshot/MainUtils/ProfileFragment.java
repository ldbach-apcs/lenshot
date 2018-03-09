package com.example.duybach.lenshot.MainUtils;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.duybach.lenshot.MainActivity;
import com.example.duybach.lenshot.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.example.duybach.lenshot.AuthenticActivity.LOGGED_IN_USER;
import static com.example.duybach.lenshot.FirebaseClass.FIREBASE_URL;

/**
 * Created by duybach on 8/31/17.
 */

public class ProfileFragment extends MainFragment {

    Firebase f;
    FloatingActionButton fabSignOut;
    FrameLayout contentContainer;
    ImageView ivProfile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        f = new Firebase(FIREBASE_URL).child("users").child(getUser());

        fabSignOut = (FloatingActionButton) v.findViewById(R.id.fab_sign_out);
        contentContainer = (FrameLayout) v.findViewById(R.id.container_personal_posts);
        ivProfile = (ImageView) v.findViewById(R.id.iv_profile);

        setUpButton();
        loadProfilePicture();

        return v;
    }

    private String getUser() {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        return pref.getString(LOGGED_IN_USER, "null");
    }

    private void loadProfilePicture() {
        f.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Uri uri =  Uri.parse(dataSnapshot.child("profilePic").getValue(String.class));
                Picasso.with(
                        getActivity().getApplicationContext()).load(uri).into(ivProfile);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void setUpButton() {
        fabSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).signOut();
            }
        });
    }
}
