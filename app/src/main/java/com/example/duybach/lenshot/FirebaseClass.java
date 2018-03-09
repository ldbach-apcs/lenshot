package com.example.duybach.lenshot;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by duybach on 8/26/17.
 */

public class FirebaseClass extends Application {

    public static final String FIREBASE_URL = "https://lenshot-cs426.firebaseio.com/";

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
