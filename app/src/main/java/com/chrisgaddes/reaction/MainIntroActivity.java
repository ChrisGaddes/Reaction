package com.chrisgaddes.reaction;

import android.Manifest;
import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class MainIntroActivity extends IntroActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Standard slide (like Google's intros)
         */
        addSlide(new SimpleSlide.Builder()
                .title("Hi")
                .description("Hello")
                .image(R.drawable.prob1)
                .background(R.color.colorWhiteBackground)
                .backgroundDark(R.color.colorGrayBackground)
                .permission(Manifest.permission.CAMERA)
                .build());

        /**
         * Custom fragment slide
         */
//        addSlide(new FragmentSlide.Builder()
//                .background(R.color.background_2)
//                .backgroundDark(R.color.background_dark_2)
//                .fragment(R.layout.fragment_2, R.style.FragmentTheme)
//                .build());
    }

}
