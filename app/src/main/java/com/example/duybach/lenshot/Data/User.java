package com.example.duybach.lenshot.Data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.example.duybach.lenshot.FirebaseClass;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by duybach on 8/28/17.
 */

public class User {
    {
        posts = new ArrayList<>();
        followers = new ArrayList<>();
        followings = new ArrayList<>();
        profilePic = null;
    }

    private String username, password;
    private Uri profilePic;
    private ArrayList<String> posts, followers, followings;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Map<String, Object> generateFirebaseStorageItem() {
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("password", password);
        // To be implemented
        itemMap.put("posts", posts);
        itemMap.put("followers", followers);
        itemMap.put("followings", followings);
        itemMap.put("profilePic", profilePic.toString());
        // itemMap.put("profilePic", Arrays.toString(convertImgToByteArray()));
        return itemMap;
    }

    public void setImageProfile(Uri imageProfile) {
        this.profilePic = imageProfile;
    }

    public String getUsername() {
        return username;
    }

    //private byte[] convertImgToByteArray() {
    //    Bitmap bitmap = ((BitmapDrawable) profilePic).getBitmap();
    //    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    //    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    //    return stream.toByteArray();
    //}
}
