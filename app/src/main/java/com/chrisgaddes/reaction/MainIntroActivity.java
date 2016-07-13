package com.chrisgaddes.reaction;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy;
import com.heinrichreimersoftware.materialintro.app.OnNavigationBlockedListener;

/**
 * Created by cagad on 7/11/2016.
 */
public class MainIntroActivity extends IntroActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

                /* Enable/disable fullscreen */
        setFullscreen(true);

        super.onCreate(savedInstanceState);

        /* Enable/disable skip button */
        setSkipEnabled(true);

        /* Enable/disable finish button */
        setFinishEnabled(true);

        /* Add a navigation policy to define when users can go forward/backward */
        setNavigationPolicy(new NavigationPolicy() {
            @Override
            public boolean canGoForward(int position) {
                return true;
            }

            @Override
            public boolean canGoBackward(int position) {
                return false;
            }
        });

        /* Add a listener to detect ehen users try to go to a page they can't go to */
        addOnNavigationBlockedListener(new OnNavigationBlockedListener() {
            @Override
            public void onNavigationBlocked(int position, int direction) {
            }
        });

        /* Add your own page change listeners */
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

}
