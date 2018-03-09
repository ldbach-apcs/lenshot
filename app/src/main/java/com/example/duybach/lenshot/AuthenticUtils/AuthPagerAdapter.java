package com.example.duybach.lenshot.AuthenticUtils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by duybach on 8/25/17.
 */

public class AuthPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 2;

    public AuthPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return (position == 0) ? new SignInFrag() : new SignUpFrag();
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    public static Fragment getActiveFragment(ViewPager container, int position, FragmentManager manager) {
        String name = makeFragmentName(container.getId(), position);
        return  manager.findFragmentByTag(name);
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }
}
