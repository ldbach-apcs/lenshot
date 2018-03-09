package com.example.duybach.lenshot;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;

public class IntroActivity extends AppIntro2 {

    private static final int NUM_INTRO_SLIDES = 3;
    private static final CharSequence[] TITLE_SLIDE = {"Spring", "Fall", "Winter"};
    private static final CharSequence[] DESCRIPTION_SLIDE = {"Where flora blossoms", "Picturesquely yellow", "Freezing hail"};
    private static final int[] IMAGE_ID_SLIDE = {R.drawable.spring, R.drawable.fall, R.drawable.winter};
    private static final int[] BACKGROUND_COLOR_SLIDE = {Color.LTGRAY, Color.DKGRAY, Color.WHITE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add slides
        for (int i = 0 ; i < NUM_INTRO_SLIDES; ++i) {
            addSlide(AppIntro2Fragment.newInstance(TITLE_SLIDE[i],
                    DESCRIPTION_SLIDE[i],
                    IMAGE_ID_SLIDE[i],
                    BACKGROUND_COLOR_SLIDE[i]));
        }


        // Add transition
        setColorTransitionsEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        this.finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        this.finish();
    }
}
